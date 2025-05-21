package com.example.calendapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = AccentColor,
    secondary = AccentColor,
    tertiary = AccentColor
)

private val LightColorScheme = lightColorScheme(
    primary = AccentColor,
    secondary = AccentColor,
    tertiary = AccentColor,
    background = BackgroundColor,
    surface = InputBackground,
    onBackground = White,
    onSurface = White
)

@Composable
fun CalendappTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
