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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.database.PrayerRequestEntity
import com.example.viewmodel.BibleViewModel

data class ForumMessage(
    val author: String,
    val message: String,
    val timestamp: String,
    val role: String = "Believer"
)

@Composable
fun CommunityScreen(viewModel: BibleViewModel) {
    var activeTab by remember { mutableStateOf("PRAYERS") } // "PRAYERS", "DISCUSSIONS"
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("community_screen_container")
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Header Section
        Text(
            text = "Christian Community",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        )
        Text(
            text = "Share burdens in prayer, explore theology, and fellowship with global believers.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.height(12.dp))

        // Dual tabs
        TabRow(
            selectedTabIndex = if (activeTab == "PRAYERS") 0 else 1,
            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .height(48.dp),
            indicator = { /* transparent indicator */ }
        ) {
            Tab(
                selected = activeTab == "PRAYERS",
                onClick = {
                    activeTab = "PRAYERS"
                    focusManager.clearFocus()
                },
                modifier = Modifier.testTag("community_tab_prayers")
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Favorite,
                        contentDescription = null,
                        tint = if (activeTab == "PRAYERS") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        "Prayer Requests",
                        fontWeight = if (activeTab == "PRAYERS") FontWeight.Bold else FontWeight.Normal,
                        color = if (activeTab == "PRAYERS") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }

            Tab(
                selected = activeTab == "DISCUSSIONS",
                onClick = {
                    activeTab = "DISCUSSIONS"
                    focusManager.clearFocus()
                },
                modifier = Modifier.testTag("community_tab_discussions")
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Groups,
                        contentDescription = null,
                        tint = if (activeTab == "DISCUSSIONS") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        "Study Forums",
                        fontWeight = if (activeTab == "DISCUSSIONS") FontWeight.Bold else FontWeight.Normal,
                        color = if (activeTab == "DISCUSSIONS") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            AnimatedContent(
                targetState = activeTab,
                transitionSpec = {
                    fadeIn() togetherWith fadeOut()
                },
                label = "community_sub"
            ) { screen ->
                when (screen) {
                    "PRAYERS" -> PrayerWallView(viewModel)
                    "DISCUSSIONS" -> DiscussionForumView(viewModel)
                }
            }
        }
    }
}

// 1. PRAYER WALL COMPOSABLE
@Composable
fun PrayerWallView(viewModel: BibleViewModel) {
    val prayersList by viewModel.prayerRequests.collectAsState()
    var openAddDialog by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    Box(modifier = Modifier.fillMaxSize()) {
        if (prayersList.isEmpty()) {
            // Baseline starting prayers loaded if none in database
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Default.Hearing,
                    contentDescription = null,
                    modifier = Modifier.size(56.dp),
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Your Prayer Circle", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(
                    "Be the first to share a prayer request or intercession need below.",
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    modifier = Modifier.padding(horizontal = 32.dp)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(prayersList) { prayer ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("prayer_card_${prayer.id}"),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        border = CardDefaults.outlinedCardBorder(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = prayer.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )

                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                                    contentColor = MaterialTheme.colorScheme.primary
                                ) {
                                    Text(
                                        text = prayer.author,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = prayer.description,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Amen trigger Row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.People,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (prayer.amenCount == 1) "1 believer joined" else "${prayer.amenCount} believers joined in agreement",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                    )
                                }

                                Button(
                                    onClick = { viewModel.amenPrayer(prayer) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (prayer.hasAmened) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                                        contentColor = if (prayer.hasAmened) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.primary
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                                    modifier = Modifier.height(34.dp).testTag("amen_btn_${prayer.id}")
                                ) {
                                    Icon(
                                        Icons.Default.VolunteerActivism,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (prayer.hasAmened) "Amen!" else "Say Amen",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Floating Action Button to toggle share dialog
        ExtendedFloatingActionButton(
            onClick = { openAddDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .testTag("add_prayer_fab"),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add")
            Spacer(modifier = Modifier.width(6.dp))
            Text("Request Prayer", fontWeight = FontWeight.Bold)
        }

        // Add prayer sheet dialog
        if (openAddDialog) {
            AlertDialog(
                onDismissRequest = { openAddDialog = false },
                title = { Text("Share Prayer Need", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            "Your request will be made available to believers globally. Let us stand with you in faith.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )

                        val titleInput by viewModel.pTitle.collectAsState()
                        val descInput by viewModel.pDesc.collectAsState()
                        val authInput by viewModel.pAuthor.collectAsState()

                        OutlinedTextField(
                            value = titleInput,
                            onValueChange = { viewModel.pTitle.value = it },
                            label = { Text("Title of focus") },
                            placeholder = { Text("E.g., Complete Healing, Wisdom for exams") },
                            modifier = Modifier.fillMaxWidth().testTag("add_prayer_title"),
                            shape = RoundedCornerShape(8.dp),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = descInput,
                            onValueChange = { viewModel.pDesc.value = it },
                            label = { Text("Elaborate request details") },
                            placeholder = { Text("Describe the situation so believers know how to agree in faith...") },
                            modifier = Modifier.fillMaxWidth().height(100.dp).testTag("add_prayer_desc"),
                            shape = RoundedCornerShape(8.dp)
                        )

                        OutlinedTextField(
                            value = authInput,
                            onValueChange = { viewModel.pAuthor.value = it },
                            label = { Text("Your name (optional)") },
                            placeholder = { Text("E.g., Brother John, Anonymous") },
                            modifier = Modifier.fillMaxWidth().testTag("add_prayer_author"),
                            shape = RoundedCornerShape(8.dp),
                            singleLine = true
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.submitPrayerRequest()
                            openAddDialog = false
                            focusManager.clearFocus()
                        },
                        modifier = Modifier.testTag("submit_prayer_request_btn")
                    ) {
                        Text("Post to Prayer Wall", fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { openAddDialog = false }) {
                        Text("Cancel")
                    }
                },
                shape = RoundedCornerShape(16.dp)
            )
        }
    }
}

// 2. STUDY GROUPS DISCUSSIONS VIEWS
@Composable
fun DiscussionForumView(viewModel: BibleViewModel) {
    var activeGroupTopic by remember { mutableStateOf("Daily Discipleship") }
    var userInputMsg by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    val groups = listOf("Daily Discipleship", "Sermon Outline prep", "Tribal Hymn preservation")

    // In-memory feed for discussions
    val discMockFeed = remember {
        mutableStateMapOf(
            "Daily Discipleship" to mutableListOf(
                ForumMessage("Sister Sarah", "I am studying Romans 8 today, life in the Holy Spirit is so freeing! Rejoice indeed, there is no condemnation in Christ Jesus.", "2 hours ago"),
                ForumMessage("Pastor Samuel", "Excellent Sarah. Walking in the Spirit is key to victory. May we all put off the old self and abide in grace.", "1 hour ago", "Pastor"),
                ForumMessage("Emmanuel", "Amen! 'If God be for us, who can be against us?' holds so true.", "20 mins ago")
            ),
            "Sermon Outline prep" to mutableListOf(
                ForumMessage("Evangelist Deborah", "Preparing on Psalm 23 for Sunday. I plan to highlight: 1) The Shepherd's Presence, 2) The Shepherd's Provision, 3) The Shepherd's Promise.", "1 day ago", "Preacher"),
                ForumMessage("Pastor Peter", "Deborah, beautifully formulated! Use John 10 (Good Shepherd) as a supplementary parallel verses hook, it will add immense depth.", "12 hours ago", "Moderator")
            ),
            "Tribal Hymn preservation" to mutableListOf(
                ForumMessage("Grace Onyeka", "The Igbo translations of Amazing Grace (Amara Ebube) captures the soulful heart in a powerful rhythmic way. Our tribes are blessed!", "3 days ago"),
                ForumMessage("Tunde Yusuf", "True! Singing Yoruba classic hymns in morning family prayers builds a massive devotional bridge for our youngsters! Let us keep the flame alive.", "2 days ago")
            )
        )
    }

    val currentTopicMessages = discMockFeed[activeGroupTopic] ?: remember { mutableListOf() }

    Column(modifier = Modifier.fillMaxSize()) {
        // Group Select Chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
        ) {
            items(groups) { group ->
                val isSelected = activeGroupTopic == group
                FilterChip(
                    selected = isSelected,
                    onClick = {
                        activeGroupTopic = group
                        focusManager.clearFocus()
                    },
                    label = { Text(group, fontSize = 12.sp) },
                    shape = RoundedCornerShape(12.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.secondary,
                        selectedLabelColor = MaterialTheme.colorScheme.onSecondary
                    ),
                    modifier = Modifier.testTag("group_chip_$group")
                )
            }
        }

        // Active chat layout
        Surface(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.03f),
            border = CardDefaults.outlinedCardBorder()
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                // Topic title
                Text(
                    text = "Topic Discussion Room: $activeGroupTopic",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                LazyColumn(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(currentTopicMessages) { post ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = post.author,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        if (post.role != "Believer") {
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Surface(
                                                color = MaterialTheme.colorScheme.tertiary,
                                                contentColor = MaterialTheme.colorScheme.onTertiary,
                                                shape = RoundedCornerShape(4.dp)
                                            ) {
                                                Text(
                                                    text = post.role,
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                                )
                                            }
                                        }
                                    }
                                    Text(
                                        text = post.timestamp,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = post.message,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Input Bar to join discussion
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = userInputMsg,
                        onValueChange = { userInputMsg = it },
                        placeholder = { Text("Fellowship and share your thoughts...") },
                        modifier = Modifier.weight(1f).testTag("discussion_comment_field"),
                        shape = RoundedCornerShape(20.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            focusedContainerColor = MaterialTheme.colorScheme.surface
                        )
                    )

                    IconButton(
                        onClick = {
                            if (userInputMsg.isNotBlank()) {
                                currentTopicMessages.add(
                                    ForumMessage("You (Disciple)", userInputMsg.trim(), "Just now")
                                )
                                userInputMsg = ""
                                focusManager.clearFocus()
                            }
                        },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.secondary)
                            .size(36.dp)
                            .testTag("submit_discussion_btn"),
                        colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.onSecondary)
                    ) {
                        Icon(Icons.Default.ArrowUpward, contentDescription = "Submit Comment", modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}
