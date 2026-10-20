package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CosmicNebulaOrb(
    modifier: Modifier = Modifier,
    size: Dp = 230.dp,
    isSpeaking: Boolean = false,
    audioLevel: Float = 0.15f,
    onClick: () -> Unit = {}
) {
    val infiniteTransition = rememberInfiniteTransition(label = "NebulaOrbTransition")

    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.94f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = if (isSpeaking) 900 else 2800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "NebulaPulse"
    )

    val cloudRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "NebulaCloudRotation"
    )

    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .size(size)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .testTag("cosmic_nebula_orb"),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val center = Offset(this.size.width / 2f, this.size.height / 2f)
            val maxRadius = this.size.width / 2f
            val reactiveBoost = if (isSpeaking) audioLevel * 0.25f else 0f
            val orbRadius = (maxRadius * 0.85f * (pulseScale + reactiveBoost)).coerceIn(10f, maxRadius)

            // 1. Ambient Nebula Glow Halo
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF7C3AED).copy(alpha = if (isSpeaking) 0.5f else 0.35f),
                        Color(0xFF4C1D95).copy(alpha = 0.15f),
                        Color.Transparent
                    ),
                    center = center,
                    radius = maxRadius
                ),
                radius = maxRadius,
                center = center
            )

            // 2. Wave rings when speaking
            if (isSpeaking) {
                for (i in 1..3) {
                    val waveR = orbRadius + (maxRadius - orbRadius) * (i / 3f) * audioLevel
                    drawCircle(
                        color = Color(0xFFA78BFA).copy(alpha = (0.4f / i) * audioLevel),
                        radius = waveR.coerceAtMost(maxRadius),
                        center = center,
                        style = Stroke(width = 2.dp.toPx())
                    )
                }
            }

            // 3. Deep 3D Cosmic Orb Base
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF6D28D9), // Bright core
                        Color(0xFF4C1D95), // Deep purple
                        Color(0xFF2E1065), // Dark violet
                        Color(0xFF0F0728)  // Shadow limb
                    ),
                    center = center - Offset(orbRadius * 0.2f, orbRadius * 0.25f),
                    radius = orbRadius
                ),
                radius = orbRadius,
                center = center
            )

            // 4. Swirling Nebula Clouds inside sphere
            for (i in 0..4) {
                val rad = Math.toRadians((cloudRotation + i * 72).toDouble())
                val cloudOffset = Offset(
                    center.x + (orbRadius * 0.35f * cos(rad)).toFloat(),
                    center.y + (orbRadius * 0.25f * sin(rad)).toFloat()
                )
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFD946EF).copy(alpha = 0.35f),
                            Color(0xFF8B5CF6).copy(alpha = 0.18f),
                            Color.Transparent
                        ),
                        center = cloudOffset,
                        radius = orbRadius * 0.55f
                    ),
                    radius = orbRadius * 0.55f,
                    center = cloudOffset
                )
            }

            // 5. Bright celestial shine / specular highlight (top left)
            val highlightCenter = center - Offset(orbRadius * 0.38f, orbRadius * 0.42f)
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.90f),
                        Color(0xFFE9D5FF).copy(alpha = 0.45f),
                        Color.Transparent
                    ),
                    center = highlightCenter,
                    radius = orbRadius * 0.32f
                ),
                radius = orbRadius * 0.32f,
                center = highlightCenter
            )

            // 6. Subtle glowing rim stroke
            drawCircle(
                brush = Brush.sweepGradient(
                    listOf(
                        Color(0xFFC084FC).copy(alpha = 0.8f),
                        Color(0xFF7C3AED).copy(alpha = 0.3f),
                        Color(0xFFC084FC).copy(alpha = 0.8f)
                    ),
                    center = center
                ),
                radius = orbRadius,
                center = center,
                style = Stroke(width = 1.5.dp.toPx())
            )
        }
    }
}
