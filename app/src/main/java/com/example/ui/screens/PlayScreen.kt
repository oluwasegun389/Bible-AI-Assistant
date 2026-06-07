@file:OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.database.ActivityLogEntity
import com.example.database.BadgeEntity
import com.example.network.GeminiClient
import com.example.network.QuizQuestion
import com.example.viewmodel.BibleViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

// --- DATA CLASSES FOR MINI GAMES ---

data class ScrambleLevel(
    val id: Int,
    val reference: String,
    val text: String,
    val scrambledWords: List<String>,
    val correctWords: List<String>,
    val clue: String
)

data class MemoryCard(
    val id: Int,
    val content: String,
    val matchId: Int, // Cards with same matchId are pairs
    var isFaceUp: Boolean = false,
    var isMatched: Boolean = false
)

@Composable
fun PlayScreen(viewModel: BibleViewModel) {
    var activeGameMode by rememberSaveable { mutableStateOf<String?>("HOME") } // "HOME", "TRIVIA", "SCRAMBLE", "MEMORY"

    AnimatedContent(
        targetState = activeGameMode,
        transitionSpec = {
            fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(300))
        },
        label = "game_screen_transitions"
    ) { mode ->
        when (mode) {
            "HOME" -> GameDashboardHome(
                onSelectTrivia = { activeGameMode = "TRIVIA" },
                onSelectScramble = { activeGameMode = "SCRAMBLE" },
                onSelectMemory = { activeGameMode = "MEMORY" }
            )
            "TRIVIA" -> TriviaGameMode(viewModel, onBack = { activeGameMode = "HOME" })
            "SCRAMBLE" -> VerseScrambleGameMode(viewModel, onBack = { activeGameMode = "HOME" })
            "MEMORY" -> WisdomMatchingGameMode(viewModel, onBack = { activeGameMode = "HOME" })
            else -> GameDashboardHome(
                onSelectTrivia = { activeGameMode = "TRIVIA" },
                onSelectScramble = { activeGameMode = "SCRAMBLE" },
                onSelectMemory = { activeGameMode = "MEMORY" }
            )
        }
    }
}

// --- 1. GAME DASHBOARD HOME VIEW ---
@Composable
fun GameDashboardHome(
    onSelectTrivia: () -> Unit,
    onSelectScramble: () -> Unit,
    onSelectMemory: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("game_dashboard_container"),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp)
    ) {
        // Hero Header banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("game_hero_card"),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f)
                )
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    // Subtle background Canvas design representation
                    val strokeColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .matchParentSize()
                    ) {
                        drawCircle(
                            color = strokeColor,
                            radius = 120.dp.toPx(),
                            center = androidx.compose.ui.geometry.Offset(size.width - 20.dp.toPx(), 20.dp.toPx())
                        )
                    }

                    Column(
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.primary, CircleShape)
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                "SCRIPTURE PLAYROOM",
                                color = MaterialTheme.colorScheme.onPrimary,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Text(
                            "Strengthen Wisdom Through Play",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Text(
                            "Engage with word challenges, theological trivia, and scripture matching puzzles designed to build a joyful, consistent devotion routine.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            lineHeight = 20.sp
                        )
                    }
                }
            }
        }

        item {
            Text(
                "Select a Game Mode:",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        // Mode 1 Card: Trivia Master
        item {
            GameModeSelectionCard(
                title = "Trivia Quest Master",
                description = "Challenge yourself with interactive multiple-choice questions. Pick standard Biblical doctrines or use AI (Gemini) to generate custom scripture study topics!",
                features = listOf("AI or Offline Mode", "Detailed Commentary", "Earn Achievements"),
                cardColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f),
                borderColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
                icon = Icons.Default.Psychology,
                tag = "btn_play_trivia",
                onClick = onSelectTrivia
            )
        }

        // Mode 2 Card: Verse Scramble
        item {
            GameModeSelectionCard(
                title = "Holy Verse Scramble",
                description = "Arrange scrambled key scripture words in the exact sequential order. Rebuild iconic passages and test your biblical verse memorization skill!",
                features = listOf("Preselected Verses", "Word Re-assembler", "Tactile Taps"),
                cardColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.25f),
                borderColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f),
                icon = Icons.Default.Extension,
                tag = "btn_play_scramble",
                onClick = onSelectScramble
            )
        }

        // Mode 3 Card: Wisdom Flip Card Match
        item {
            GameModeSelectionCard(
                title = "Wisdom Match (Memory Flip)",
                description = "Pair famous Bible figures, heroes, and queens with their matching historical records, stories, and declarations in this beautiful flipped card memory match game.",
                features = listOf("3D Rotating Cards", "Tracks Match Attempts", "Memory Builder"),
                cardColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
                icon = Icons.Default.GridView,
                tag = "btn_play_memory",
                onClick = onSelectMemory
            )
        }
    }
}

