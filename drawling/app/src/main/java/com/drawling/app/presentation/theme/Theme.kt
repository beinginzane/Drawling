package com.drawling.app.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Drawling Palette — warm cream + deep rose + soft ink
val RosePrimary = Color(0xFFE8637A)
val RoseLight = Color(0xFFF4A0AE)
val RoseDark = Color(0xFFB33D52)
val CreamBackground = Color(0xFFFFF8F4)
val InkSurface = Color(0xFF1C1B1F)
val InkOnSurface = Color(0xFF1C1B1F)
val SoftGrey = Color(0xFFEDE8E3)
val MutedText = Color(0xFF8E8E93)

private val LightColorScheme = lightColorScheme(
    primary = RosePrimary,
    onPrimary = Color.White,
    primaryContainer = RoseLight,
    onPrimaryContainer = RoseDark,
    secondary = Color(0xFF9B7FA6),
    onSecondary = Color.White,
    background = CreamBackground,
    onBackground = InkOnSurface,
    surface = Color.White,
    onSurface = InkOnSurface,
    surfaceVariant = SoftGrey,
    onSurfaceVariant = MutedText,
    error = Color(0xFFB3261E),
    outline = Color(0xFFCAC4D0)
)

private val DarkColorScheme = darkColorScheme(
    primary = RoseLight,
    onPrimary = RoseDark,
    primaryContainer = RoseDark,
    onPrimaryContainer = RoseLight,
    secondary = Color(0xFFCFB1DB),
    onSecondary = Color(0xFF3C1F4A),
    background = Color(0xFF141218),
    onBackground = Color(0xFFEBE0F0),
    surface = Color(0xFF1C1B1F),
    onSurface = Color(0xFFEBE0F0),
    surfaceVariant = Color(0xFF2B2930),
    onSurfaceVariant = Color(0xFFCAC4D0),
    error = Color(0xFFF2B8B5),
    outline = Color(0xFF938F99)
)

@Composable
fun DrawlingTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = DrawlingTypography,
        content = content
    )
}
