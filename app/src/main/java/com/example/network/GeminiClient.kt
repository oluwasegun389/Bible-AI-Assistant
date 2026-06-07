package com.example.network

import com.example.BuildConfig
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

data class QuizQuestion(
    val question: String,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val explanation: String
)

object GeminiClient {

    private const val API_KEY = BuildConfig.GEMINI_API_KEY
    private const val MODEL_NAME = "gemini-3.5-flash"
    private const val ENDPOINT = "https://generativelanguage.googleapis.com/v1beta/models/$MODEL_NAME:generateContent?key=$API_KEY"

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    // General Study / Ask Questions
    suspend fun generateContent(prompt: String, systemInstruction: String? = null): String = withContext(Dispatchers.IO) {
        if (API_KEY.isEmpty() || API_KEY == "MY_GEMINI_API_KEY") {
            return@withContext "API Key is missing. Please enter your GEMINI_API_KEY securely in the Secrets panel in AI Studio.\n\n(No hardcoded keys are allowed inside code for security)."
        }

        try {
            val root = JSONObject()
            val contentsArray = JSONArray()
            val contentObj = JSONObject()
            val partsArray = JSONArray()
            val partObj = JSONObject()
            partObj.put("text", prompt)
            partsArray.put(partObj)
            contentObj.put("parts", partsArray)
            contentsArray.put(contentObj)
            root.put("contents", contentsArray)

            if (systemInstruction != null) {
                val sysInstObj = JSONObject()
                val sysPartsArray = JSONArray()
                val sysPartObj = JSONObject()
                sysPartObj.put("text", systemInstruction)
                sysPartsArray.put(sysPartObj)
                sysInstObj.put("parts", sysPartsArray)
                root.put("systemInstruction", sysInstObj)
            }

            val requestBody = root.toString().toRequestBody("application/json".toMediaType())
            val request = Request.Builder()
                .url(ENDPOINT)
                .post(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    val errBody = response.body?.string() ?: ""
                    return@withContext "Error: API Request failed (code ${response.code}).\n$errBody"
                }
                val bodyText = response.body?.string() ?: return@withContext "Error: Empty response body"
                val jsonResponse = JSONObject(bodyText)
                val candidates = jsonResponse.getJSONArray("candidates")
                val firstCandidate = candidates.getJSONObject(0)
                val responseContent = firstCandidate.getJSONObject("content")
                val responseParts = responseContent.getJSONArray("parts")
                responseParts.getJSONObject(0).getString("text")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            "Error: ${e.localizedMessage ?: "Unknown network error. Check your connection."}"
        }
    }

