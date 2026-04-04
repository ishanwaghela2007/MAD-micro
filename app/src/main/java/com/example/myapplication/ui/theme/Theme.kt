package com.example.myapplication.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = VitalityGreen,
    secondary = VitalityGreenDark,
    tertiary = VitalityTextSecondary,
    background = VitalityBackground,
    surface = VitalitySurface,
    onPrimary = Color.Black,
    onSecondary = Color.White,
    onBackground = VitalityTextPrimary,
    onSurface = VitalityTextPrimary,
    error = VitalityError
)

private val LightColorScheme = lightColorScheme(
    primary = VitalityGreenDark,
    secondary = VitalityGreen,
    tertiary = Color(0xFF4CAF50),
    background = Color.White,
    surface = Color(0xFFF5F5F5),
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    error = Color(0xFFB00020)
)

@Composable
fun VitalityFlowTheme(
    darkTheme: Boolean = true, // Force dark theme by default for the premium look
    dynamicColor: Boolean = false, // Disable dynamic colors to keep brand consistency
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


    MaterialTheme(
      colorScheme = colorScheme,
      typography = Typography,
      content = content
    )
}