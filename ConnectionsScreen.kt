package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppNavDestination
import com.example.ui.MainViewModel
import com.example.ui.theme.CosmicBackground
import com.example.ui.theme.CosmicBorder
import com.example.ui.theme.CosmicSurfaceCard
import com.example.ui.theme.CosmicSurfaceElevated
import com.example.ui.theme.LiaGold
import com.example.ui.theme.LiaTextMuted
import com.example.ui.theme.LiaTextPrimary
import com.example.ui.theme.LiaTextSecondary
import com.example.ui.theme.LiaViolet
import com.example.ui.theme.StatusGreen

@Composable
fun ConnectionsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val settings by viewModel.settings.collectAsState()

    var geminiKeyInput by remember(settings?.geminiApiKey) { mutableStateOf(settings?.geminiApiKey ?: "") }
    var groqKeyInput by remember(settings?.groqApiKey) { mutableStateOf(settings?.groqApiKey ?: "") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CosmicBackground)
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.navigateTo(AppNavDestination.SETTINGS) }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = LiaTextPrimary
                )
            }
            Text(
                text = "Connections",
                color = LiaTextPrimary,
                fontSize = 26.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Normal
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 4.dp, bottom = 120.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "LIA talks and chats through your own keys. They are stored only on this phone.",
                    color = LiaTextSecondary,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
            }

            // Card 1: Voice - Gemini key
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = CosmicSurfaceCard),
                    shape = RoundedCornerShape(20.dp),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(CosmicBorder, LiaViolet.copy(alpha = 0.25f)))),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Voice - Gemini key",
                                color = LiaTextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Active",
                                    tint = StatusGreen,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Using LIA's key",
                                    color = StatusGreen,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Powers live voice conversations.",
                            color = LiaTextSecondary,
                            fontSize = 12.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))
                        OutlinedTextField(
                            value = geminiKeyInput,
                            onValueChange = { geminiKeyInput = it },
                            placeholder = { Text("Paste your Gemini API key", color = LiaTextMuted, fontSize = 13.sp) },
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = CosmicSurfaceElevated,
                                unfocusedContainerColor = CosmicSurfaceElevated,
                                focusedBorderColor = LiaGold,
                                unfocusedBorderColor = CosmicBorder,
                                focusedTextColor = LiaTextPrimary,
                                unfocusedTextColor = LiaTextPrimary
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(14.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .clip(RoundedCornerShape(25.dp))
                                .background(LiaGold)
                                .clickable {
                                    viewModel.updateGeminiKey(geminiKeyInput)
                                    Toast.makeText(context, "Gemini key saved securely on device", Toast.LENGTH_SHORT).show()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Save Gemini key",
                                color = Color.Black,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable {
                                try {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://aistudio.google.com/"))
                                    context.startActivity(intent)
                                } catch (_: Exception) {}
                            }
                        ) {
                            Text(
                                text = "Get a free key at Google AI Studio",
                                color = LiaGold,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.OpenInNew,
                                contentDescription = "Open",
                                tint = LiaGold,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }

            // Card 2: Chat - Groq keys
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = CosmicSurfaceCard),
                    shape = RoundedCornerShape(20.dp),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(CosmicBorder, LiaViolet.copy(alpha = 0.25f)))),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Chat - Groq keys",
                                color = LiaTextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Active",
                                    tint = StatusGreen,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Ready",
                                    color = StatusGreen,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Fast typed replies. Add several keys, separated by commas; they take turns if one is rate limited.",
                            color = LiaTextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))
                        OutlinedTextField(
                            value = groqKeyInput,
                            onValueChange = { groqKeyInput = it },
                            placeholder = { Text("gsk_..., gsk_...", color = LiaTextMuted, fontSize = 13.sp) },
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = CosmicSurfaceElevated,
                                unfocusedContainerColor = CosmicSurfaceElevated,
                                focusedBorderColor = LiaGold,
                                unfocusedBorderColor = CosmicBorder,
                                focusedTextColor = LiaTextPrimary,
                                unfocusedTextColor = LiaTextPrimary
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(14.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .clip(RoundedCornerShape(25.dp))
                                .background(LiaGold)
                                .clickable {
                                    viewModel.updateGroqKey(groqKeyInput)
                                    Toast.makeText(context, "Groq keys saved securely on device", Toast.LENGTH_SHORT).show()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Save Groq keys",
                                color = Color.Black,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable {
                                try {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://console.groq.com/"))
                                    context.startActivity(intent)
                                } catch (_: Exception) {}
                            }
                        ) {
                            Text(
                                text = "Get a free key at Groq",
                                color = LiaGold,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.OpenInNew,
                                contentDescription = "Open",
                                tint = LiaGold,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
