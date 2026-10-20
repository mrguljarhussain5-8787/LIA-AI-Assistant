package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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

data class VoiceProfile(
    val name: String,
    val description: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoiceAndLanguageScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val settings by viewModel.settings.collectAsState()

    var showVoiceDialog by remember { mutableStateOf(false) }
    var showModelDialog by remember { mutableStateOf(false) }
    var showSpeedDialog by remember { mutableStateOf(false) }
    var showVolumeDialog by remember { mutableStateOf(false) }

    val allVoices = listOf(
        VoiceProfile("Aoede", "Breezy"),
        VoiceProfile("Kore", "Firm"),
        VoiceProfile("Zephyr", "Bright"),
        VoiceProfile("Leda", "Youthful"),
        VoiceProfile("Callirrhoe", "Easy-going"),
        VoiceProfile("Autonoe", "Bright"),
        VoiceProfile("Despina", "Smooth"),
        VoiceProfile("Erinome", "Clear"),
        VoiceProfile("Laomedeia", "Upbeat"),
        VoiceProfile("Achernar", "Soft"),
        VoiceProfile("Gacrux", "Mature"),
        VoiceProfile("Pulcherrima", "Forward"),
        VoiceProfile("Vindemiatrix", "Gentle"),
        VoiceProfile("Sulafat", "Warm")
    )

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
                text = "Voice & language",
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
            contentPadding = PaddingValues(top = 8.dp, bottom = 120.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Group 1: Identity & Style
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = CosmicSurfaceCard),
                    shape = RoundedCornerShape(18.dp),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(CosmicBorder, LiaViolet.copy(alpha = 0.2f)))),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        VoiceConfigRow(
                            icon = Icons.Default.AutoAwesome,
                            title = "Assistant name",
                            value = settings?.assistantName ?: "Lia",
                            onClick = { viewModel.navigateTo(AppNavDestination.PROFILE) }
                        )
                        HorizontalDivider(color = CosmicBorder.copy(alpha = 0.5f), thickness = 0.8.dp)

                        VoiceConfigRow(
                            icon = Icons.Default.Mood,
                            title = "Personality",
                            value = if (settings?.personality == "GF_MODE") "GF Mode" else (settings?.personality ?: "GF Mode"),
                            onClick = { viewModel.navigateTo(AppNavDestination.PERSONALITY) }
                        )
                        HorizontalDivider(color = CosmicBorder.copy(alpha = 0.5f), thickness = 0.8.dp)

                        VoiceConfigRow(
                            icon = Icons.Default.Language,
                            title = "Language",
                            value = settings?.language ?: "Auto",
                            onClick = {}
                        )
                        HorizontalDivider(color = CosmicBorder.copy(alpha = 0.5f), thickness = 0.8.dp)

                        VoiceConfigRow(
                            icon = Icons.Default.Tune,
                            title = "Response style",
                            value = settings?.responseStyle ?: "Balanced",
                            onClick = {}
                        )
                    }
                }
            }

            // Group 2: How she sounds
            item {
                Text(
                    text = "How she sounds",
                    color = LiaTextMuted,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                )
            }

            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = CosmicSurfaceCard),
                    shape = RoundedCornerShape(18.dp),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(CosmicBorder, LiaViolet.copy(alpha = 0.2f)))),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        VoiceConfigRow(
                            icon = Icons.Default.Person,
                            title = "Voice",
                            value = "${settings?.voiceName ?: "Kore"} - ${settings?.voiceQuality ?: "Firm"}",
                            onClick = { showVoiceDialog = true }
                        )
                        HorizontalDivider(color = CosmicBorder.copy(alpha = 0.5f), thickness = 0.8.dp)

                        VoiceConfigRow(
                            icon = Icons.Default.AutoAwesome,
                            title = "Voice model",
                            value = settings?.voiceModel ?: "Gemini 3.8 Live",
                            onClick = { showModelDialog = true }
                        )
                        HorizontalDivider(color = CosmicBorder.copy(alpha = 0.5f), thickness = 0.8.dp)

                        VoiceConfigRow(
                            icon = Icons.Default.Speed,
                            title = "Speech speed",
                            value = "${String.format("%.2f", settings?.speechSpeed ?: 0.85f)}x",
                            onClick = { showSpeedDialog = true }
                        )
                        HorizontalDivider(color = CosmicBorder.copy(alpha = 0.5f), thickness = 0.8.dp)

                        VoiceConfigRow(
                            icon = Icons.Default.VolumeUp,
                            title = "Voice volume",
                            value = "${settings?.voiceVolume ?: 65}%",
                            onClick = { showVolumeDialog = true }
                        )
                    }
                }
            }

            // Group 3: Listening
            item {
                Text(
                    text = "Listening",
                    color = LiaTextMuted,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                )
            }

            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = CosmicSurfaceCard),
                    shape = RoundedCornerShape(18.dp),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(CosmicBorder, LiaViolet.copy(alpha = 0.2f)))),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(LiaGold.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Hearing,
                                    contentDescription = "Listening",
                                    tint = LiaGold,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text(
                                    text = "Listen when the app opens",
                                    color = LiaTextPrimary,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Starts listening as soon as you open LIA",
                                    color = LiaTextSecondary,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        Switch(
                            checked = settings?.listenOnAppOpen ?: true,
                            onCheckedChange = { viewModel.updateListenOnAppOpen(it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.Black,
                                checkedTrackColor = LiaGold,
                                uncheckedThumbColor = LiaTextMuted,
                                uncheckedTrackColor = CosmicSurfaceElevated
                            )
                        )
                    }
                }
            }
        }
    }

    // 1. Voice Selection Dialog (Screenshot 3 & 7)
    if (showVoiceDialog) {
        BasicAlertDialog(onDismissRequest = { showVoiceDialog = false }) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2138)),
                shape = RoundedCornerShape(24.dp),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(CosmicBorder, LiaViolet.copy(alpha = 0.3f)))),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Voice",
                        color = LiaTextPrimary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Tap play to hear each voice, then pick your favourite.",
                        color = LiaTextSecondary,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(340.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(allVoices) { voice ->
                            val isSelected = settings?.voiceName == voice.name
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSelected) LiaGold.copy(alpha = 0.15f) else Color.Transparent)
                                    .clickable {
                                        viewModel.updateVoice(voice.name, voice.description)
                                    }
                                    .padding(vertical = 8.dp, horizontal = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(38.dp)
                                            .clip(CircleShape)
                                            .background(CosmicSurfaceElevated)
                                            .clickable { viewModel.previewVoice(voice.name) },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.PlayArrow,
                                            contentDescription = "Preview",
                                            tint = LiaGold,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = voice.name,
                                            color = if (isSelected) LiaGold else LiaTextPrimary,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Text(
                                            text = voice.description,
                                            color = LiaTextMuted,
                                            fontSize = 12.sp
                                        )
                                    }
                                }

                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Active",
                                        tint = LiaGold,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { showVoiceDialog = false }) {
                            Text("Cancel", color = LiaTextSecondary, fontSize = 14.sp)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        TextButton(onClick = { showVoiceDialog = false }) {
                            Text("Use ${settings?.voiceName ?: "Kore"}", color = LiaGold, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    // 2. Speech Speed Dialog (Screenshot 1 & 14)
    if (showSpeedDialog) {
        var tempSpeed by remember { mutableFloatStateOf(settings?.speechSpeed ?: 0.85f) }
        BasicAlertDialog(onDismissRequest = { showSpeedDialog = false }) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2138)),
                shape = RoundedCornerShape(24.dp),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(CosmicBorder, LiaGold.copy(alpha = 0.3f)))),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Column(modifier = Modifier.padding(22.dp)) {
                    Text(
                        text = "Speech speed",
                        color = LiaTextPrimary,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "${String.format("%.2f", tempSpeed)}x",
                        color = LiaTextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Slider(
                        value = tempSpeed,
                        onValueChange = { tempSpeed = it },
                        valueRange = 0.5f..1.5f,
                        colors = SliderDefaults.colors(
                            thumbColor = LiaGold,
                            activeTrackColor = LiaGold,
                            inactiveTrackColor = CosmicBorder
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { showSpeedDialog = false }) {
                            Text("Cancel", color = LiaTextSecondary, fontSize = 14.sp)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        TextButton(
                            onClick = {
                                viewModel.updateSpeechSpeed(tempSpeed)
                                showSpeedDialog = false
                            }
                        ) {
                            Text("Save", color = LiaGold, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    // 3. Voice Model Dialog (Screenshot 13)
    if (showModelDialog) {
        val models = listOf(
            "Gemini 3.8 Live",
            "Gemini 3.1 Flash Live (preview)",
            "Gemini 2.5 Flash Native Audio (Dec 2025)"
        )
        BasicAlertDialog(onDismissRequest = { showModelDialog = false }) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2138)),
                shape = RoundedCornerShape(24.dp),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(CosmicBorder, LiaViolet.copy(alpha = 0.3f)))),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Column(modifier = Modifier.padding(22.dp)) {
                    Text(
                        text = "Voice model",
                        color = LiaTextPrimary,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    models.forEach { modelName ->
                        val isSelected = settings?.voiceModel == modelName
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .clickable {
                                    viewModel.updateVoiceModel(modelName)
                                }
                                .padding(vertical = 12.dp, horizontal = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = modelName,
                                color = if (isSelected) LiaGold else LiaTextPrimary,
                                fontSize = 15.sp,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                modifier = Modifier.weight(1f)
                            )
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = LiaGold,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { showModelDialog = false }) {
                            Text("Close", color = LiaGold, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun VoiceConfigRow(
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
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
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
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = value,
                    color = LiaTextSecondary,
                    fontSize = 12.sp
                )
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
