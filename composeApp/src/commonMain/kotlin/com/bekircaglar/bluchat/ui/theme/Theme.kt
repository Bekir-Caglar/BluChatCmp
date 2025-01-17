package com.bekircaglar.bluchat.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Blue,
    secondary = Yellow,
    tertiary = Orange,
    background = Color.White,
    surface = BlueLight,
    onPrimary = LightText,
    onSecondary = DarkBlue,
    onBackground = DarkText,
    onSurface = DarkText
)

private val DarkColorScheme = darkColorScheme(
    primary = BlueLight,
    secondary = Orange,
    tertiary = Yellow,
    background = DarkBackground,
    surface = DarkBlue,
    onPrimary = DarkText,
    onSecondary = Blue,
    onBackground = LightText,
    onSurface = LightText
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}