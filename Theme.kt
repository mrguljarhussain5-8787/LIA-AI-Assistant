package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LiaDarkColorScheme = darkColorScheme(
    primary = LiaGold,
    onPrimary = Color.Black,
    primaryContainer = LiaGoldDark,
    onPrimaryContainer = Color.White,
    secondary = LiaViolet,
    onSecondary = Color.White,
    secondaryContainer = CosmicSurfaceElevated,
    onSecondaryContainer = LiaTextPrimary,
    tertiary = LiaVioletGlow,
    onTertiary = Color.White,
    background = CosmicBackground,
    onBackground = LiaTextPrimary,
    surface = CosmicSurface,
    onSurface = LiaTextPrimary,
    surfaceVariant = CosmicSurfaceCard,
    onSurfaceVariant = LiaTextSecondary,
    outline = CosmicBorder
)

@Composable
fun MyApplicationTheme(
    auraTheme: String = "CRIMSON",
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LiaDarkColorScheme,
        typography = Typography,
        content = content
    )
}
