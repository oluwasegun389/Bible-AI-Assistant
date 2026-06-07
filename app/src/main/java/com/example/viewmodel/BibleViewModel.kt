package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.database.*
import com.example.network.GeminiClient
import com.example.network.QuizQuestion
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class BibleViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repository = BibleRepository(db)

    // Reactively observe DB tables via Repository
    val notes = repository.allNotes.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val bookmarks = repository.allBookmarks.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val streaks = repository.streakFlow.stateIn(viewModelScope, SharingStarted.Lazily, StreakEntity())
    val prayerRequests = repository.allPrayerRequests.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val badges = repository.allBadges.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val favoriteHymns = repository.favoriteHymns.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val activityLogs = repository.allActivityLogs.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // --- BIBLE READER STATE ---
    var selectedBookIndex = MutableStateFlow(BibleData.books.indexOfFirst { it.name == "Psalms" }.coerceAtLeast(0)) // Defaults to Psalms dynamically
    var selectedChapterIndex = MutableStateFlow(0) // Chapter 23 (index 0 for Psalms)
    var activeVersion = MutableStateFlow("KJV") // "KJV", "WEB", "AMP"
    var activeDisplayMode = MutableStateFlow("SINGLE") // "SINGLE", "PARALLEL", "COMPARISON"
    
    // Bottom Sheet options or Dialog options for verse
    val selectedVerse = MutableStateFlow<BibleVerse?>(null)
    
    val loadedChapters = MutableStateFlow<Map<String, List<BibleVerse>>>(emptyMap())
    val isChapterLoading = MutableStateFlow(false)
    
    val currentBook: StateFlow<BibleBook> = selectedBookIndex.map { index ->
        BibleData.books.getOrElse(index) { BibleData.books.firstOrNull { it.name == "Psalms" } ?: BibleData.books[0] }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), BibleData.books.firstOrNull { it.name == "Psalms" } ?: BibleData.books[0])

    val currentChapter: StateFlow<BibleChapter> = combine(currentBook, selectedChapterIndex, loadedChapters) { book, chIdx, loaded ->
        val defaultChapter = book.chapters.getOrElse(chIdx) { book.chapters[0] }
        val key = "${book.name}_${defaultChapter.chapterNumber}"
        val loadedVerses = loaded[key]
        if (loadedVerses != null) {
            defaultChapter.copy(verses = loadedVerses)
        } else {
            defaultChapter
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), (BibleData.books.firstOrNull { it.name == "Psalms" } ?: BibleData.books[0]).chapters[0])

    // --- AI CHATBOT / ASSISTANT STATE ---
    private val _chatHistory = MutableStateFlow<List<Pair<String, Boolean>>>(
        listOf(
            "Welcome! I am your AI Biblical study assistant. Ask me questions about passages, request sermon outlines, generate personalized prayers, or study complex Christian doctrine." to false
        )
    )
    val chatHistory: StateFlow<List<Pair<String, Boolean>>> = _chatHistory.asStateFlow()
    
    val isChatLoading = MutableStateFlow(false)
    val chatInput = MutableStateFlow("")

    // Preset Prompts
    val presets = listOf(
        "Explain John 3:16" to "Explain what John 3:16 means in simple language.",
        "Anxiety Scriptures" to "Please give me key biblical verses and guidance about dealing with anxiety and fear.",
        "Outline on Grace" to "Act as a pastor. Outline an inspiring sermon on 'Grace and Redemption' with scripture references.",
        "Original Meaning" to "Explain the historical background and original greek meaning of John 1:1."
    )

    // --- AI QUIZ STATE ---
    val isQuizLoading = MutableStateFlow(false)
    val activeQuizList = MutableStateFlow<List<QuizQuestion>>(emptyList())
    val currentQuizTopic = MutableStateFlow("Faith")
    val currentQuizIndex = MutableStateFlow(0)
    val selectedOption = MutableStateFlow<Int?>(null)
    val quizScore = MutableStateFlow(0)
    val showQuizExplanation = MutableStateFlow(false)
    val quizCompleted = MutableStateFlow(false)
    val answeredCorrectly = MutableStateFlow<Boolean?>(null)

    // --- HYMNS STATE ---
    var hymnQuery = MutableStateFlow("")
    var hymnCategory = MutableStateFlow("All") // "All", "Worship", "Praise", "Thanksgiving", "Faith"
    var selectedHymn = MutableStateFlow<Hymn?>(null)
    var activeHymnLang = MutableStateFlow("English") // "English", "Yoruba", "Igbo", "Hausa"
    var isHymnPlaying = MutableStateFlow(false) // Instrumental player simulation
    var isHymnFavorite = MutableStateFlow(false)
    val customHymns = MutableStateFlow<List<Hymn>>(emptyList())
    val isHymnSearchLoading = MutableStateFlow(false)

    // --- NEW NOTE/PRAYER INPUT ---
    val noteTitle = MutableStateFlow("")
    val noteContent = MutableStateFlow("")
    
    val pTitle = MutableStateFlow("")
    val pDesc = MutableStateFlow("")
    val pAuthor = MutableStateFlow("")

    init {
        // Unlock baseline starting badge on launch
        viewModelScope.launch {
            repository.badgeDao.unlockBadge(
                BadgeEntity("WELCOME", "Spiritual Seeker", "Initialized the AI Bible study platform and set up spiritual goals.")
            )
        }

        // Reactively observe book and chapter choices and load if they are placeholders
        viewModelScope.launch {
            combine(currentBook, selectedChapterIndex) { book, chIdx ->
                val ch = book.chapters.getOrElse(chIdx) { book.chapters[0] }
                book.name to ch
            }.collect { (bookName, chapter) ->
                checkAndLoadChapter(bookName, chapter.chapterNumber, chapter.verses)
            }
        }
    }

    fun checkAndLoadChapter(bookName: String, chapterNumber: Int, verses: List<BibleVerse>) {
        val key = "${bookName}_${chapterNumber}"
        if (loadedChapters.value.containsKey(key)) return // Already loaded
        
        // Check if the chapter contains standard instruction or placeholder markers
        val isPlaceholder = verses.size <= 1 && (
            verses.firstOrNull()?.kjvText?.contains("Welcome to") == true ||
            verses.firstOrNull()?.kjvText?.contains("...") == true ||
            verses.firstOrNull()?.kjvText?.contains("Tap to synchronize") == true
        )
        
        if (isPlaceholder) {
            isChapterLoading.value = true
            viewModelScope.launch {
                // 1. Try from public REST API
                var fetched = GeminiClient.fetchChapterFromApi(bookName, chapterNumber)
                
                // 2. Fallback to Gemini AI translation
                if (fetched == null || fetched.isEmpty()) {
                    fetched = GeminiClient.fetchChapterFromGemini(bookName, chapterNumber)
                }
                
                if (fetched != null && fetched.isNotEmpty()) {
                    loadedChapters.value = loadedChapters.value + (key to fetched)
                }
                isChapterLoading.value = false
            }
        }
    }

    // --- BIBLE READER ACTIONS ---
    fun selectBook(index: Int) {
        selectedBookIndex.value = index
        selectedChapterIndex.value = 0
    }

    fun selectChapter(index: Int) {
        selectedChapterIndex.value = index
    }

    fun setVersion(version: String) {
        activeVersion.value = version
    }

    fun setDisplayMode(mode: String) {
        activeDisplayMode.value = mode
    }

    fun selectVerse(verse: BibleVerse?) {
        selectedVerse.value = verse
        if (verse != null) {
            // Check if favorited favorited/bookmarked in database
            viewModelScope.launch {
                val ref = "${currentBook.value.name} ${currentChapter.value.chapterNumber}:${verse.number}"
                val bookmarked = db.bookmarkDao().isBookmarked(ref)
                // We're just updating the UI or executing operations
            }
        }
    }

    fun toggleBookmark(verse: BibleVerse) {
        viewModelScope.launch {
            val ref = "${currentBook.value.name} ${currentChapter.value.chapterNumber}:${verse.number}"
            val isBookmarked = db.bookmarkDao().isBookmarked(ref)
            if (isBookmarked) {
                db.bookmarkDao().deleteBookmark(ref)
            } else {
                db.bookmarkDao().insertBookmark(BookmarkEntity(verseReference = ref))
                repository.activityLogDao.insertLog(ActivityLogEntity(type = "READ", value = "Bookmarked $ref"))
            }
        }
    }

    fun addNoteForVerse(verse: BibleVerse, title: String, content: String) {
        if (title.isBlank() || content.isBlank()) return
        viewModelScope.launch {
            val ref = "${currentBook.value.name} ${currentChapter.value.chapterNumber}:${verse.number}"
            db.noteDao().insertNote(
                NoteEntity(
                    title = title,
                    content = content,
                    verseReference = ref
                )
            )
            repository.activityLogDao.insertLog(ActivityLogEntity(type = "READ", value = "Added study note for $ref"))
            // Clear inputs
            noteTitle.value = ""
            noteContent.value = ""
            selectedVerse.value = null
        }
    }

    fun deleteNote(id: Int) {
        viewModelScope.launch {
            db.noteDao().deleteNote(id)
        }
    }

    fun markChapterAsRead() {
        viewModelScope.launch {
            repository.recordReading(currentBook.value.name, currentChapter.value.chapterNumber)
        }
    }

    // --- AI CHAT ACTIONS ---
    fun sendChatMessage(text: String) {
        if (text.isBlank()) return
        val currentInputText = text.trim()
        chatInput.value = ""
        
        // Append user prompt to list
        _chatHistory.value = _chatHistory.value + (currentInputText to true)
        isChatLoading.value = true

        viewModelScope.launch {
            val sysInstruction = """
                You are high-fidelity Christian scripture analysis companion.
                Your response must be theological, faith-lifting, supportive and aligned with standard Christian teachings.
                Maintain a compassionate, academic but spiritual posture. 
                Explain biblical events, Hebrew/Greek roots where relevant, but relate everything back to modern godly wisdom and application.
                Do not criticize any particular Christian denomination. Direct all answers to glorify Jesus Christ.
            """.trimIndent()
            
            val response = GeminiClient.generateContent(currentInputText, sysInstruction)
            _chatHistory.value = _chatHistory.value + (response to false)
            isChatLoading.value = false
            
            // Log that user is actively engaging with study tool
            repository.recordDevotional("Engaging AI Assistant: '${currentInputText.take(20)}...'")
        }
    }

    fun clearChat() {
        _chatHistory.value = listOf(
            "Welcome! I am your AI Biblical study assistant. Ask me questions about passages, request sermon outlines, generate personalized prayers, or study complex Christian doctrine." to false
        )
    }

    // --- AI QUIZ ACTIONS ---
    fun startQuiz(topic: String) {
        currentQuizTopic.value = topic
        isQuizLoading.value = true
        currentQuizIndex.value = 0
        selectedOption.value = null
        quizScore.value = 0
        showQuizExplanation.value = false
        quizCompleted.value = false
        answeredCorrectly.value = null

        viewModelScope.launch {
            val list = GeminiClient.generateQuiz(topic)
            activeQuizList.value = list
            isQuizLoading.value = false
        }
    }

    fun selectQuizOption(index: Int) {
        if (selectedOption.value != null) return // Already submitted
        selectedOption.value = index
        val currentQuestion = activeQuizList.value.getOrNull(currentQuizIndex.value)
        if (currentQuestion != null) {
            val correct = index == currentQuestion.correctAnswerIndex
            answeredCorrectly.value = correct
            if (correct) {
                quizScore.value += 1
            }
            showQuizExplanation.value = true
        }
    }

    fun nextQuizQuestion() {
        val nextIdx = currentQuizIndex.value + 1
        if (nextIdx < activeQuizList.value.size) {
            currentQuizIndex.value = nextIdx
            selectedOption.value = null
            showQuizExplanation.value = false
            answeredCorrectly.value = null
        } else {
            quizCompleted.value = true
            // Save results to activity logs & earn gamification credits
            viewModelScope.launch {
                repository.recordQuizScore(quizScore.value, activeQuizList.value.size, currentQuizTopic.value)
            }
        }
    }

    // --- HYMNS ACTIONS ---
    val filteredHymns: Flow<List<Hymn>> = combine(hymnQuery, hymnCategory, customHymns) { query, category, customList ->
        var list = BibleData.hymns + customList
        list = list.distinctBy { it.id }
        if (category != "All") {
            list = list.filter { it.category.equals(category, ignoreCase = true) }
        }
        if (query.isNotBlank()) {
            list = list.filter {
                it.title.contains(query, ignoreCase = true) ||
                it.lyricsEnglish.contains(query, ignoreCase = true) ||
                it.lyricsYoruba.contains(query, ignoreCase = true) ||
                it.lyricsIgbo.contains(query, ignoreCase = true) ||
                it.lyricsHausa.contains(query, ignoreCase = true) ||
                it.hymnNumber.toString() == query.trim()
            }
        }
        list
    }.stateIn(viewModelScope, SharingStarted.Lazily, BibleData.hymns)

    fun searchHymnOnline() {
        val query = hymnQuery.value.trim()
        if (query.isBlank()) return
        isHymnSearchLoading.value = true
        viewModelScope.launch {
            val hymn = GeminiClient.fetchHymnFromGemini(query)
            if (hymn != null) {
                val updated = customHymns.value.toMutableList()
                updated.removeAll { it.title.equals(hymn.title, ignoreCase = true) }
                updated.add(0, hymn)
                customHymns.value = updated
                selectHymn(hymn)
            }
            isHymnSearchLoading.value = false
        }
    }

    fun selectHymn(hymn: Hymn?) {
        selectedHymn.value = hymn
        isHymnPlaying.value = false
        if (hymn != null) {
            // check favorite
            viewModelScope.launch {
                db.favoriteHymnDao().getAllFavoriteHymns().collectLatest { list ->
                    isHymnFavorite.value = list.any { it.hymnId == hymn.id }
                }
            }
        }
    }

    fun toggleHymnFavorite(hymnId: Int, lang: String) {
        viewModelScope.launch {
            repository.toggleFavoriteHymn(hymnId, lang)
            isHymnFavorite.value = !isHymnFavorite.value
        }
    }

    fun toggleHymnPlayback() {
        isHymnPlaying.value = !isHymnPlaying.value
    }

    // --- COMMUNITY ACTIONS ---
    fun submitPrayerRequest() {
        val title = pTitle.value.trim()
        val desc = pDesc.value.trim()
        val authorName = pAuthor.value.trim().ifBlank { "Faithful Believer" }

        if (title.isBlank() || desc.isBlank()) return
        viewModelScope.launch {
            repository.addPrayerRequest(title, desc, authorName)
            pTitle.value = ""
            pDesc.value = ""
            pAuthor.value = ""
        }
    }

    fun amenPrayer(prayer: PrayerRequestEntity) {
        viewModelScope.launch {
            repository.amenPrayerRequest(prayer.id, prayer.amenCount, prayer.hasAmened)
            // Record prayer streaks
            repository.recordPrayer(prayer.title)
        }
    }
}
