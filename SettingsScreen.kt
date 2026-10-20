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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.vector.ImageVector
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
fun SettingsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val settings by viewModel.settings.collectAsState()
    val chatMessages by viewModel.chatMessages.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CosmicBackground)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 18.dp, bottom = 120.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Title
        item {
            Text(
                text = "Settings",
                color = LiaTextPrimary,
                fontSize = 28.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(bottom = 6.dp)
            )
        }

        // Profile Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CosmicSurfaceCard),
                shape = RoundedCornerShape(18.dp),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(CosmicBorder, LiaViolet.copy(alpha = 0.25f)))),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.navigateTo(AppNavDestination.PROFILE) }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Circular avatar with LIA AI logo
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(Color(0xFF8B5CF6), Color(0xFF1E1B4B))
                                )
                            )
                            .border(1.5.dp, LiaGold.copy(alpha = 0.6f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "LIA",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = settings?.userName ?: "Aaryan",
                            color = LiaTextPrimary,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Your profile",
                            color = LiaTextSecondary,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        // Status Pills (Voice Ready & Chat Ready)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatusPill(
                    label = "Voice",
                    status = "Ready",
                    modifier = Modifier.weight(1f)
                )
                StatusPill(
                    label = "Chat",
                    status = "Ready",
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Join Telegram community card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CosmicSurfaceCard),
                shape = RoundedCornerShape(16.dp),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(CosmicBorder, Color(0xFF0088CC).copy(alpha = 0.3f)))),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/"))
                            context.startActivity(intent)
                        } catch (_: Exception) {
                            Toast.makeText(context, "Telegram link ready", Toast.LENGTH_SHORT).show()
                        }
                    }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(LiaGold.copy(alpha = 0.18f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = "Telegram",
                                tint = LiaGold,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Join Telegram community",
                            color = LiaTextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
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

        // Navigation Menu List (Single grouped card with clean dividers)
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CosmicSurfaceCard),
                shape = RoundedCornerShape(18.dp),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(CosmicBorder, LiaViolet.copy(alpha = 0.2f)))),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    SettingsRow(
                        icon = Icons.Default.RecordVoiceOver,
                        iconTint = LiaGold,
                        title = "Voice & language",
                        subtitle = "${settings?.voiceName ?: "Kore"} • ${settings?.language ?: "Auto"} • ${String.format("%.2f", settings?.speechSpeed ?: 0.85f)}x",
                        onClick = { viewModel.navigateTo(AppNavDestination.VOICE_LANGUAGE) }
                    )
                    HorizontalDivider(color = CosmicBorder.copy(alpha = 0.5f), thickness = 0.8.dp)

                    SettingsRow(
                        icon = Icons.Default.Key,
                        iconTint = Color(0xFF00E5FF),
                        title = "Connections",
                        subtitle = "Gemini and Groq keys",
                        onClick = { viewModel.navigateTo(AppNavDestination.CONNECTIONS) }
                    )
                    HorizontalDivider(color = CosmicBorder.copy(alpha = 0.5f), thickness = 0.8.dp)

                    SettingsRow(
                        icon = Icons.Default.Palette,
                        iconTint = Color(0xFFEC4899),
                        title = "Look & feel",
                        subtitle = "System • Classic orb",
                        onClick = { viewModel.navigateTo(AppNavDestination.VOICE_LANGUAGE) }
                    )
                    HorizontalDivider(color = CosmicBorder.copy(alpha = 0.5f), thickness = 0.8.dp)

                    SettingsRow(
                        icon = Icons.Default.Sms,
                        iconTint = Color(0xFFF59E0B),
                        title = "Chats & memory",
                        subtitle = "${chatMessages.size} saved chats",
                        onClick = { viewModel.navigateTo(AppNavDestination.HISTORY) }
                    )
                    HorizontalDivider(color = CosmicBorder.copy(alpha = 0.5f), thickness = 0.8.dp)

                    SettingsRow(
                        icon = Icons.Default.Smartphone,
                        iconTint = Color(0xFF10B981),
                        title = "Phone control",
                        subtitle = "Quick actions and permissions",
                        onClick = { viewModel.navigateTo(AppNavDestination.QUICK_ACTIONS) }
                    )
                    HorizontalDivider(color = CosmicBorder.copy(alpha = 0.5f), thickness = 0.8.dp)

                    SettingsRow(
                        icon = Icons.Default.Info,
                        iconTint = Color(0xFF94A3B8),
                        title = "Privacy & about",
                        subtitle = "Your data, version, support",
                        onClick = { viewModel.navigateTo(AppNavDestination.PROFILE) }
                    )
                }
            }
        }
    }
}

@Composable
fun StatusPill(
    label: String,
    status: String,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CosmicSurfaceElevated),
        shape = RoundedCornerShape(16.dp),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(CosmicBorder, StatusGreen.copy(alpha = 0.25f)))),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = status,
                tint = StatusGreen,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = label,
                    color = LiaTextMuted,
                    fontSize = 11.sp
                )
                Text(
                    text = status,
                    color = LiaTextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun SettingsRow(
    icon: ImageVector,
    iconTint: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(iconTint.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconTint,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(
                    text = title,
                    color = LiaTextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    color = LiaTextSecondary,
                    fontSize = 12.sp
                )
            }
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Navigate",
            tint = LiaTextMuted,
            modifier = Modifier.size(20.dp)
        )
    }
}
