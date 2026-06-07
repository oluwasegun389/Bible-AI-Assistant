package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.BibleViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppLayout()
            }
        }
    }
}

@Composable
fun MainAppLayout() {
    val viewModel: BibleViewModel = viewModel()
    var currentTabIdx by remember { mutableStateOf(0) }

    val tabs = listOf(
        TabItem("Study", Icons.Default.MenuBook, "study_tab"),
        TabItem("AI Companion", Icons.Default.AutoAwesome, "ai_tab"),
        TabItem("Games", Icons.Default.Extension, "games_tab"),
        TabItem("Hymnal", Icons.Default.MusicNote, "hymn_tab"),
        TabItem("Fellowship", Icons.Default.Groups, "fellowship_tab"),
        TabItem("Dashboard", Icons.Default.TrendingUp, "tracker_tab")
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("app_bottom_navigation"),
                tonalElevation = 8.dp
            ) {
                tabs.forEachIndexed { index, tab ->
                    NavigationBarItem(
                        selected = currentTabIdx == index,
                        onClick = { currentTabIdx = index },
                        icon = { Icon(tab.icon, contentDescription = tab.title) },
                        label = { Text(tab.title) },
                        alwaysShowLabel = false,
                        modifier = Modifier.testTag(tab.testTagId)
                    )
                }
            }
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            AnimatedContent(
                targetState = currentTabIdx,
                transitionSpec = {
                    fadeIn() togetherWith fadeOut()
                },
                label = "main_screens"
            ) { targetIdx ->
                when (targetIdx) {
                    0 -> StudyScreen(viewModel)
                    1 -> AiScreen(viewModel)
                    2 -> PlayScreen(viewModel)
                    3 -> HymnScreen(viewModel)
                    4 -> CommunityScreen(viewModel)
                    5 -> GrowthScreen(viewModel)
                }
            }
        }
    }
}

data class TabItem(
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val testTagId: String
)
