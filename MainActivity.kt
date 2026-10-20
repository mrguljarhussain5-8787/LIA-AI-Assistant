package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppNavDestination
import com.example.ui.MainViewModel
import com.example.ui.components.CosmicNebulaOrb
import com.example.ui.screens.ChatScreen
import com.example.ui.screens.ConnectionsScreen
import com.example.ui.screens.ConversationsHistoryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.PersonalityScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.QuickActionsScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.VoiceAndLanguageScreen
import com.example.ui.theme.CosmicBackground
import com.example.ui.theme.CosmicBorder
import com.example.ui.theme.LiaGold
import com.example.ui.theme.LiaTextMuted
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settings by viewModel.settings.collectAsState()
            val currentScreen by viewModel.currentScreen.collectAsState()

            MyApplicationTheme {
                BackHandler(enabled = currentScreen != AppNavDestination.HOME) {
                    when (currentScreen) {
                        AppNavDestination.QUICK_ACTIONS -> viewModel.navigateTo(AppNavDestination.HOME)
                        AppNavDestination.PERSONALITY -> viewModel.navigateTo(AppNavDestination.VOICE_LANGUAGE)
                        AppNavDestination.VOICE_LANGUAGE -> viewModel.navigateTo(AppNavDestination.SETTINGS)
                        AppNavDestination.CONNECTIONS -> viewModel.navigateTo(AppNavDestination.SETTINGS)
                        AppNavDestination.PROFILE -> viewModel.navigateTo(AppNavDestination.SETTINGS)
                        else -> viewModel.navigateTo(AppNavDestination.HOME)
                    }
                }

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(CosmicBackground),
                    bottomBar = {
                        LiaBottomNavigationBar(
                            currentDestination = currentScreen,
                            isSpeaking = viewModel.voiceEngine.isSpeaking.collectAsState().value,
                            audioLevel = viewModel.voiceEngine.audioVisualizerLevel.collectAsState().value,
                            onNavigate = { viewModel.navigateTo(it) },
                            onOrbClick = {
                                viewModel.testRealisticVoice()
                            }
                        )
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        Crossfade(targetState = currentScreen, label = "ScreenTransition") { destination ->
                            when (destination) {
                                AppNavDestination.HOME -> HomeScreen(viewModel = viewModel)
                                AppNavDestination.CHAT -> ChatScreen(viewModel = viewModel)
                                AppNavDestination.HISTORY -> ConversationsHistoryScreen(viewModel = viewModel)
                                AppNavDestination.SETTINGS -> SettingsScreen(viewModel = viewModel)
                                AppNavDestination.QUICK_ACTIONS -> QuickActionsScreen(viewModel = viewModel)
                                AppNavDestination.VOICE_LANGUAGE -> VoiceAndLanguageScreen(viewModel = viewModel)
                                AppNavDestination.CONNECTIONS -> ConnectionsScreen(viewModel = viewModel)
                                AppNavDestination.PERSONALITY -> PersonalityScreen(viewModel = viewModel)
                                AppNavDestination.PROFILE -> ProfileScreen(viewModel = viewModel)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LiaBottomNavigationBar(
    currentDestination: AppNavDestination,
    isSpeaking: Boolean,
    audioLevel: Float,
    onNavigate: (AppNavDestination) -> Unit,
    onOrbClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp),
            color = Color(0xFF0F1020).copy(alpha = 0.96f),
            border = androidx.compose.foundation.BorderStroke(1.dp, CosmicBorder.copy(alpha = 0.6f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Home
                NavTabButton(
                    label = "Home",
                    icon = if (currentDestination == AppNavDestination.HOME) Icons.Filled.Home else Icons.Outlined.Home,
                    isSelected = currentDestination == AppNavDestination.HOME,
                    onClick = { onNavigate(AppNavDestination.HOME) }
                )

                // Chat
                NavTabButton(
                    label = "Chat",
                    icon = if (currentDestination == AppNavDestination.CHAT) Icons.AutoMirrored.Filled.Chat else Icons.AutoMirrored.Outlined.Chat,
                    isSelected = currentDestination == AppNavDestination.CHAT,
                    onClick = { onNavigate(AppNavDestination.CHAT) }
                )

                // Center spacer for the protruding Orb
                Spacer(modifier = Modifier.size(54.dp))

                // History
                NavTabButton(
                    label = "History",
                    icon = if (currentDestination == AppNavDestination.HISTORY) Icons.Filled.History else Icons.Outlined.History,
                    isSelected = currentDestination == AppNavDestination.HISTORY,
                    onClick = { onNavigate(AppNavDestination.HISTORY) }
                )

                // Settings
                NavTabButton(
                    label = "Settings",
                    icon = if (currentDestination in listOf(
                            AppNavDestination.SETTINGS,
                            AppNavDestination.VOICE_LANGUAGE,
                            AppNavDestination.CONNECTIONS,
                            AppNavDestination.PERSONALITY,
                            AppNavDestination.PROFILE
                        )) Icons.Filled.Settings else Icons.Outlined.Settings,
                    isSelected = currentDestination in listOf(
                        AppNavDestination.SETTINGS,
                        AppNavDestination.VOICE_LANGUAGE,
                        AppNavDestination.CONNECTIONS,
                        AppNavDestination.PERSONALITY,
                        AppNavDestination.PROFILE
                    ),
                    onClick = { onNavigate(AppNavDestination.SETTINGS) }
                )
            }
        }

        // Protruding Center Glowing Orb Button (matching screenshot exactly!)
        Box(
            modifier = Modifier
                .offset(y = (-16).dp)
                .size(62.dp)
                .clip(CircleShape)
                .background(Color(0xFF0B0C18))
                .border(2.dp, LiaGold.copy(alpha = 0.8f), CircleShape)
                .clickable { onOrbClick() }
                .testTag("nav_center_orb_button"),
            contentAlignment = Alignment.Center
        ) {
            CosmicNebulaOrb(
                size = 54.dp,
                isSpeaking = isSpeaking,
                audioLevel = audioLevel,
                onClick = onOrbClick
            )
        }
    }
}

@Composable
fun NavTabButton(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) LiaGold else LiaTextMuted,
            modifier = Modifier.size(22.dp)
        )
        Text(
            text = label,
            color = if (isSelected) LiaGold else LiaTextMuted,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}