@Composable
fun GameModeSelectionCard(
    title: String,
    description: String,
    features: List<String>,
    cardColor: Color,
    borderColor: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tag: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag(tag),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.5.dp, borderColor),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(MaterialTheme.colorScheme.surface, CircleShape)
                        .border(1.dp, borderColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Interactive Playroom",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Short chips of features
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                features.forEach { feature ->
                    Box(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                            .border(0.5.dp, borderColor, RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = feature,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }
    }
}

// --- 2. GAME MODE: TRIVIA QUEST MASTER (QUIZ) ---
@Composable
fun TriviaGameMode(
    viewModel: BibleViewModel,
    onBack: () -> Unit
) {
    val activeQuizList by viewModel.activeQuizList.collectAsState()
    val isQuizLoading by viewModel.isQuizLoading.collectAsState()
    val currentQuizIndex by viewModel.currentQuizIndex.collectAsState()
    val selectedOption by viewModel.selectedOption.collectAsState()
    val quizScore by viewModel.quizScore.collectAsState()
    val showExplanation by viewModel.showQuizExplanation.collectAsState()
    val quizCompleted by viewModel.quizCompleted.collectAsState()
    val answeredCorrectly by viewModel.answeredCorrectly.collectAsState()
    val quizTopic by viewModel.currentQuizTopic.collectAsState()

    var customTopicInput by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    val topics = listOf("Faith", "Grace", "Old Testament", "New Testament Epistles", "Parables of Jesus", "Prophets")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("trivia_screen")
    ) {
        // Game Header bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.testTag("trivia_back_btn")
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back to menu")
            }

            Text(
                text = "Trivia Quest Master",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            // Small indicator for points
            Box(
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.secondaryContainer,
                        RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        Icons.Default.EmojiEvents,
                        contentDescription = "Score",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        "Score: $quizScore",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))

        if (activeQuizList.isEmpty() && !isQuizLoading) {
            // Setup Quiz View
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp)
            ) {
                item {
                    Text(
                        "Select a theology or scripture topic to test your wisdom, or key in your own tailored topic with our AI Bible Study Companion!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }

                item {
                    Text(
                        "Popular Scripture Topics:",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                // Grid of topics
                item {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        topics.forEach { topic ->
                            FilterChip(
                                selected = false,
                                onClick = { viewModel.startQuiz(topic) },
                                label = { Text(topic) },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.testTag("trivia_chip_topic_$topic")
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "WRITE A UNIQUE BIBLE TOPIC:",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                item {
                    OutlinedTextField(
                        value = customTopicInput,
                        onValueChange = { customTopicInput = it },
                        placeholder = { Text("E.g. Pauline Epistles, Books of Moses, Miracles...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("trivia_custom_input"),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                }

                item {
                    Button(
                        onClick = {
                            if (customTopicInput.isNotBlank()) {
                                viewModel.startQuiz(customTopicInput)
                                focusManager.clearFocus()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("trivia_start_custom_btn"),
                        shape = RoundedCornerShape(12.dp),
                        enabled = customTopicInput.isNotBlank()
                    ) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Connect AI & Generate Trivia", fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else if (isQuizLoading) {
            // Loading Mode
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Searching Ancient Scriptures...",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Tailoring 5 rigorous theological questions on \"$quizTopic\"...",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center
                )
            }
        } else if (quizCompleted) {
            // Complete Mode
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.size(120.dp)) {
                    CircularProgressIndicator(
                        progress = { quizScore / 5f },
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        strokeWidth = 10.dp,
                        modifier = Modifier.fillMaxSize()
                    )
                    Text(
                        text = "$quizScore/5",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = when {
                        quizScore == 5 -> "Crown of Wisdom Unlocked!"
                        quizScore >= 3 -> "Good & Faithful Servant!"
                        else -> "Keep Pressing in Scripture!"
                    },
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = when {
                        quizScore == 5 -> "Perfect representation! You have earned the 'Theology Scholar' badge. Go check your Achievements tab!"
                        quizScore >= 3 -> "Wonderful grasp of the holy text. Continue testing yourself daily to establish steadfast doctrine."
                        else -> "The Scriptures are a wellspring of wisdom. Review Biblical chapters and take the challenge again to improve."
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { viewModel.startQuiz(quizTopic) },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .testTag("trivia_retry_btn"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Play Again")
                    }

                    Button(
                        onClick = { viewModel.startQuiz("Faith") }, // Just clear out quiz state by loading another or re-setup
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .testTag("trivia_other_btn"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Other Topic")
                    }
                }
            }
        } else {
            // Live Question Play View
            val questionList = activeQuizList
            val currentQ = questionList.getOrNull(currentQuizIndex)

            if (currentQ != null) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp)
                ) {
                    // Question progress indicator
                    item {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "QUESTION ${currentQuizIndex + 1} OF ${questionList.size}",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Topic: $quizTopic",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )
                        }
                        
                        LinearProgressIndicator(
                            progress = { (currentQuizIndex + 1).toFloat() / questionList.size },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                        )
                    }

                    // Question text Card
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.15f))
                        ) {
                            Text(
                                text = currentQ.question,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(20.dp)
                            )
                        }
                    }

                    // Options list
                    itemsIndexed(currentQ.options) { index, option ->
                        val isSelected = selectedOption == index
                        val isCorrectIndex = index == currentQ.correctAnswerIndex
                        val isAnswered = selectedOption != null

                        val cardColor = when {
                            !isAnswered && isSelected -> MaterialTheme.colorScheme.primaryContainer
                            isAnswered && isCorrectIndex -> Color.Green.copy(alpha = 0.15f)
                            isAnswered && isSelected && !isCorrectIndex -> Color.Red.copy(alpha = 0.15f)
                            else -> MaterialTheme.colorScheme.surface
                        }

                        val strokeColor = when {
                            !isAnswered && isSelected -> MaterialTheme.colorScheme.primary
                            isAnswered && isCorrectIndex -> Color.Green.copy(alpha = 0.7f)
                            isAnswered && isSelected && !isCorrectIndex -> Color.Red.copy(alpha = 0.7f)
                            else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                        }

                        val textColor = when {
                            isAnswered && isCorrectIndex -> Color(0xFF1B5E20)
                            isAnswered && isSelected && !isCorrectIndex -> Color(0xFFB71C1C)
                            else -> MaterialTheme.colorScheme.onSurface
                        }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(enabled = !isAnswered) { viewModel.selectQuizOption(index) }
                                .testTag("trivia_option_$index"),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = cardColor),
                            border = BorderStroke(2.dp, strokeColor)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = option,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = textColor,
                                    fontWeight = if (isSelected || (isAnswered && isCorrectIndex)) FontWeight.Bold else FontWeight.Medium,
                                    modifier = Modifier.weight(1f)
                                )

                                if (isAnswered) {
                                    if (isCorrectIndex) {
                                        Icon(Icons.Default.CheckCircle, contentDescription = "Correct", tint = Color(0xFF2E7D32))
                                    } else if (isSelected) {
                                        Icon(Icons.Default.Cancel, contentDescription = "Incorrect", tint = Color(0xFFC62828))
                                    }
                                }
                            }
                        }
                    }

                    // Explanation Column
                    if (showExplanation) {
                        item {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .testTag("trivia_explanation_card"),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (answeredCorrectly == true) Color.Green.copy(alpha = 0.05f) else Color.Red.copy(alpha = 0.02f)
                                ),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, if (answeredCorrectly == true) Color.Green.copy(alpha = 0.3f) else Color.Red.copy(alpha = 0.1f))
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(24.dp)
                                                .background(
                                                    if (answeredCorrectly == true) Color.Green.copy(alpha = 0.15f) else Color.Red.copy(alpha = 0.1f),
                                                    CircleShape
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = if (answeredCorrectly == true) Icons.Default.Check else Icons.Default.Close,
                                                contentDescription = null,
                                                tint = if (answeredCorrectly == true) Color(0xFF2E7D32) else Color(0xFFC62828),
                                                modifier = Modifier.size(14.dp)
                                            )
                                        }

                                        Text(
                                            text = if (answeredCorrectly == true) "CORRECT ANSWER!" else "INCORRECT",
                                            fontWeight = FontWeight.Bold,
                                            style = MaterialTheme.typography.labelMedium,
                                            color = if (answeredCorrectly == true) Color(0xFF2E7D32) else Color(0xFFC62828)
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = currentQ.explanation,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        lineHeight = 16.sp
                                    )
                                }
                            }
                        }

                        item {
                            Button(
                                onClick = { viewModel.nextQuizQuestion() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .testTag("trivia_next_btn"),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = if (currentQuizIndex == questionList.size -1) "Finish Quiz" else "Next Question",
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(Icons.Default.ArrowForward, contentDescription = null)
                            }
                        }
                    }
                }
            }
        }
    }
}

