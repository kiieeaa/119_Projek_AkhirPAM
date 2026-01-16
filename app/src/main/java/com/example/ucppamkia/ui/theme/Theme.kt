package com.example.ucppamkia.ui.theme

import BlueAccent
import BlueDark
import BlueLight
import BluePrimary
import BlueSurface
import TextPrimary
import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Skema Warna Gelap (Tetap elegan dengan basis biru tua)
private val DarkColorScheme = darkColorScheme(
    primary = BluePrimary,
    secondary = BlueAccent,
    tertiary = BlueLight,
    background = Color(0xFF121212),
    surface = Color(0xFF1E2A38), // Dark blue surface
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = TextPrimary,
    onBackground = BlueLight,
    onSurface = BlueLight
)

// Skema Warna Terang (Dominan Biru Bersih)
private val LightColorScheme = lightColorScheme(
    primary = BluePrimary,
    secondary = BlueAccent,
    tertiary = BlueDark,
    background = BlueSurface,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = TextPrimary,
    onSurface = TextPrimary
)

@Composable
fun UcppamkiaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Matikan agar tema biru kita konsisten
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Status bar biru gelap
            window.statusBarColor = BlueDark.toArgb()
            // Icon status bar putih (false)
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}