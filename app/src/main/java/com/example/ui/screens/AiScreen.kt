package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import com.example.viewmodel.BibleViewModel
import kotlinx.coroutines.launch

@Composable
fun AiScreen(viewModel: BibleViewModel) {
    var activeSubTab by remember { mutableStateOf("ASSISTANT") } // "ASSISTANT", "QUIZ"
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("ai_screen_container")
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Screen Intro
        Text(
            text = "AI Discipleship Guidance",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        )
        Text(
            text = "Ask theological questions, prepare sermons, or quiz your scripture knowledge.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.height(12.dp))

        // Sub-Tab Segment Button
        TabRow(
            selectedTabIndex = if (activeSubTab == "ASSISTANT") 0 else 1,
            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .height(48.dp),
            indicator = { /* Transparent / No indicator needed as we do styled tabs */ }
        ) {
            Tab(
                selected = activeSubTab == "ASSISTANT",
                onClick = {
                    activeSubTab = "ASSISTANT"
                    focusManager.clearFocus()
                },
                modifier = Modifier.testTag("tab_ai_assistant")
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.SupportAgent,
                        contentDescription = null,
                        tint = if (activeSubTab == "ASSISTANT") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        "AI Assistant",
                        fontWeight = if (activeSubTab == "ASSISTANT") FontWeight.Bold else FontWeight.Normal,
                        color = if (activeSubTab == "ASSISTANT") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }

            Tab(
                selected = activeSubTab == "QUIZ",
                onClick = {
                    activeSubTab = "QUIZ"
                    focusManager.clearFocus()
                },
                modifier = Modifier.testTag("tab_ai_quiz")
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Psychology,
                        contentDescription = null,
                        tint = if (activeSubTab == "QUIZ") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        "Knowledge Challengers",
                        fontWeight = if (activeSubTab == "QUIZ") FontWeight.Bold else FontWeight.Normal,
                        color = if (activeSubTab == "QUIZ") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Dynamic Subview Render
        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            AnimatedContent(
                targetState = activeSubTab,
                transitionSpec = {
                    fadeIn() togetherWith fadeOut()
                },
                label = "ai_subviews"
            ) { target ->
                when (target) {
                    "ASSISTANT" -> AiChatView(viewModel)
                    "QUIZ" -> AiQuizView(viewModel)
                }
            }
        }
    }
}

// 1. AI CHAT COMPOSABLE
@Composable
fun AiChatView(viewModel: BibleViewModel) {
    val chatHistory by viewModel.chatHistory.collectAsState()
    val isChatLoading by viewModel.isChatLoading.collectAsState()
    val chatInputText by viewModel.chatInput.collectAsState()
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    // Keep chat scrolled down when loading or receiving message
    LaunchedEffect(chatHistory.size, isChatLoading) {
        if (chatHistory.isNotEmpty()) {
            listState.animateScrollToItem(chatHistory.size - 1)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Chat Logs
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Preset Suggestion Prompt chips inside chat log if only instructions are there
            if (chatHistory.size == 1) {
                item {
                    Text(
                        text = "Suggested study starting points:",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(viewModel.presets) { preset ->
                            SuggestionChip(
                                onClick = { viewModel.sendChatMessage(preset.second) },
                                label = { Text(preset.first, fontSize = 12.sp) },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.testTag("preset_${preset.first}")
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            items(chatHistory) { message ->
                val isUser = message.second
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
                ) {
                    if (!isUser) {
                        Surface(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary),
                            color = MaterialTheme.colorScheme.primary
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Default.AutoAwesome,
                                    contentDescription = "AI",
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    Surface(
                        color = if (isUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                        contentColor = if (isUser) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                        shape = RoundedCornerShape(
                            topStart = 16.dp, 
                            topEnd = 16.dp, 
                            bottomStart = if (isUser) 16.dp else 4.dp, 
                            bottomEnd = if (isUser) 4.dp else 16.dp
                        ),
                        modifier = Modifier.widthIn(max = 280.dp)
                    ) {
                        Text(
                            text = message.first,
                            modifier = Modifier.padding(12.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            lineHeight = 22.sp
                        )
                    }

                    if (isUser) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "User icon",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }

            // Chat loading typing bubble
            if (isChatLoading) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(start = 40.dp),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "AI is analyzing scriptures...",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        )
                    }
                }
            }
        }

        // Chat Input Pad
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(
                onClick = { viewModel.clearChat() },
                modifier = Modifier.testTag("clear_chat_btn")
            ) {
                Icon(Icons.Default.Refresh, contentDescription = "Clear Chat history", tint = MaterialTheme.colorScheme.error)
            }

            OutlinedTextField(
                value = chatInputText,
                onValueChange = { viewModel.chatInput.value = it },
                placeholder = { Text("Ask about theological meanings...") },
                modifier = Modifier.weight(1f).testTag("chat_input_field"),
                shape = RoundedCornerShape(24.dp),
                maxLines = 3,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedContainerColor = MaterialTheme.colorScheme.surface
                )
            )

            FloatingActionButton(
                onClick = {
                    if (chatInputText.isNotBlank()) {
                        viewModel.sendChatMessage(chatInputText)
                        focusManager.clearFocus()
                    }
                },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(48.dp).testTag("send_chat_btn")
            ) {
                Icon(Icons.Default.Send, contentDescription = "Send", modifier = Modifier.size(18.dp))
            }
        }
    }
}

// 2. AI BIBLE QUIZ COMPOSABLE
@Composable
fun AiQuizView(viewModel: BibleViewModel) {
    val isQuizLoading by viewModel.isQuizLoading.collectAsState()
    val quizTopic by viewModel.currentQuizTopic.collectAsState()
    val activeQuizList by viewModel.activeQuizList.collectAsState()
    val currentQuizIndex by viewModel.currentQuizIndex.collectAsState()
    val selectedOption by viewModel.selectedOption.collectAsState()
    val quizScore by viewModel.quizScore.collectAsState()
    val showExplanation by viewModel.showQuizExplanation.collectAsState()
    val quizCompleted by viewModel.quizCompleted.collectAsState()
    val answeredCorrectly by viewModel.answeredCorrectly.collectAsState()

    var customTopicInput by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    val topics = listOf("Faith", "Life of Jesus", "Moses & Exodus", "Eternity & Revelation", "Prophets")

    if (activeQuizList.isEmpty() && !isQuizLoading) {
        // Selection State / Initial State
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Quiz,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                "Biblical Knowledge Challenge",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                "Generate custom 5-question quizzes instantly via AI to test and strengthen your theological knowledge.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Topics Rows
            Text(
                "Choose an inspiration:",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                items(topics) { topic ->
                    SuggestionChip(
                        onClick = { viewModel.startQuiz(topic) },
                        label = { Text(topic) },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("quiz_topic_chip_$topic")
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("— OR WRITE A SPECIFIC TOPIC —", style = MaterialTheme.typography.labelSmall)
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = customTopicInput,
                onValueChange = { customTopicInput = it },
                placeholder = { Text("E.g., Pauline Epistles, Fruits of the Spirit...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .testTag("custom_quiz_input"),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (customTopicInput.isNotBlank()) {
                        viewModel.startQuiz(customTopicInput)
                        focusManager.clearFocus()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .height(48.dp)
                    .testTag("start_custom_quiz_btn"),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Generate AI Topic Quiz", fontWeight = FontWeight.Bold)
            }
        }
    } else if (isQuizLoading) {
        // Loading State
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Searching Scriptures for Insight...",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                "AI is generating 5 tailored theological questions on \"$quizTopic\"...",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Surface(
                modifier = Modifier.padding(24.dp),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
            ) {
                Text(
                    text = "\"The law of Thy mouth is better unto me than thousands of gold and silver.\" - Psalm 119:72",
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    } else if (quizCompleted) {
        // Completion/Performance Dashboard state
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(100.dp)) {
                CircularProgressIndicator(
                    progress = { quizScore / 5f },
                    color = MaterialTheme.colorScheme.tertiary,
                    trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    strokeWidth = 10.dp,
                    modifier = Modifier.fillMaxSize()
                )
                Text(
                    text = "$quizScore/5",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = if (quizScore == 5) "Godly Scholar Badge Unlocked!" else if (quizScore >= 3) "Excellent Progress!" else "Keep Studying Scripture",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                text = if (quizScore == 5) "Perfect score! You have displayed impeccable command of Bible doctrine. The 'Theology Scholar' achievement card is placed in your dashboard!"
                       else "Good work. Keep challenging your understanding of the Scriptures daily to grow in spiritual wisdom.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { viewModel.startQuiz(quizTopic) },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Replay, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Try Again", fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = {
                    viewModel.activeQuizList.value = emptyList()
                    viewModel.quizCompleted.value = false
                },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Return to Quiz list", fontWeight = FontWeight.SemiBold)
            }
        }
    } else {
        // Active Question slide
        val questionObj = activeQuizList.getOrNull(currentQuizIndex)
        if (questionObj != null) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Header progress
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Challenge Topic: $quizTopic",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        "Question ${currentQuizIndex + 1} of 5",
                        style = MaterialTheme.typography.labelSmall
                    )
                }

                LinearProgressIndicator(
                    progress = { (currentQuizIndex + 1) / 5f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                // The Question Text
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
                ) {
                    Text(
                        text = questionObj.question,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(18.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 4 Options rendering
                questionObj.options.forEachIndexed { optIdx, option ->
                    val isOptionSelected = selectedOption == optIdx
                    val correctIdx = questionObj.correctAnswerIndex
                    
                    val cardBg = when {
                        selectedOption == null -> {
                            if (isOptionSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                        }
                        optIdx == correctIdx -> Color(0xFFE8F5E9) // correct highlights green
                        isOptionSelected -> Color(0xFFFFEBEE) // wrong selection highlights red
                        else -> MaterialTheme.colorScheme.surface
                    }

                    val outlineColor = when {
                        selectedOption == null -> {
                            if (isOptionSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                        }
                        optIdx == correctIdx -> Color(0xFF2E7D32)
                        isOptionSelected -> Color(0xFFC62828)
                        else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.selectQuizOption(optIdx) }
                            .testTag("quiz_opt_$optIdx"),
                        colors = CardDefaults.cardColors(containerColor = cardBg),
                        border = CardDefaults.outlinedCardBorder().copy(width = 1.5.dp, brush = androidx.compose.ui.graphics.SolidColor(outlineColor)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val optionPrefix = when (optIdx) {
                                0 -> "A"
                                1 -> "B"
                                2 -> "C"
                                else -> "D"
                            }
                            Surface(
                                shape = CircleShape,
                                color = if (selectedOption == null) MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                                        else if (optIdx == correctIdx) Color(0xFF2E7D32)
                                        else if (isOptionSelected) Color(0xFFC62828)
                                        else MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                                modifier = Modifier.size(28.dp),
                                contentColor = if (selectedOption != null && (optIdx == correctIdx || isOptionSelected)) Color.White
                                               else MaterialTheme.colorScheme.primary
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(optionPrefix, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(text = option, style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }

                // Answer explanation card loaded after select
                AnimatedVisibility(
                    visible = showExplanation,
                    enter = slideInVertically(initialOffsetY = { -20 }) + fadeIn(),
                    exit = slideOutVertically(targetOffsetY = { -20 }) + fadeOut()
                ) {
                    Surface(
                        modifier = Modifier.fillMaxWidth().weight(1f),
                        color = if (answeredCorrectly == true) Color(0xFFE8F5E9).copy(alpha = 0.6f) else Color(0xFFFFEBEE).copy(alpha = 0.6f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        LazyColumn(modifier = Modifier.padding(12.dp)) {
                            item {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = if (answeredCorrectly == true) Icons.Default.CheckCircle else Icons.Default.Cancel,
                                        contentDescription = null,
                                        tint = if (answeredCorrectly == true) Color(0xFF2E7D32) else Color(0xFFC62828)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (answeredCorrectly == true) "Correct! Praise God." else "Incorrect, learn context:",
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.titleSmall,
                                        color = if (answeredCorrectly == true) Color(0xFF2E7D32) else Color(0xFFC62828)
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = questionObj.explanation,
                                    style = MaterialTheme.typography.bodyMedium,
                                    lineHeight = 20.sp
                                )
                            }
                        }
                    }
                }

                // Next Button
                if (selectedOption != null) {
                    Button(
                        onClick = { viewModel.nextQuizQuestion() },
                        modifier = Modifier.fillMaxWidth().height(48.dp).testTag("next_quiz_q_btn"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = if (currentQuizIndex == 4) "Show Results" else "Next Question",
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.Default.ArrowForward, contentDescription = null)
                    }
                }
            }
        }
    }
}
