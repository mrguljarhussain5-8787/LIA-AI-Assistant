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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

data class QuickActionItem(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val iconBgColor: Color,
    val prompt: String
)

@Composable
fun QuickActionsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val actions = listOf(
        QuickActionItem("Open an app", "\"YouTube kholo\"", Icons.Default.Apps, Color(0xFFD97706), "YouTube kholo aur lo-fi chalao"),
        QuickActionItem("Call someone", "\"Mummy ko call karo\"", Icons.Default.Phone, Color(0xFF0D9488), "Papa ko call karo"),
        QuickActionItem("Send a message", "\"Papa ko WhatsApp karo\"", Icons.Default.Sms, Color(0xFFE11D48), "Mummy ko WhatsApp karo ki main late hoon"),
        QuickActionItem("Build a website", "\"ek cafe ki website banao\"", Icons.Default.Language, Color(0xFFEA580C), "ek cafe ki website banao"),
        QuickActionItem("Sketch a photo", "\"drawing banao\"", Icons.Default.Brush, Color(0xFF9333EA), "drawing banao"),
        QuickActionItem("Talk to LIA", "Start a voice conversation", Icons.Default.Mic, LiaGold, "Voice conversation start"),
        QuickActionItem("Ask anything", "Start a text conversation", Icons.AutoMirrored.Filled.Chat, Color(0xFF0284C7), "Tell me something interesting, Lia!")
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
            IconButton(onClick = { viewModel.navigateTo(AppNavDestination.HOME) }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = LiaTextPrimary
                )
            }
            Text(
                text = "Quick actions",
                color = LiaTextPrimary,
                fontSize = 26.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Normal
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 8.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item(span = { GridItemSpan(2) }) {
                Text(
                    text = "Tap one to start. Say it the way you would to a friend, in Hindi, English or both.",
                    color = LiaTextSecondary,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
            }

            items(actions) { action ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = CosmicSurfaceCard),
                    shape = RoundedCornerShape(18.dp),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(CosmicBorder, LiaViolet.copy(alpha = 0.2f)))),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(138.dp)
                        .clickable {
                            if (action.title == "Talk to LIA") {
                                viewModel.testRealisticVoice()
                            } else {
                                viewModel.sendMessage(action.prompt)
                                viewModel.navigateTo(AppNavDestination.CHAT)
                            }
                        }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(action.iconBgColor.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = action.icon,
                                contentDescription = action.title,
                                tint = action.iconBgColor,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Column {
                            Text(
                                text = action.title,
                                color = LiaTextPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = action.subtitle,
                                color = LiaTextMuted,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
