package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Brush
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ConversationsHistoryScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val messages by viewModel.chatMessages.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    val assistantMessages = messages.filter { it.sender == "ASSISTANT" }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CosmicBackground)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(18.dp))
        Text(
            text = "Conversations",
            color = LiaTextPrimary,
            fontSize = 28.sp,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search conversations", color = LiaTextMuted, fontSize = 14.sp) },
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 14.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            val filtered = assistantMessages.filter {
                if (searchQuery.isBlank()) true else it.text.contains(searchQuery, ignoreCase = true)
            }

            items(filtered.reversed(), key = { it.id }) { msg ->
                val timeStr = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(msg.timestamp)).lowercase()
                val snippetTitle = if (msg.text.contains("YouTube")) {
                    "Voice - Hmm? Boss,"
                } else if (msg.text.contains("WhatsApp")) {
                    "Voice - WhatsApp"
                } else if (msg.text.contains("finally here") || msg.text.contains("missed")) {
                    "hii"
                } else {
                    "Voice - Lia response"
                }

                Card(
                    colors = CardDefaults.cardColors(containerColor = CosmicSurfaceCard),
                    shape = RoundedCornerShape(16.dp),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(CosmicBorder, LiaViolet.copy(alpha = 0.2f)))),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.speakMessage(msg.text)
                            viewModel.navigateTo(AppNavDestination.CHAT)
                        }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = snippetTitle,
                                color = LiaTextPrimary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = timeStr,
                                color = LiaTextMuted,
                                fontSize = 11.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = msg.text,
                            color = LiaTextSecondary,
                            fontSize = 13.sp,
                            maxLines = 2,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }
    }
}
