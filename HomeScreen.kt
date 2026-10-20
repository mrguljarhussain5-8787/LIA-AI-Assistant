package com.example.ui.screens

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tools.DeviceTools
import com.example.ui.AppNavDestination
import com.example.ui.MainViewModel
import com.example.ui.components.CosmicNebulaOrb
import com.example.ui.theme.CosmicBackground
import com.example.ui.theme.CosmicBorder
import com.example.ui.theme.CosmicSurfaceCard
import com.example.ui.theme.CosmicSurfaceElevated
import com.example.ui.theme.LiaGold
import com.example.ui.theme.LiaGoldGlow
import com.example.ui.theme.LiaTextMuted
import com.example.ui.theme.LiaTextPrimary
import com.example.ui.theme.LiaTextSecondary
import com.example.ui.theme.LiaViolet
import java.util.Locale

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val settings by viewModel.settings.collectAsState()
    val isSpeaking by viewModel.voiceEngine.isSpeaking.collectAsState()
    val visualizerLevel by viewModel.voiceEngine.audioVisualizerLevel.collectAsState()

    val speechLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val spokenText = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.firstOrNull()
            if (!spokenText.isNullOrBlank()) {
                viewModel.sendMessage(spokenText)
            }
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF070814),
                        Color(0xFF0D0F22),
                        Color(0xFF13112E)
                    )
                )
            )
            .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(top = 28.dp, bottom = 120.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // 1. Center Large Cosmic Nebula Orb
        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 10.dp)
            ) {
                CosmicNebulaOrb(
                    size = 230.dp,
                    isSpeaking = isSpeaking,
                    audioLevel = visualizerLevel,
                    onClick = {
                        DeviceTools.vibrate(context, 35)
                        if (isSpeaking) {
                            viewModel.stopSpeaking()
                        } else {
                            viewModel.testRealisticVoice()
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = if (isSpeaking) "Speaking with Lia's voice..." else "Waking up...",
                    color = LiaTextPrimary.copy(alpha = 0.9f),
                    fontSize = 24.sp,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // 2. Action Buttons: [🎤 Talk] (Gold) and [💬 Chat] (Glass card)
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Talk Button (Golden pill)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(54.dp)
                        .clip(RoundedCornerShape(27.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(LiaGold, LiaGoldGlow)
                            )
                        )
                        .clickable {
                            DeviceTools.vibrate(context, 35)
                            try {
                                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                                    putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                                    putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                                    putExtra(RecognizerIntent.EXTRA_PROMPT, "Talk to ${settings?.assistantName ?: "LIA"}...")
                                }
                                speechLauncher.launch(intent)
                            } catch (_: Exception) {
                                viewModel.testRealisticVoice()
                            }
                        }
                        .testTag("home_talk_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = "Talk",
                            tint = Color.Black,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Talk",
                            color = Color.Black,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Chat Button (Dark card)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(54.dp)
                        .clip(RoundedCornerShape(27.dp))
                        .background(CosmicSurfaceElevated)
                        .border(1.dp, CosmicBorder, RoundedCornerShape(27.dp))
                        .clickable {
                            viewModel.navigateTo(AppNavDestination.CHAT)
                        }
                        .testTag("home_chat_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Chat,
                            contentDescription = "Chat",
                            tint = LiaTextPrimary,
                            modifier = Modifier.size(19.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Chat",
                            color = LiaTextPrimary,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        // 3. "Try saying" Section
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Try saying",
                    color = LiaTextMuted,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(start = 4.dp, top = 6.dp)
                )

                val samplePrompts = listOf(
                    "\"YouTube kholo aur lo-fi chalao\"",
                    "\"Mummy ko WhatsApp karo ki main late hoon\"",
                    "\"Papa ko call karo\""
                )

                samplePrompts.forEach { phrase ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = CosmicSurfaceCard),
                        shape = RoundedCornerShape(16.dp),
                        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(CosmicBorder, LiaViolet.copy(alpha = 0.2f)))),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val cleanText = phrase.replace("\"", "")
                                viewModel.sendMessage(cleanText)
                                viewModel.navigateTo(AppNavDestination.CHAT)
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 15.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = phrase,
                                color = LiaTextPrimary,
                                fontSize = 15.sp,
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Normal
                            )
                            Icon(
                                imageVector = Icons.Default.Mic,
                                contentDescription = "Speak",
                                tint = LiaGold,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }

        // 4. Quick Actions Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CosmicSurfaceCard),
                shape = RoundedCornerShape(18.dp),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(CosmicBorder, LiaViolet.copy(alpha = 0.3f)))),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        viewModel.navigateTo(AppNavDestination.QUICK_ACTIONS)
                    }
                    .testTag("quick_actions_launcher_card")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(CosmicSurfaceElevated),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Apps,
                            contentDescription = "Apps",
                            tint = LiaGold,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Quick actions",
                            color = LiaTextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Open apps, calls and messages",
                            color = LiaTextSecondary,
                            fontSize = 13.sp
                        )
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Open",
                        tint = LiaTextMuted,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
