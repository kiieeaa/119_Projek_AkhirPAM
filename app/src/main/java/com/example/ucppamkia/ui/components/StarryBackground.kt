package com.example.ucppamkia.ui.components

import ElectricBlue
import NeonBlue
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun StarryBackground(
    modifier: Modifier = Modifier,
    starCount: Int = 150,
    baseColor: Color = Color(0xFF0A1929) // Warna background
) {
    // Data bintang acak
    val stars = remember {
        List(starCount) {
            Star(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                size = Random.nextFloat() * 3f + 1f,
                twinkleSpeed = Random.nextFloat() * 2000f + 1000f,
                twinkleOffset = Random.nextFloat() * 1000f,
                color = when (Random.nextInt(10)) {
                    0, 1 -> NeonBlue.copy(alpha = 0.9f)
                    2 -> ElectricBlue.copy(alpha = 0.8f)
                    3 -> Color(0xFF64B5F6).copy(alpha = 0.7f)
                    else -> Color.White.copy(alpha = Random.nextFloat() * 0.5f + 0.5f)
                }
            )
        }
    }

    // Animasi kelap-kelip
    val infiniteTransition = rememberInfiniteTransition(label = "starTwinkle")
    val animationTime by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 10000f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "time"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        // Background
        drawRect(color = baseColor, size = size)

        // Gambar bintang
        stars.forEach { star ->
            val starX = star.x * size.width
            val starY = star.y * size.height

            // Opacity animasi
            val twinklePhase = (animationTime + star.twinkleOffset) / star.twinkleSpeed
            val opacity = (sin(twinklePhase * Math.PI * 2).toFloat() + 1f) / 2f

            drawStar(
                center = Offset(starX, starY),
                size = star.size,
                color = star.color.copy(alpha = star.color.alpha * opacity)
            )

            // Glow bintang besar
            if (star.size > 2.5f) {
                drawCircle(
                    color = star.color.copy(alpha = 0.1f * opacity),
                    radius = star.size * 3f,
                    center = Offset(starX, starY)
                )
            }
        }

        // Gambar bulan
        drawMoon()

        // Bintang jatuh
        if (animationTime.toInt() % 3000 < 100) {
            drawShootingStar(animationTime.toInt() % 3000 / 100f)
        }
    }
}

private fun DrawScope.drawMoon() {
    val moonRadius = 100f // Radius bulan
    val moonCenter = Offset(size.width * 0.85f, size.height * 0.15f)

    // Glow bulan
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                Color.White.copy(alpha = 0.5f),
                Color.White.copy(alpha = 0.1f),
                Color.Transparent
            ),
            center = moonCenter,
            radius = moonRadius * 2.5f
        ),
        center = moonCenter,
        radius = moonRadius * 2.5f
    )

    // Badan bulan
    drawCircle(
        color = Color(0xFFFFFFE0),
        radius = moonRadius,
        center = moonCenter
    )

    // Detail kawah
    drawCircle(
        color = Color.LightGray.copy(alpha = 0.3f),
        radius = moonRadius * 0.2f,
        center = Offset(moonCenter.x - 20f, moonCenter.y - 10f)
    )
    drawCircle(
        color = Color.LightGray.copy(alpha = 0.2f),
        radius = moonRadius * 0.15f,
        center = Offset(moonCenter.x + 30f, moonCenter.y + 40f)
    )
}

private data class Star(
    val x: Float,
    val y: Float,
    val size: Float,
    val twinkleSpeed: Float,
    val twinkleOffset: Float,
    val color: Color
)

private fun DrawScope.drawStar(
    center: Offset,
    size: Float,
    color: Color
) {
    // Bintang kecil
    if (size < 2f) {
        drawCircle(color = color, radius = size, center = center)
    } else {
        // Bintang besar
        val outerRadius = size

        drawLine(
            color = color,
            start = Offset(center.x - outerRadius, center.y),
            end = Offset(center.x + outerRadius, center.y),
            strokeWidth = size * 0.3f
        )

        drawLine(
            color = color,
            start = Offset(center.x, center.y - outerRadius),
            end = Offset(center.x, center.y + outerRadius),
            strokeWidth = size * 0.3f
        )

        // Titik tengah
        drawCircle(
            color = color,
            radius = size * 0.5f,
            center = center
        )
    }
}

private fun DrawScope.drawShootingStar(progress: Float) {
    val startX = size.width * 0.8f
    val startY = size.height * 0.2f
    val endX = startX - 200f * progress
    val endY = startY + 150f * progress

    val alpha = 1f - progress

    // Garis utama
    drawLine(
        color = Color.White.copy(alpha = alpha * 0.8f),
        start = Offset(startX, startY),
        end = Offset(endX, endY),
        strokeWidth = 2f
    )

    // Ekor cahaya
    drawLine(
        color = NeonBlue.copy(alpha = alpha * 0.5f),
        start = Offset(startX, startY),
        end = Offset(endX + 50f, endY - 30f),
        strokeWidth = 1.5f
    )
}
