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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.ChatMessageEntity
import com.example.tools.DeviceTools
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ChatScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val settings by viewModel.settings.collectAsState()
    val messages by viewModel.chatMessages.collectAsState()
    val isGenerating by viewModel.isGenerating.collectAsState()
    val isSpeaking by viewModel.voiceEngine.isSpeaking.collectAsState()

    var textInput by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    val speechLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val spoken = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.firstOrNull()
            if (!spoken.isNullOrBlank()) {
                viewModel.sendMessage(spoken)
            }
        }
    }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CosmicBackground)
    ) {
        // Chat Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(LiaViolet.copy(alpha = 0.25f))
                        .border(1.dp, LiaGold.copy(alpha = 0.6f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "L",
                        color = LiaGold,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = settings?.assistantName ?: "Lia",
                        color = LiaTextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(StatusGreen)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "${settings?.personality ?: "GF Mode"} • Ready",
                            color = LiaTextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (isSpeaking) {
                    IconButton(
                        onClick = { viewModel.stopSpeaking() },
                        modifier = Modifier.testTag("stop_speaking_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Stop,
                            contentDescription = "Stop Speaking",
                            tint = LiaGold
                        )
                    }
                }
                IconButton(
                    onClick = { viewModel.clearChat() },
                    modifier = Modifier.testTag("clear_chat_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteSweep,
                        contentDescription = "Clear Chat",
                        tint = LiaTextSecondary
                    )
                }
            }
        }

        // Suggestions row
        val suggestions = listOf(
            "Kaisi ho Lia?",
            "YouTube kholo aur gaana chalao",
            "Mummy ko WhatsApp karo",
            "Ek romantic baat bolo na",
            "Torch on karo",
            "Papa ko call lagao"
        )
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(suggestions) { prompt ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(CosmicSurfaceElevated)
                        .border(1.dp, CosmicBorder, RoundedCornerShape(16.dp))
                        .clickable { viewModel.sendMessage(prompt) }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(text = prompt, color = LiaTextSecondary, fontSize = 12.sp)
                }
            }
        }

        // Message List
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 10.dp, bottom = 10.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(messages, key = { it.id }) { msg ->
                MessageBubble(
                    message = msg,
                    assistantName = settings?.assistantName ?: "Lia",
                    onSpeak = { viewModel.speakMessage(msg.text) },
                    onCopy = { DeviceTools.copyToClipboard(context, msg.text) },
                    onToggleFavorite = { viewModel.toggleFavorite(msg.id) }
                )
            }

            if (isGenerating) {
                item {
                    Row(
                        modifier = Modifier.padding(start = 8.dp, top = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            color = LiaGold,
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${settings?.assistantName ?: "Lia"} is thinking...",
                            color = LiaTextMuted,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // Bottom Input Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .padding(bottom = 72.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = textInput,
                onValueChange = { textInput = it },
                placeholder = {
                    Text(
                        text = "Message ${settings?.assistantName ?: "Lia"}...",
                        color = LiaTextMuted,
                        fontSize = 14.sp
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = CosmicSurfaceElevated,
                    unfocusedContainerColor = CosmicSurfaceElevated,
                    focusedBorderColor = LiaGold,
                    unfocusedBorderColor = CosmicBorder,
                    focusedTextColor = LiaTextPrimary,
                    unfocusedTextColor = LiaTextPrimary
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(
                    onSend = {
                        if (textInput.isNotBlank()) {
                            viewModel.sendMessage(textInput)
                            textInput = ""
                        }
                    }
                ),
                modifier = Modifier
                    .weight(1f)
                    .testTag("chat_input_field")
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Mic speech-to-text
            IconButton(
                onClick = {
                    try {
                        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                        }
                        speechLauncher.launch(intent)
                    } catch (_: Exception) {}
                },
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(CosmicSurfaceElevated)
                    .border(1.dp, CosmicBorder, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = "Voice input",
                    tint = LiaGold
                )
            }

            Spacer(modifier = Modifier.width(6.dp))

            // Send button
            IconButton(
                onClick = {
                    if (textInput.isNotBlank()) {
                        viewModel.sendMessage(textInput)
                        textInput = ""
                    }
                },
                enabled = textInput.isNotBlank(),
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(if (textInput.isNotBlank()) LiaGold else CosmicSurfaceElevated)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send",
                    tint = if (textInput.isNotBlank()) Color.Black else LiaTextMuted
                )
            }
        }
    }
}

@Composable
fun MessageBubble(
    message: ChatMessageEntity,
    assistantName: String,
    onSpeak: () -> Unit,
    onCopy: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    val isUser = message.sender == "USER"
    val timeFormatted = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(message.timestamp))

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        // Sender tag
        Text(
            text = if (isUser) "You • $timeFormatted" else "$assistantName • $timeFormatted",
            color = LiaTextMuted,
            fontSize = 11.sp,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )

        Card(
            colors = CardDefaults.cardColors(
                containerColor = if (isUser) LiaGold.copy(alpha = 0.2f) else CosmicSurfaceCard
            ),
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isUser) 16.dp else 4.dp,
                bottomEnd = if (isUser) 4.dp else 16.dp
            ),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = Brush.linearGradient(
                    listOf(
                        if (isUser) LiaGold.copy(alpha = 0.7f) else CosmicBorder,
                        if (isUser) LiaGold.copy(alpha = 0.3f) else CosmicBorder
                    )
                )
            ),
            modifier = Modifier.fillMaxWidth(0.88f)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = message.text,
                    color = LiaTextPrimary,
                    fontSize = 15.sp,
                    lineHeight = 22.sp
                )

                if (!isUser) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onSpeak, modifier = Modifier.size(28.dp)) {
                            Icon(
                                imageVector = Icons.Default.VolumeUp,
                                contentDescription = "Hear Voice",
                                tint = LiaGold,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        IconButton(onClick = onCopy, modifier = Modifier.size(28.dp)) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy message",
                                tint = LiaTextSecondary,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                        IconButton(onClick = onToggleFavorite, modifier = Modifier.size(28.dp)) {
                            Icon(
                                imageVector = if (message.isFavorite) Icons.Default.Star else Icons.Default.StarBorder,
                                contentDescription = "Favorite",
                                tint = if (message.isFavorite) LiaGold else LiaTextSecondary,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
