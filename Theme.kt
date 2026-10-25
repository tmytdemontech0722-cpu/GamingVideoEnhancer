package com.example.viralgamingenhancer.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val NeonBlue = Color(0xFF29B6FF)
val NeonPurple = Color(0xFF9B6BFF)

private val Scheme = darkColorScheme(
    primary = NeonBlue,
    onPrimary = Color.Black,
    secondary = NeonPurple,
    background = Color(0xFF0B0B14),
    surface = Color(0xFF151526),
    onBackground = Color(0xFFEAEAF5),
    onSurface = Color(0xFFEAEAF5),
)

@Composable
fun ViralTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = Scheme, content = content)
}