// --- 3. GAME MODE: HOLY VERSE SCRAMBLE (WORD BUILDING GAME) ---
@Composable
fun VerseScrambleGameMode(
    viewModel: BibleViewModel,
    onBack: () -> Unit
) {
    val levels = remember {
        listOf(
            ScrambleLevel(
                id = 1,
                reference = "John 3:16",
                text = "For God so loved the world that He gave His only begotten Son.",
                scrambledWords = listOf("loved", "world", "Son.", "God", "the", "He", "begotten", "only", "His", "so", "gave", "that", "For").shuffled(),
                correctWords = listOf("For", "God", "so", "loved", "the", "world", "that", "He", "gave", "His", "only", "begotten", "Son."),
                clue = "Nicodemus learns of God's redemptive plan to rescue mankind."
            ),
            ScrambleLevel(
                id = 2,
                reference = "Psalm 23:1",
                text = "The LORD is my shepherd I shall not want.",
                scrambledWords = listOf("LORD", "shepherd", "The", "not", "shall", "is", "my", "want", "I").shuffled(),
                correctWords = listOf("The", "LORD", "is", "my", "shepherd", "I", "shall", "not", "want."),
                clue = "Pastoral declaration of David detailing comfort and sheep safety."
            ),
            ScrambleLevel(
                id = 3,
                reference = "Philippians 4:13",
                text = "I can do all things through Christ who strengthens me.",
                scrambledWords = listOf("things", "do", "strengthens", "all", "through", "Christ", "who", "I", "can", "me.").shuffled(),
                correctWords = listOf("I", "can", "do", "all", "things", "through", "Christ", "who", "strengthens", "me."),
                clue = "Paul's encouraging reminder of spiritual empowerment in suffering."
            ),
            ScrambleLevel(
                id = 4,
                reference = "Proverbs 3:5",
                text = "Trust in the LORD with all your heart.",
                scrambledWords = listOf("heart.", "the", "with", "Trust", "all", "LORD", "your", "in").shuffled(),
                correctWords = listOf("Trust", "in", "the", "LORD", "with", "all", "your", "heart."),
                clue = "Solomonic proverb warning against self-reliance."
            )
        )
    }

    var currentLevelIdx by rememberSaveable { mutableStateOf(0) }
    val level = levels[currentLevelIdx]

    val activeScrambledWords = remember(currentLevelIdx) { mutableStateListOf<String>().apply { addAll(level.scrambledWords) } }
    val selectedWords = remember(currentLevelIdx) { mutableStateListOf<String>() }

    var gameFinished by remember { mutableStateOf(false) }
    var scrambleWinner by remember { mutableStateOf<Boolean?>(null) } // null = play, true = correct order, false = wrong order
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("scramble_screen")
    ) {
        // Scramble Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.testTag("scramble_back_btn")
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back to menu")
            }

            Text(
                text = "Holy Verse Scramble",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            // Round Tracker Indicator
            Box(
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.primaryContainer,
                        RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    "Verse ${currentLevelIdx + 1}/${levels.size}",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp)
        ) {
            // Task Clue Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.03f)),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.MenuBook, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                            Text(
                                "REBUILD REFERENCE: ${level.reference}",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Clue: ${level.clue}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            }

            // Word Placeholders/Assembly Section
            item {
                Text(
                    "Your Constructed Scripture:",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
                
                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    modifier = Modifier.fillMaxWidth().defaultMinSize(minHeight = 100.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    ) {
                        if (selectedWords.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(80.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "Tap word chips below in sequence to compose the verse.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                                    textAlign = TextAlign.Center
                                )
                            }
                        } else {
                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                selectedWords.forEachIndexed { idx, word ->
                                    Box(
                                        modifier = Modifier
                                            .background(
                                                MaterialTheme.colorScheme.primaryContainer,
                                                RoundedCornerShape(8.dp)
                                            )
                                            .clickable {
                                                // Remove word back to scrambled list
                                                selectedWords.removeAt(idx)
                                                activeScrambledWords.add(word)
                                                scrambleWinner = null
                                            }
                                            .padding(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Text(
                                                word,
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onPrimaryContainer
                                            )
                                            Icon(
                                                Icons.Default.Close,
                                                contentDescription = "Remove",
                                                modifier = Modifier.size(12.dp),
                                                tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.5f)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Word Bank Chips Selection
            item {
                Text(
                    "Word Builder Bank:",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
                
                Spacer(modifier = Modifier.height(8.dp))

                if (activeScrambledWords.isEmpty() && scrambleWinner == null) {
                    // Check button
                    Button(
                        onClick = {
                            val userStr = selectedWords.joinToString(" ")
                            val actualStr = level.correctWords.joinToString(" ")
                            // Robust punctuation & space cleaning comparisons
                            val cleanUser = userStr.replace("[.,?!]".toRegex(), "").trim().lowercase()
                            val cleanActual = actualStr.replace("[.,?!]".toRegex(), "").trim().lowercase()
                            
                            if (cleanUser == cleanActual) {
                                scrambleWinner = true
                                viewModel.markChapterAsRead()
                                scope.launch {
                                    val logRepo = com.example.database.AppDatabase.getDatabase(viewModel.getApplication()).activityLogDao()
                                    logRepo.insertLog(ActivityLogEntity(type = "READ", value = "Completed Verse Scramble: ${level.reference}"))
                                }
                            } else {
                                scrambleWinner = false
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("scramble_check_btn"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Verifying Sequence", fontWeight = FontWeight.Bold)
                    }
                } else {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        activeScrambledWords.forEach { word ->
                            Box(
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(10.dp))
                                    .border(
                                        1.dp,
                                        MaterialTheme.colorScheme.outline.copy(alpha = 0.25f),
                                        RoundedCornerShape(10.dp)
                                    )
                                    .clickable {
                                        selectedWords.add(word)
                                        activeScrambledWords.remove(word)
                                    }
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                                    .testTag("scramble_chip_$word")
                            ) {
                                Text(
                                    word,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }

            // Game evaluation states banner
            if (scrambleWinner != null) {
                item {
                    val winner = scrambleWinner == true
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (winner) Color.Green.copy(alpha = 0.08f) else Color.Red.copy(alpha = 0.08f)
                        ),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.5.dp, if (winner) Color.Green.copy(alpha = 0.5f) else Color.Red.copy(alpha = 0.3f))
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = if (winner) Icons.Default.CheckCircle else Icons.Default.Cancel,
                                contentDescription = null,
                                tint = if (winner) Color(0xFF2E7D32) else Color(0xFFC62828),
                                modifier = Modifier.size(36.dp)
                            )

                            Text(
                                text = if (winner) "AMEN! PERFECT SEQUENCE" else "WORDS ARE MISPLACED",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleSmall,
                                color = if (winner) Color(0xFF2E7D32) else Color(0xFFC62828)
                            )

                            Text(
                                text = if (winner) "Great memory! You rearranged the words of ${level.reference} exactly as detailed in orthodox scripture."
                                       else "Sequence does not match standard biblical verse structure. Tap any word above to return it to the word builder bank and try again.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                textAlign = TextAlign.Center
                            )

                            if (winner) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(
                                    onClick = {
                                        if (currentLevelIdx + 1 < levels.size) {
                                            currentLevelIdx += 1
                                            scrambleWinner = null
                                        } else {
                                            // Reset all
                                            currentLevelIdx = 0
                                            scrambleWinner = null
                                            onBack()
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                        .testTag("scramble_action_btn"),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        text = if (currentLevelIdx + 1 < levels.size) "Next Scripture" else "Complete Challenge!",
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// --- 4. GAME MODE: WISDOM FLIP (CHARACTER MATCH MEMORY GAME) ---
@Composable
fun WisdomMatchingGameMode(
    viewModel: BibleViewModel,
    onBack: () -> Unit
) {
    // 6 Pairs total = 12 cards
    val baselinePairs = remember {
        listOf(
            "Moses" to "Splits Red Sea",
            "David" to "Defeats Goliath",
            "Noah" to "Constructs Ark",
            "Paul" to "Apostle to Gentiles",
            "Esther" to "Brave Queen Saves Jews",
            "Daniel" to "Lion's Den Survivor"
        )
    }

    var cardsList = remember {
        mutableStateListOf<MemoryCard>().apply {
            val generated = mutableListOf<MemoryCard>()
            baselinePairs.forEachIndexed { idx, pair ->
                // Add left item
                generated.add(MemoryCard(id = idx * 2, content = pair.first, matchId = idx))
                // Add right item
                generated.add(MemoryCard(id = idx * 2 + 1, content = pair.second, matchId = idx))
            }
            generated.shuffle()
            addAll(generated)
        }
    }

    var selectedFirstIdx by remember { mutableStateOf<Int?>(null) }
    var selectedSecondIdx by remember { mutableStateOf<Int?>(null) }
    var matchAttempts by remember { mutableStateOf(0) }
    var matchedPairsCount by remember { mutableStateOf(0) }
    val scope = rememberCoroutineScope()

    var showCompleteDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("memory_screen")
    ) {
        // Memory Header bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.testTag("memory_back_btn")
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back to menu")
            }

            Text(
                text = "Wisdom Match Game",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            // Round Tracker Indicator
            Box(
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.tertiaryContainer,
                        RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    "Matches: $matchedPairsCount/6",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Instructions banner
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.03f)),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.GridOn,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Column {
                        Text(
                            "Pair biblical figures with their respective historical actions.",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            "Match Attempts: $matchAttempts",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Grid of 12 Flip Cards
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                itemsIndexed(cardsList) { index, card ->
                    MemoryFlipCard(
                        card = card,
                        onClick = {
                            if (selectedFirstIdx == null) {
                                // Select first card
                                selectedFirstIdx = index
                                cardsList[index] = card.copy(isFaceUp = true)
                            } else if (selectedSecondIdx == null && selectedFirstIdx != index) {
                                // Select second card
                                selectedSecondIdx = index
                                cardsList[index] = card.copy(isFaceUp = true)
                                matchAttempts += 1

                                scope.launch {
                                    delay(800) // Brief pause to show the second flipped card
                                    val firstIdx = selectedFirstIdx!!
                                    val secondIdx = selectedSecondIdx!!
                                    
                                    val card1 = cardsList[firstIdx]
                                    val card2 = cardsList[secondIdx]

                                    if (card1.matchId == card2.matchId) {
                                        // Match success!
                                        cardsList[firstIdx] = card1.copy(isMatched = true)
                                        cardsList[secondIdx] = card2.copy(isMatched = true)
                                        matchedPairsCount += 1
                                        
                                        if (matchedPairsCount == 6) {
                                            showCompleteDialog = true
                                            // Write success lock badge & log
                                            scope.launch {
                                                val logRepo = com.example.database.AppDatabase.getDatabase(viewModel.getApplication()).activityLogDao()
                                                logRepo.insertLog(ActivityLogEntity(type = "READ", value = "Won Wisdom Match Game in $matchAttempts attempts!"))
                                            }
                                        }
                                    } else {
                                        // Match failed - flip back down
                                        cardsList[firstIdx] = card1.copy(isFaceUp = false)
                                        cardsList[secondIdx] = card2.copy(isFaceUp = false)
                                    }

                                    selectedFirstIdx = null
                                    selectedSecondIdx = null
                                }
                            }
                        }
                    )
                }
            }

            // Reset Game button
            Button(
                onClick = {
                    cardsList.clear()
                    val generated = mutableListOf<MemoryCard>()
                    baselinePairs.forEachIndexed { idx, pair ->
                        generated.add(MemoryCard(id = idx * 2, content = pair.first, matchId = idx))
                        generated.add(MemoryCard(id = idx * 2 + 1, content = pair.second, matchId = idx))
                    }
                    generated.shuffle()
                    cardsList.addAll(generated)
                    
                    selectedFirstIdx = null
                    selectedSecondIdx = null
                    matchAttempts = 0
                    matchedPairsCount = 0
                    showCompleteDialog = false
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("memory_reset_btn"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Restart Board", fontWeight = FontWeight.Bold)
            }
        }
    }

    if (showCompleteDialog) {
        AlertDialog(
            onDismissRequest = { showCompleteDialog = false },
            title = {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Icon(
                        imageVector = Icons.Default.Campaign,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(54.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Board Completed!",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            },
            text = {
                Text(
                    text = "Congratulations! You successfully matched all 6 Biblical couples and events in $matchAttempts attempts. Your accomplishment has been safely logged in your Spiritual Dashboard tracker!",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showCompleteDialog = false
                        onBack()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Exit to Playroom")
                }
            }
        )
    }
}

@Composable
fun MemoryFlipCard(
    card: MemoryCard,
    onClick: () -> Unit
) {
    // Card flipping animation
    val rotation by animateFloatAsState(
        targetValue = if (card.isFaceUp || card.isMatched) 180f else 0f,
        animationSpec = tween(durationMillis = 400),
        label = "rotation_anim"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 8 * density
            }
            .clickable(enabled = !card.isFaceUp && !card.isMatched, onClick = onClick)
            .testTag("memory_card_${card.id}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = when {
                card.isMatched -> Color.Green.copy(alpha = 0.06f)
                card.isFaceUp -> MaterialTheme.colorScheme.primaryContainer
                else -> MaterialTheme.colorScheme.surface
            }
        ),
        border = BorderStroke(
            width = 1.5.dp,
            color = when {
                card.isMatched -> Color.Green.copy(alpha = 0.5f)
                card.isFaceUp -> MaterialTheme.colorScheme.primary
                else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
            }
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // Check if rotation has crossed the 90 deg boundary to display front or back
            if (rotation <= 90f) {
                // Back of the card (pattern representation)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.08f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "?",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Scripture",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                    )
                }
            } else {
                // Front / Faceup representation of details
                Text(
                    text = card.content,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .padding(8.dp)
                        .graphicsLayer {
                            // Cancel Y inversion
                            rotationY = 180f
                        }
                )
            }
        }
    }
}
