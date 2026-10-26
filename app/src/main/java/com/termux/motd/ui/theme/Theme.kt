package com.termux.motd.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = NeonCyan,
    onPrimary = Color.Black,
    primaryContainer = NeonCyanDark,
    onPrimaryContainer = Color.White,
    background = DarkBackground,
    onBackground = TextWhite,
    surface = DarkSurface,
    onSurface = TextWhite,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextMuted
)

@Composable
fun ModernTermuxMotdTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content
    )
}
