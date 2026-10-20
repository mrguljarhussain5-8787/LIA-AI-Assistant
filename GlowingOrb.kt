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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun GlowingOrb(
    modifier: Modifier = Modifier,
    baseSize: Dp = 190.dp,
    orbType: String = "NEON", // CLASSIC, ENERGY, NEON, PULSAR
    primaryColor: Color = Color(0xFFFF2A55),
    secondaryColor: Color = Color(0xFFFF6B8B),
    isSpeaking: Boolean = false,
    audioVisualizerLevel: Float = 0.15f,
    onClick: () -> Unit = {}
) {
    val infiniteTransition = rememberInfiniteTransition(label = "OrbTransition")

    // Rotation for outer rings
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = if (isSpeaking) 4000 else 9000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "OrbRotation"
    )

    // Reverse rotation for inner rings
    val reverseRotation by infiniteTransition.animateFloat(
        initialValue = 360f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = if (isSpeaking) 5000 else 12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "OrbReverseRotation"
    )

    // Breathing glow animation
    val breathingPulse by infiniteTransition.animateFloat(
        initialValue = 0.88f,
        targetValue = 1.12f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = if (isSpeaking) 800 else 2400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "OrbBreathing"
    )

    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .size(baseSize)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .testTag("glowing_orb_center"),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(baseSize)) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val maxRadius = size.width / 2f
            val reactiveBoost = if (isSpeaking) audioVisualizerLevel * 0.35f else 0f
            val currentScale = (breathingPulse + reactiveBoost).coerceIn(0.7f, 1.4f)
            val coreRadius = (maxRadius * 0.44f) * currentScale

            // 1. Ambient outer diffuse glow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        primaryColor.copy(alpha = if (isSpeaking) 0.55f else 0.32f),
                        secondaryColor.copy(alpha = 0.15f),
                        Color.Transparent
                    ),
                    center = center,
                    radius = maxRadius * 0.98f
                ),
                radius = maxRadius * 0.98f,
                center = center
            )

            // 2. Animated Audio Spectrum / Waveform Ripples (when speaking)
            if (isSpeaking || audioVisualizerLevel > 0.2f) {
                for (i in 1..4) {
                    val waveRadius = coreRadius + (maxRadius - coreRadius) * (i / 4f) * audioVisualizerLevel
                    drawCircle(
                        color = secondaryColor.copy(alpha = (0.35f / i) * audioVisualizerLevel),
                        radius = waveRadius.coerceAtMost(maxRadius),
                        center = center,
                        style = Stroke(width = 2.5.dp.toPx())
                    )
                }
            }

            // 3. Orbiting energy rings (based on orbType)
            when (orbType) {
                "ENERGY" -> {
                    // Intricate tangled neon curves like Screenshot 3 (Neon Green/Energy orb)
                    for (step in 0..5) {
                        val angleOffset = Math.toRadians((rotationAngle + step * 60).toDouble())
                        val rX = maxRadius * 0.78f
                        val rY = maxRadius * 0.52f
                        val pX = (center.x + rX * cos(angleOffset)).toFloat()
                        val pY = (center.y + rY * sin(angleOffset)).toFloat()
                        drawCircle(
                            color = primaryColor.copy(alpha = 0.7f),
                            radius = 4.dp.toPx(),
                            center = Offset(pX, pY)
                        )
                    }
                    drawCircle(
                        brush = Brush.sweepGradient(
                            listOf(primaryColor, secondaryColor, Color.Transparent, primaryColor),
                            center = center
                        ),
                        radius = maxRadius * 0.75f,
                        center = center,
                        style = Stroke(
                            width = 2.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(30f, 20f), rotationAngle)
                        )
                    )
                }
                "PULSAR" -> {
                    // Geometric dashed radar rings
                    drawCircle(
                        color = primaryColor.copy(alpha = 0.6f),
                        radius = maxRadius * 0.88f,
                        center = center,
                        style = Stroke(
                            width = 1.5.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(14f, 18f), rotationAngle)
                        )
                    )
                    drawCircle(
                        color = secondaryColor.copy(alpha = 0.75f),
                        radius = maxRadius * 0.68f,
                        center = center,
                        style = Stroke(
                            width = 2.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(24f, 16f), reverseRotation)
                        )
                    )
                }
                else -> {
                    // NEON & CLASSIC: Multi-layered concentric orbital arcs like Screenshot 1
                    drawCircle(
                        brush = Brush.sweepGradient(
                            listOf(primaryColor.copy(alpha = 0.8f), secondaryColor, primaryColor.copy(alpha = 0.2f)),
                            center = center
                        ),
                        radius = maxRadius * 0.82f,
                        center = center,
                        style = Stroke(
                            width = 2.5.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(45f, 25f), rotationAngle)
                        )
                    )
                    drawCircle(
                        color = secondaryColor.copy(alpha = 0.65f),
                        radius = maxRadius * 0.65f,
                        center = center,
                        style = Stroke(
                            width = 1.8.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(35f, 30f), reverseRotation)
                        )
                    )
                }
            }

            // 4. Solid Bright Core Sphere
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.95f),
                        secondaryColor,
                        primaryColor,
                        primaryColor.copy(alpha = 0.85f)
                    ),
                    center = center - Offset(coreRadius * 0.25f, coreRadius * 0.25f),
                    radius = coreRadius
                ),
                radius = coreRadius,
                center = center
            )

            // Inner gloss highlight
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color.White.copy(alpha = 0.85f), Color.Transparent),
                    center = center - Offset(coreRadius * 0.35f, coreRadius * 0.35f),
                    radius = coreRadius * 0.45f
                ),
                radius = coreRadius * 0.45f,
                center = center - Offset(coreRadius * 0.35f, coreRadius * 0.35f)
            )
        }

        // Center Icon (Microphone or Audio Wave)
        Icon(
            imageVector = if (isSpeaking) Icons.Default.VolumeUp else Icons.Default.Mic,
            contentDescription = "Assistant Voice Trigger",
            tint = Color.White,
            modifier = Modifier.size(baseSize * 0.22f)
        )
    }
}
