package com.example.ui.screens

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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.vector.ImageVector
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val settings by viewModel.settings.collectAsState()

    var showEditUserDialog by remember { mutableStateOf(false) }
    var showEditAssistantDialog by remember { mutableStateOf(false) }

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
                text = "Profile",
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
            contentPadding = PaddingValues(top = 10.dp, bottom = 120.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Center Profile Avatar & Header
            item {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(92.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(Color(0xFF8B5CF6), Color(0xFF1E1B4B))
                                )
                            )
                            .border(2.dp, LiaGold, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "LIA",
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 2.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = settings?.userName ?: "Aaryan",
                        color = LiaTextPrimary,
                        fontSize = 28.sp,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Normal
                    )
                }
            }

            // Section: You
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "You",
                        color = LiaTextMuted,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                    )

                    Card(
                        colors = CardDefaults.cardColors(containerColor = CosmicSurfaceCard),
                        shape = RoundedCornerShape(18.dp),
                        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(CosmicBorder, LiaViolet.copy(alpha = 0.2f)))),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            ProfileRow(
                                icon = Icons.Default.Edit,
                                title = "Your name",
                                value = settings?.userName ?: "Aaryan",
                                onClick = { showEditUserDialog = true }
                            )
                            HorizontalDivider(color = CosmicBorder.copy(alpha = 0.5f), thickness = 0.8.dp)

                            ProfileRow(
                                icon = Icons.Default.Security,
                                title = "Privacy controls",
                                value = "",
                                onClick = {}
                            )
                        }
                    }
                }
            }

            // Section: Your assistant
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Your assistant",
                        color = LiaTextMuted,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                    )

                    Card(
                        colors = CardDefaults.cardColors(containerColor = CosmicSurfaceCard),
                        shape = RoundedCornerShape(18.dp),
                        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(CosmicBorder, LiaViolet.copy(alpha = 0.2f)))),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            ProfileRow(
                                icon = Icons.Default.AutoAwesome,
                                title = "Name",
                                value = settings?.assistantName ?: "Lia",
                                onClick = { showEditAssistantDialog = true }
                            )
                            HorizontalDivider(color = CosmicBorder.copy(alpha = 0.5f), thickness = 0.8.dp)

                            ProfileRow(
                                icon = Icons.Default.Mood,
                                title = "Personality",
                                value = if (settings?.personality == "GF_MODE") "GF Mode" else (settings?.personality ?: "GF Mode"),
                                onClick = { viewModel.navigateTo(AppNavDestination.PERSONALITY) }
                            )
                            HorizontalDivider(color = CosmicBorder.copy(alpha = 0.5f), thickness = 0.8.dp)

                            ProfileRow(
                                icon = Icons.Default.Language,
                                title = "Language",
                                value = settings?.language ?: "Auto",
                                onClick = {}
                            )
                            HorizontalDivider(color = CosmicBorder.copy(alpha = 0.5f), thickness = 0.8.dp)

                            ProfileRow(
                                icon = Icons.Default.RecordVoiceOver,
                                title = "Voice",
                                value = settings?.voiceName ?: "Kore",
                                onClick = { viewModel.navigateTo(AppNavDestination.VOICE_LANGUAGE) }
                            )
                        }
                    }
                }
            }
        }
    }

    // Edit User Name Dialog
    if (showEditUserDialog) {
        var tempName by remember { mutableStateOf(settings?.userName ?: "Aaryan") }
        BasicAlertDialog(onDismissRequest = { showEditUserDialog = false }) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2138)),
                shape = RoundedCornerShape(20.dp),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(CosmicBorder, LiaGold.copy(alpha = 0.3f)))),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Your Name", color = LiaTextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = tempName,
                        onValueChange = { tempName = it },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = LiaTextPrimary,
                            unfocusedTextColor = LiaTextPrimary,
                            focusedBorderColor = LiaGold,
                            unfocusedBorderColor = CosmicBorder
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        TextButton(onClick = { showEditUserDialog = false }) {
                            Text("Cancel", color = LiaTextSecondary)
                        }
                        TextButton(onClick = {
                            viewModel.updateUserName(tempName)
                            showEditUserDialog = false
                        }) {
                            Text("Save", color = LiaGold, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    // Edit Assistant Name Dialog
    if (showEditAssistantDialog) {
        var tempName by remember { mutableStateOf(settings?.assistantName ?: "Lia") }
        BasicAlertDialog(onDismissRequest = { showEditAssistantDialog = false }) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2138)),
                shape = RoundedCornerShape(20.dp),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(CosmicBorder, LiaGold.copy(alpha = 0.3f)))),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Assistant Name", color = LiaTextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = tempName,
                        onValueChange = { tempName = it },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = LiaTextPrimary,
                            unfocusedTextColor = LiaTextPrimary,
                            focusedBorderColor = LiaGold,
                            unfocusedBorderColor = CosmicBorder
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        TextButton(onClick = { showEditAssistantDialog = false }) {
                            Text("Cancel", color = LiaTextSecondary)
                        }
                        TextButton(onClick = {
                            viewModel.updateAssistantName(tempName)
                            showEditAssistantDialog = false
                        }) {
                            Text("Save", color = LiaGold, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileRow(
    icon: ImageVector,
    title: String,
    value: String,
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
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(CosmicSurfaceElevated),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = LiaGold,
                    modifier = Modifier.size(18.dp)
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
                if (value.isNotBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = value,
                        color = LiaTextSecondary,
                        fontSize = 12.sp
                    )
                }
            }
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Open",
            tint = LiaTextMuted,
            modifier = Modifier.size(20.dp)
        )
    }
}
