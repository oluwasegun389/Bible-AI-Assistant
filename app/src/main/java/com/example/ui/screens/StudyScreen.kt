package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.database.BibleBook
import com.example.database.BibleChapter
import com.example.database.BibleData
import com.example.database.BibleVerse
import com.example.viewmodel.BibleViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StudyScreen(viewModel: BibleViewModel) {
    val currentBook by viewModel.currentBook.collectAsState()
    val currentChapter by viewModel.currentChapter.collectAsState()
    val activeVersion by viewModel.activeVersion.collectAsState()
    val activeDisplayMode by viewModel.activeDisplayMode.collectAsState()
    val selectedVerse by viewModel.selectedVerse.collectAsState()
    
    val bookmarksList by viewModel.bookmarks.collectAsState()
    val notesList by viewModel.notes.collectAsState()
    val isChapterLoading by viewModel.isChapterLoading.collectAsState()

    var showBookDropdown by remember { mutableStateOf(false) }
    var showChapterDropdown by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("study_screen_container")
    ) {
        // Welcome and Header Area
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Holy Scripture",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        )
        Text(
            text = "Read, compare, and study with AI discipleship guidance.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.height(12.dp))

        // Book & Chapter Selectors Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Book Selector Button
            Box(modifier = Modifier.weight(1f)) {
                Button(
                    onClick = { showBookDropdown = true },
                    modifier = Modifier.fillMaxWidth().testTag("book_selector_btn"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Book, contentDescription = "Book")
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = currentBook.name, fontWeight = FontWeight.SemiBold)
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
                }

                if (showBookDropdown) {
                    var searchQuery by remember { mutableStateOf("") }
                    AlertDialog(
                        onDismissRequest = { showBookDropdown = false },
                        title = {
                            Column {
                                Text(
                                    "Select Bible Book",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    "Browse or search across all 66 books",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            }
                        },
                        text = {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = searchQuery,
                                    onValueChange = { searchQuery = it },
                                    placeholder = { Text("Type to search (e.g. Gen, Rom)...") },
                                    modifier = Modifier.fillMaxWidth().testTag("book_search_input"),
                                    shape = RoundedCornerShape(12.dp),
                                    leadingIcon = { Icon(Icons.Default.Search, null) },
                                    trailingIcon = {
                                        if (searchQuery.isNotEmpty()) {
                                            IconButton(onClick = { searchQuery = "" }) {
                                                Icon(Icons.Default.Close, null)
                                            }
                                        }
                                    },
                                    singleLine = true
                                )
                                
                                val filteredBooks = BibleData.books.mapIndexed { index, book -> index to book }
                                    .filter { it.second.name.contains(searchQuery, ignoreCase = true) }
                                
                                if (filteredBooks.isEmpty()) {
                                    Box(
                                        modifier = Modifier.fillMaxWidth().height(200.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("No books found matching \"$searchQuery\"", style = MaterialTheme.typography.bodySmall)
                                    }
                                } else {
                                    val otBooks = filteredBooks.filter { it.first < 39 }
                                    val ntBooks = filteredBooks.filter { it.first >= 39 }
                                    
                                    LazyColumn(
                                        modifier = Modifier.fillMaxWidth().height(280.dp),
                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        if (otBooks.isNotEmpty()) {
                                            item { 
                                                Text(
                                                    "Old Testament",
                                                    fontWeight = FontWeight.Bold,
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = MaterialTheme.colorScheme.secondary,
                                                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                                                ) 
                                            }
                                            items(otBooks) { (idx, book) ->
                                                DropdownMenuItem(
                                                    text = { Text(book.name, fontWeight = FontWeight.Medium) },
                                                    onClick = {
                                                        viewModel.selectBook(idx)
                                                        showBookDropdown = false
                                                    },
                                                    leadingIcon = { Icon(Icons.Default.MenuBook, null, tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)) }
                                                )
                                            }
                                        }
                                        
                                        if (ntBooks.isNotEmpty()) {
                                            item { 
                                                Text(
                                                    "New Testament",
                                                    fontWeight = FontWeight.Bold,
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = MaterialTheme.colorScheme.secondary,
                                                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                                                ) 
                                            }
                                            items(ntBooks) { (idx, book) ->
                                                DropdownMenuItem(
                                                    text = { Text(book.name, fontWeight = FontWeight.Medium) },
                                                    onClick = {
                                                        viewModel.selectBook(idx)
                                                        showBookDropdown = false
                                                    },
                                                    leadingIcon = { Icon(Icons.Default.MenuBook, null, tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)) }
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        },
                        confirmButton = {
                            TextButton(onClick = { showBookDropdown = false }) {
                                Text("Close")
                            }
                        },
                        shape = RoundedCornerShape(20.dp)
                    )
                }
            }

            // Chapter Selector Button
            Box(modifier = Modifier.weight(1f)) {
                Button(
                    onClick = { showChapterDropdown = true },
                    modifier = Modifier.fillMaxWidth().testTag("chapter_selector_btn"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = "Chapter ${currentChapter.chapterNumber}", fontWeight = FontWeight.SemiBold)
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
                }

                DropdownMenu(
                    expanded = showChapterDropdown,
                    onDismissRequest = { showChapterDropdown = false },
                    modifier = Modifier.testTag("chapter_dropdown")
                ) {
                    currentBook.chapters.forEachIndexed { idx, ch ->
                        DropdownMenuItem(
                            text = { Text("Chapter ${ch.chapterNumber}") },
                            onClick = {
                                viewModel.selectChapter(idx)
                                showChapterDropdown = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Version Selector and Display Mode Rows
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            item {
                Text(
                    text = "Version: ",
                    modifier = Modifier.padding(end = 4.dp),
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                )
            }
            items(listOf("KJV", "WEB", "AMP")) { version ->
                val isSelected = activeVersion == version
                FilterChip(
                    selected = isSelected,
                    onClick = { viewModel.setVersion(version) },
                    label = { Text(version) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier.testTag("version_chip_$version")
                )
            }
            item {
                Spacer(modifier = Modifier.width(16.dp))
                VerticalDivider(modifier = Modifier.height(24.dp))
                Spacer(modifier = Modifier.width(16.dp))
            }
            item {
                Text(
                    text = "View: ",
                    modifier = Modifier.padding(end = 4.dp),
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                )
            }
            items(listOf("SINGLE", "PARALLEL", "COMPARISON")) { mode ->
                val isSelected = activeDisplayMode == mode
                FilterChip(
                    selected = isSelected,
                    onClick = { viewModel.setDisplayMode(mode) },
                    label = { Text(mode) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier.testTag("view_chip_$mode")
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Reading Content Card
        Surface(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 1.dp
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Intro text
                item {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "${currentBook.name} • Chapter ${currentChapter.chapterNumber}",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = currentBook.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        
                        if (isChapterLoading) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(16.dp),
                                        strokeWidth = 2.dp,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = "Synchronizing full chapter verses...",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                }

                // Verses List depending on Display mode
                items(currentChapter.verses) { verse ->
                    val ref = "${currentBook.name} ${currentChapter.chapterNumber}:${verse.number}"
                    val isBookmarked = bookmarksList.any { it.verseReference == ref }
                    val hasNote = notesList.any { it.verseReference == ref }
                    val isSelected = selectedVerse == verse

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                                else Color.Transparent
                            )
                            .combinedClickable(
                                onClick = { viewModel.selectVerse(if (isSelected) null else verse) },
                                onLongClick = { viewModel.toggleBookmark(verse) }
                            )
                            .padding(8.dp)
                            .testTag("verse_item_${verse.number}")
                    ) {
                        Row(
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "${verse.number}.",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            )

                            // Render text according to viewing model
                            Column(modifier = Modifier.weight(1f)) {
                                when (activeDisplayMode) {
                                    "SINGLE" -> {
                                        val text = when (activeVersion) {
                                            "KJV" -> verse.kjvText
                                            "WEB" -> verse.webText
                                            else -> verse.ampText
                                        }
                                        Text(
                                            text = text,
                                            style = MaterialTheme.typography.bodyLarge.copy(
                                                fontFamily = FontFamily.Serif,
                                                lineHeight = 28.sp
                                            )
                                        )
                                    }
                                    "PARALLEL" -> {
                                        // Side-by-side or stacked translations
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                                        ) {
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    text = "KJV",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = MaterialTheme.colorScheme.primary,
                                                    fontWeight = FontWeight.Bold
                                                )
                                                Text(
                                                    text = verse.kjvText,
                                                    style = MaterialTheme.typography.bodyMedium.copy(
                                                        fontFamily = FontFamily.Serif,
                                                        lineHeight = 24.sp
                                                    )
                                                )
                                            }
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    text = "WEB",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = MaterialTheme.colorScheme.secondary,
                                                    fontWeight = FontWeight.Bold
                                                )
                                                Text(
                                                    text = verse.webText,
                                                    style = MaterialTheme.typography.bodyMedium.copy(
                                                        fontFamily = FontFamily.Serif,
                                                        lineHeight = 24.sp
                                                    )
                                                )
                                            }
                                        }
                                    }
                                    "COMPARISON" -> {
                                        Column(
                                            verticalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Text(
                                                text = "KJV: " + verse.kjvText,
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontFamily = FontFamily.Serif,
                                                    lineHeight = 22.sp
                                                )
                                            )
                                            Text(
                                                text = "WEB: " + verse.webText,
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontFamily = FontFamily.Serif,
                                                    lineHeight = 22.sp,
                                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                                                )
                                            )
                                            Text(
                                                text = "AMP: " + verse.ampText,
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontFamily = FontFamily.Serif,
                                                    lineHeight = 22.sp,
                                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                                                )
                                            )
                                        }
                                    }
                                }

                                // Interactive Status Badges (Bookmark, Note)
                                Row(
                                    modifier = Modifier.padding(top = 4.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (isBookmarked) {
                                        Icon(
                                            imageVector = Icons.Default.Bookmark,
                                            contentDescription = "Bookmarked",
                                            tint = MaterialTheme.colorScheme.tertiary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Text(
                                            "Bookmarked",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.tertiary
                                        )
                                    }
                                    if (hasNote) {
                                        Icon(
                                            imageVector = Icons.Default.Note,
                                            contentDescription = "Has Study Note",
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Text(
                                            "Personal Note",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Button to Log Complete Chapter Read
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { viewModel.markChapterAsRead() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("mark_read_btn"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Check, contentDescription = "Check")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Record Chapter as Completed Study", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

        // Animated Actions Area when a Verse is selected
        AnimatedVisibility(
            visible = selectedVerse != null,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
        ) {
            val verse = selectedVerse ?: return@AnimatedVisibility
            val ref = "${currentBook.name} ${currentChapter.chapterNumber}:${verse.number}"
            val isBookmarked = bookmarksList.any { it.verseReference == ref }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.primaryContainer,
                tonalElevation = 6.dp
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Verse Actions: $ref",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )

                        IconButton(onClick = { viewModel.selectVerse(null) }) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = MaterialTheme.colorScheme.onPrimaryContainer)
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Toggle Bookmark Button
                        Button(
                            onClick = { viewModel.toggleBookmark(verse) },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isBookmarked) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.surface,
                                contentColor = if (isBookmarked) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.primary
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(
                                if (isBookmarked) Icons.Default.BookmarkRemove else Icons.Default.BookmarkAdd,
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(if (isBookmarked) "Unbookmark" else "Bookmark", fontWeight = FontWeight.SemiBold)
                        }

                        // Add note toggler
                    }

                    // Add Note Input Box
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Write a study note or prayer observation:",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    val noteTitleInput by viewModel.noteTitle.collectAsState()
                    val noteContentInput by viewModel.noteContent.collectAsState()

                    OutlinedTextField(
                        value = noteTitleInput,
                        onValueChange = { viewModel.noteTitle.value = it },
                        placeholder = { Text("Reflection tag (e.g. Life Application, Comfort)") },
                        modifier = Modifier.fillMaxWidth().testTag("note_title_input"),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            focusedContainerColor = MaterialTheme.colorScheme.surface
                        )
                    )

                    OutlinedTextField(
                        value = noteContentInput,
                        onValueChange = { viewModel.noteContent.value = it },
                        placeholder = { Text("What did the Spirit quicken in you today?") },
                        modifier = Modifier.fillMaxWidth().height(80.dp).testTag("note_content_input"),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            focusedContainerColor = MaterialTheme.colorScheme.surface
                        ),
                        maxLines = 3
                    )

                    Button(
                        onClick = {
                            viewModel.addNoteForVerse(verse, noteTitleInput, noteContentInput)
                            focusManager.clearFocus()
                        },
                        modifier = Modifier.align(Alignment.End).testTag("save_note_btn"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            contentColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Text("Save Reflection to Dashboard", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}
