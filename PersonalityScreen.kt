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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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

data class PersonalityOption(
    val code: String,
    val title: String,
    val description: String,
    val icon: ImageVector,
    val isGf: Boolean = false
)

@Composable
fun PersonalityScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val settings by viewModel.settings.collectAsState()

    val options = listOf(
        PersonalityOption("NORMAL", "Normal", "Balanced and neutral responses.", Icons.Default.Person),
        PersonalityOption("FRIENDLY", "Friendly", "Warm, casual, and encouraging.", Icons.Default.LightMode),
        PersonalityOption("PROFESSIONAL", "Professional", "Concise, formal, and precise.", Icons.Default.Work),
        PersonalityOption("FUNNY", "Funny", "Light-hearted and humorous", Icons.Default.Mood),
        PersonalityOption("COMPANION", "Companion", "Warm, caring and playful", Icons.Default.Favorite),
        PersonalityOption("GF_MODE", "GF Mode", "Extra Romantic, Emotional, and deeply attached to Boss.", Icons.Default.Favorite, isGf = true),
        PersonalityOption("TEACHER", "Teacher", "Clear explanations and learning-focused", Icons.Default.School),
        PersonalityOption("DEVELOPER", "Developer", "Technical and precise", Icons.Default.Code),
        PersonalityOption("HUNGRY", "Hungry", "Food-obsessed and always craving something.", Icons.Default.Fastfood)
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
            IconButton(onClick = { viewModel.navigateTo(AppNavDestination.VOICE_LANGUAGE) }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = LiaTextPrimary
                )
            }
            Text(
                text = "Personality",
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
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(options) { item ->
                val isSelected = settings?.personality == item.code

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected && item.isGf) CosmicSurfaceElevated else CosmicSurfaceCard
                    ),
                    shape = RoundedCornerShape(16.dp),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.linearGradient(
                            if (isSelected) listOf(LiaGold, LiaGold) else listOf(CosmicBorder, LiaViolet.copy(alpha = 0.15f))
                        )
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.updatePersonality(item.code)
                            viewModel.testRealisticVoice()
                        }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isSelected) LiaGold.copy(alpha = 0.2f) else CosmicSurfaceElevated
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.title,
                                    tint = if (isSelected) LiaGold else LiaTextSecondary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column {
                                Text(
                                    text = item.title,
                                    color = if (isSelected) LiaGold else LiaTextPrimary,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = item.description,
                                    color = LiaTextSecondary,
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp
                                )
                            }
                        }

                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(LiaGold),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = Color.Black,
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
