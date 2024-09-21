package com.example.greenspots.ui.theme
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Color(0xFF1EB980),
    primaryVariant = Color(0xFF045D56),
    secondary = Color(0xFF03DAC5)
)

private val LightColorPalette = lightColors(
    primary = Color(0xFF1EB980),
    primaryVariant = Color(0xFF045D56),
    secondary = Color(0xFF03DAC5)
)

@Composable
fun GreenSpotsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
