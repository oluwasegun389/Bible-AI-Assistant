package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.database.Hymn
import com.example.viewmodel.BibleViewModel
import kotlinx.coroutines.delay

@Composable
fun HymnScreen(viewModel: BibleViewModel) {
    val selectedHymn by viewModel.selectedHymn.collectAsState()

    AnimatedContent(
        targetState = selectedHymn,
        transitionSpec = {
            slideInHorizontally(initialOffsetX = { it }) + fadeIn() togetherWith
            slideOutHorizontally(targetOffsetX = { -it }) + fadeOut()
        },
        label = "hymn_deck"
    ) { hymn ->
        if (hymn == null) {
            HymnListView(viewModel)
        } else {
            HymnDetailView(hymn, viewModel)
        }
    }
}

@Composable
fun HymnListView(viewModel: BibleViewModel) {
    val searchQuery by viewModel.hymnQuery.collectAsState()
    val selectedCat by viewModel.hymnCategory.collectAsState()
    val listHymns by viewModel.filteredHymns.collectAsState(emptyList())
    val favoriteHymns by viewModel.favoriteHymns.collectAsState()

    val categories = listOf("All", "Worship", "Praise", "Thanksgiving", "Prayer", "Christmas")

    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(16.dp))

        // Screen intro
        Text(
            text = "Hymnal & Worship Companion",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        )
        Text(
            text = "Sing praises and worship offline across tribal multi-languages.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.height(12.dp))

        // Quick Search Box
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.hymnQuery.value = it },
            placeholder = { Text("Search by lyrics, number, or title...") },
            modifier = Modifier.fillMaxWidth().testTag("hymn_search_field"),
            shape = RoundedCornerShape(12.dp),
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                if (searchQuery.isNotBlank()) {
                    IconButton(onClick = { viewModel.hymnQuery.value = "" }) {
                        Icon(Icons.Default.Close, contentDescription = "Clear")
                    }
                }
            },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Category Chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(categories) { cat ->
                val isSel = selectedCat == cat
                FilterChip(
                    selected = isSel,
                    onClick = { viewModel.hymnCategory.value = cat },
                    label = { Text(cat) },
                    shape = RoundedCornerShape(12.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier.testTag("category_chip_$cat")
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        val isSearchingOnline by viewModel.isHymnSearchLoading.collectAsState()

        if (searchQuery.isNotBlank() || isSearchingOnline) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
                    .clickable(enabled = !isSearchingOnline) { viewModel.searchHymnOnline() }
                    .testTag("search_hymn_online_card"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
                ),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "AI Lookup",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isSearchingOnline) "Looking up complete hymnal online..." else "Look up \"$searchQuery\" with AI",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Find/complete any hymn with full verses and multi-translations",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                    if (isSearchingOnline) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Search",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
        }

        // Hymns List view
        if (listHymns.isEmpty()) {
            Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.MusicOff,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.outline
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "No hymns match your filters.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(listHymns) { hymn ->
                    val isFav = favoriteHymns.any { it.hymnId == hymn.id }
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.selectHymn(hymn) }
                            .testTag("hymn_card_${hymn.id}"),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.03f)
                        ),
                        border = CardDefaults.outlinedCardBorder()
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Circular Badge showing number
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(36.dp),
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = hymn.hymnNumber.toString(),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            // Titles column
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = hymn.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Category: ${hymn.category} • Traditional translation available",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            }

                            // Favorite Icon
                            IconButton(onClick = { viewModel.toggleHymnFavorite(hymn.id, "English") }) {
                                Icon(
                                    imageVector = if (isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = "Favorite",
                                    tint = if (isFav) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HymnDetailView(hymn: Hymn, viewModel: BibleViewModel) {
    val activeLang by viewModel.activeHymnLang.collectAsState()
    val isPlaying by viewModel.isHymnPlaying.collectAsState()
    val isFavorite by viewModel.isHymnFavorite.collectAsState()

    val languages = listOf("English", "Yoruba", "Igbo", "Hausa")

    // Local animated progression state for visual wave simulation
    var playPulse by remember { mutableStateOf(1f) }

    // Wave animations
    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            while (true) {
                playPulse = if (playPulse == 1f) 1.5f else 1f
                delay(600)
            }
        } else {
            playPulse = 1f
        }
    }

    val activeLyrics = when (activeLang) {
        "Yoruba" -> hymn.lyricsYoruba
        "Igbo" -> hymn.lyricsIgbo
        "Hausa" -> hymn.lyricsHausa
        else -> hymn.lyricsEnglish
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(16.dp))

        // Custom Top Bar for detail view
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = { viewModel.selectHymn(null) },
                modifier = Modifier.testTag("hymn_back_btn")
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }

            Text(
                text = "Hymn #${hymn.hymnNumber}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            // Favoriting Action
            IconButton(onClick = { viewModel.toggleHymnFavorite(hymn.id, activeLang) }) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (isFavorite) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Title and Category Header
        Text(
            text = hymn.title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        )
        Text(
            text = "Category: ${hymn.category} • English: \"${hymn.englishTitle}\"",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Language Select Tab
        TabRow(
            selectedTabIndex = languages.indexOf(activeLang).coerceAtLeast(0),
            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .height(40.dp),
            indicator = { /* transparent indicators default to clean custom selected states */ }
        ) {
            languages.forEach { lang ->
                Tab(
                    selected = activeLang == lang,
                    onClick = { viewModel.activeHymnLang.value = lang },
                    modifier = Modifier.testTag("hymn_lang_tab_$lang")
                ) {
                    Text(
                        lang,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = if (activeLang == lang) FontWeight.Bold else FontWeight.Normal,
                        color = if (activeLang == lang) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Scrolling Lyrics Area
        Surface(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 1.dp
        ) {
            Box(modifier = Modifier.fillMaxSize().padding(24.dp)) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        Text(
                            text = activeLyrics,
                            style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 28.sp),
                            textAlign = TextAlign.Center,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                            modifier = Modifier.padding(bottom = 24.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // MIDI / Playback Simulated Bar
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.primaryContainer,
            tonalElevation = 6.dp
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Play and Pause controller
                FilledIconButton(
                    onClick = { viewModel.toggleHymnPlayback() },
                    shape = CircleShape,
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier.size(48.dp).testTag("play_instrumental_btn")
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = "Play/Pause Instrumental"
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Simulated Progression Waves or Text indicator
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (isPlaying) "Playing Instrumental Worship..." else "Listen to Instrumental Accompaniment",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "Synthesizer offline MIDI playback",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    )
                }

                // Worship Waves visualizer representation
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier.height(24.dp)
                ) {
                    repeat(4) { blockIdx ->
                        val scaleHeight = when (blockIdx) {
                            0 -> if (isPlaying) 18.dp else 6.dp
                            1 -> if (isPlaying) 24.dp else 4.dp
                            2 -> if (isPlaying) 14.dp else 10.dp
                            else -> if (isPlaying) 20.dp else 8.dp
                        }
                        Box(
                            modifier = Modifier
                                .width(4.dp)
                                .height(scaleHeight)
                                .background(MaterialTheme.colorScheme.primary)
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}