    // Generate Interactive Quiz
    suspend fun generateQuiz(topic: String): List<QuizQuestion> = withContext(Dispatchers.IO) {
        val systemInstruction = "You are an expert Bible Scholar. Generate exactly 5 challenging multiple-choice Bible quiz questions on the specified topic/book. You must respond ONLY with a clean, unformatted JSON array that complies exactly with the requested schema. No markdown formatting blocks or HTML tags."
        
        val prompt = """
            Generate a JSON array of 5 multiple choice questions on "$topic".
            Keep questions deeply educational and faith-oriented.
            The JSON array must contain objects with exactly this keys:
            - "question": string
            - "options": array of exactly 4 strings
            - "correctAnswerIndex": integer (0 to 3)
            - "explanation": string detailing the biblical verses and context of this answer.
            
            Return ONLY the valid JSON array and nothing else.
        """.trimIndent()

        val rawResponse = generateContent(prompt, systemInstruction)
        
        try {
            // strip potential markdown wrapper code formatting if the model still returns it
            var cleanJson = rawResponse.trim()
            if (cleanJson.startsWith("```json")) {
                cleanJson = cleanJson.substringAfter("```json")
            }
            if (cleanJson.startsWith("```")) {
                cleanJson = cleanJson.substringAfter("```")
            }
            if (cleanJson.endsWith("```")) {
                cleanJson = cleanJson.substringBeforeLast("```")
            }
            cleanJson = cleanJson.trim()

            val array = JSONArray(cleanJson)
            val list = mutableListOf<QuizQuestion>()
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                val question = obj.getString("question")
                
                val optsArray = obj.getJSONArray("options")
                val options = List(optsArray.length()) { idx -> optsArray.getString(idx) }
                
                val correctIndex = obj.getInt("correctAnswerIndex")
                val explanation = obj.getString("explanation")
                list.add(QuizQuestion(question, options, correctIndex, explanation))
            }
            list
        } catch (e: Exception) {
            e.printStackTrace()
            getMockQuiz(topic) // Return curated fallback quiz if JSON parsing or connection failed
        }
    }

    // High quality local fallbacks if offline or key is missing
    fun getMockQuiz(topic: String): List<QuizQuestion> {
        return listOf(
            QuizQuestion(
                question = "Who is recognized as the father of faith in the Old Testament?",
                options = listOf("Moses", "Abraham", "Noah", "David"),
                correctAnswerIndex = 1,
                explanation = "In Genesis 12 and Romans 4, Abraham is referred to as the father of all who believe due to his profound trust in God's covenants."
            ),
            QuizQuestion(
                question = "What is the primary spiritual focus of Romans Chapter 8?",
                options = listOf("The Law of Moses", "The lineage of David", "Life in the Holy Spirit and freedom of believers", "The reconstruction of Jerusalem"),
                correctAnswerIndex = 2,
                explanation = "Romans 8 details walking in the Spirit, our adoption as children of God, and the truth that nothing can separate us from the love of Christ."
            ),
            QuizQuestion(
                question = "Which chapter of John's Gospel contains the famous declaration: 'For God so loved the world...'?",
                options = listOf("John 1", "John 3", "John 14", "John 17"),
                correctAnswerIndex = 1,
                explanation = "John 3:16 is spoken during Jesus' spiritual discourse with Nicodemus concerning being born again."
            ),
            QuizQuestion(
                question = "In the Sermon on the Mount, what does Jesus call His believers to be in relationship to the world?",
                options = listOf("The rulers and authorities", "The salt of the earth and the light of the world", "The hermits in solitary worship", "The soldiers of the temple"),
                correctAnswerIndex = 1,
                explanation = "In Matthew 5:13-14, Jesus commands believers to be salt and light, preserving righteousness and illuminating truth."
            ),
            QuizQuestion(
                question = "Which psalm is universally known for the opening line: 'The LORD is my shepherd; I shall not want'?",
                options = listOf("Psalm 1", "Psalm 23", "Psalm 91", "Psalm 121"),
                correctAnswerIndex = 1,
                explanation = "Psalm 23, written by David, describes the pastoral guidance, comfort, and abundant provision of the Lord as a Shepherd."
            )
        )
    }

    fun amplifyVerseText(text: String): String {
        var amplified = text
        val replacements = mapOf(
            "God" to "God [Elohim, Creator]",
            "LORD" to "LORD [Yahweh, Sovereign]",
            "Jesus" to "Jesus [the Messiah, Savior]",
            "Christ" to "Christ [the Anointed One]",
            "faith" to "faith [complete trust and confidence]",
            "grace" to "grace [unmerited favor and spiritual blessing]",
            "spirit" to "spirit [the breath of life, heart]",
            "Spirit" to "Spirit [Holy Spirit]",
            "righteousness" to "righteousness [right standing with God]",
            "love" to "love [unconditional, selfless agape]",
            "peace" to "peace [inner calm and spiritual well-being]",
            "salvation" to "salvation [deliverance, restoration]"
        )
        for ((key, value) in replacements) {
            amplified = amplified.replace(" $key ", " $value ", ignoreCase = true)
            amplified = amplified.replace(" $key,", " $value,", ignoreCase = true)
            amplified = amplified.replace(" $key.", " $value.", ignoreCase = true)
        }
        return amplified
    }

    suspend fun fetchChapterFromApi(bookName: String, chapterNumber: Int): List<com.example.database.BibleVerse>? = withContext(Dispatchers.IO) {
        try {
            val cleanBook = bookName.trim().replace(" ", "+")
            
            // 1. Fetch KJV
            val urlKjv = "https://bible-api.com/$cleanBook+$chapterNumber?translation=kjv"
            val reqKjv = Request.Builder().url(urlKjv).build()
            val textKjv = try {
                client.newCall(reqKjv).execute().use { response ->
                    if (response.isSuccessful) response.body?.string() else null
                }
            } catch (e: Exception) {
                null
            }
            
            // 2. Fetch WEB
            val urlWeb = "https://bible-api.com/$cleanBook+$chapterNumber?translation=web"
            val reqWeb = Request.Builder().url(urlWeb).build()
            val textWeb = try {
                client.newCall(reqWeb).execute().use { response ->
                    if (response.isSuccessful) response.body?.string() else null
                }
            } catch (e: Exception) {
                null
            }

            if (textKjv == null && textWeb == null) {
                return@withContext null
            }

            val kjvJson = if (textKjv != null) JSONObject(textKjv) else null
            val webJson = if (textWeb != null) JSONObject(textWeb) else null

            val kjvVersesArray = kjvJson?.optJSONArray("verses")
            val webVersesArray = webJson?.optJSONArray("verses")

            val finalVerses = mutableListOf<com.example.database.BibleVerse>()
            
            val count = maxOf(kjvVersesArray?.length() ?: 0, webVersesArray?.length() ?: 0)
            if (count == 0) return@withContext null

            for (i in 0 until count) {
                val kjvItem = kjvVersesArray?.optJSONObject(i)
                val webItem = webVersesArray?.optJSONObject(i)

                val vNum = if (kjvItem != null) kjvItem.optInt("verse") else (webItem?.optInt("verse") ?: (i + 1))
                
                var kjvText = kjvItem?.optString("text")?.trim() ?: ""
                var webText = webItem?.optString("text")?.trim() ?: ""

                if (kjvText.isEmpty() && webText.isNotEmpty()) {
                    kjvText = webText
                }
                if (webText.isEmpty() && kjvText.isNotEmpty()) {
                    webText = kjvText
                }

                val ampText = amplifyVerseText(webText)

                finalVerses.add(
                    com.example.database.BibleVerse(
                        number = vNum,
                        kjvText = kjvText,
                        webText = webText,
                        ampText = ampText
                    )
                )
            }
            finalVerses
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun fetchHymnFromGemini(hymnQuery: String): com.example.database.Hymn? = withContext(Dispatchers.IO) {
        val systemInstruction = "You are an expert hymnal historian and linguist. You retrieve authentic, complete lyrics (at least 3-5 verses) of any requested Christian hymn, with high-quality traditional translations in Yoruba, Igbo, and Hausa. Respond ONLY with a clean, raw JSON object. No markdown block formatting."
        val prompt = """
            Search and retrieve complete hymn details for "$hymnQuery".
            Output must be a single JSON object with EXACTLY these keys:
            - "title": string (The full official English title of this hymn)
            - "englishTitle": string (English title)
            - "category": string (One of: "Worship", "Praise", "Thanksgiving", "Faith", "Prayer", "Christmas", "Easter", "Communion")
            - "lyricsEnglish": string (Complete lyrics in English, all verses, separated by double newlines)
            - "lyricsYoruba": string (Accurate, complete traditional Yoruba translation, all verses)
            - "lyricsIgbo": string (Accurate, complete traditional Igbo translation, all verses)
            - "lyricsHausa": string (Accurate, complete traditional Hausa translation, all verses)
            - "hymnNumber": integer (An appropriate hymn number, or 100+ random)
            
            Return ONLY the valid raw JSON object. Do not truncate verses. Give the complete lyrics (at least 3-4 verses) in all 4 languages.
        """.trimIndent()
        
        val rawResponse = generateContent(prompt, systemInstruction)
        try {
            var cleanJson = rawResponse.trim()
            if (cleanJson.startsWith("```json")) {
                cleanJson = cleanJson.substringAfter("```json")
            }
            if (cleanJson.startsWith("```")) {
                cleanJson = cleanJson.substringAfter("```")
            }
            if (cleanJson.endsWith("```")) {
                cleanJson = cleanJson.substringBeforeLast("```")
            }
            cleanJson = cleanJson.trim()
            
            val obj = JSONObject(cleanJson)
            com.example.database.Hymn(
                id = (100..1000).random(),
                title = obj.getString("title"),
                englishTitle = obj.getString("englishTitle"),
                category = obj.optString("category", "Worship"),
                lyricsEnglish = obj.getString("lyricsEnglish"),
                lyricsYoruba = obj.getString("lyricsYoruba"),
                lyricsIgbo = obj.getString("lyricsIgbo"),
                lyricsHausa = obj.getString("lyricsHausa"),
                hymnNumber = obj.optInt("hymnNumber", (101..999).random())
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun fetchChapterFromGemini(bookName: String, chapterNumber: Int): List<com.example.database.BibleVerse>? = withContext(Dispatchers.IO) {
        val systemInstruction = "You are an expert Bible archivist. You deliver accurate full chapters of the Bible as JSON. Do not include any intro, markdown formatting wrapper, or HTML. Return ONLY a valid JSON array of objects representing the verses."
        val prompt = """
            Provide all matching verses for the book "$bookName" Chapter $chapterNumber.
            Format the response as a strict, raw JSON array of objects, where each object has exactly these keys:
            - "number": integer (verse number)
            - "kjvText": string (the exact standard King James Version text of this verse)
            - "webText": string (the World English Bible translation of this verse)
            - "ampText": string (the Amplified Bible style translation, including bracketed clarifications of Greek/Hebrew terms)
            
            Return the complete chapter from verse 1 to the end. Do not truncate! Return ONLY the raw JSON array.
        """.trimIndent()

        val rawResponse = generateContent(prompt, systemInstruction)
        try {
            var cleanJson = rawResponse.trim()
            if (cleanJson.startsWith("```json")) {
                cleanJson = cleanJson.substringAfter("```json")
            }
            if (cleanJson.startsWith("```")) {
                cleanJson = cleanJson.substringAfter("```")
            }
            if (cleanJson.endsWith("```")) {
                cleanJson = cleanJson.substringBeforeLast("```")
            }
            cleanJson = cleanJson.trim()

            val array = JSONArray(cleanJson)
            val list = mutableListOf<com.example.database.BibleVerse>()
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                val number = obj.getInt("number")
                val kjvText = obj.getString("kjvText")
                val webText = obj.getString("webText")
                val ampText = obj.optString("ampText", obj.getString("webText"))
                list.add(com.example.database.BibleVerse(number, kjvText, webText, ampText))
            }
            list
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
