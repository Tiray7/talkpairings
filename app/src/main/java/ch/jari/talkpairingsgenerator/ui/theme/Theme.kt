package ch.jari.talkpairingsgenerator.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


private val darkColorPalette = darkColorScheme(
    primary = Green700,
    onPrimary = Color.White,
    primaryContainer = Green500,
    onPrimaryContainer = Color.Black,
    inversePrimary = Color.White,
    secondary = Orange200,
    onSecondary = Color.Black,
    secondaryContainer = Orange200,
    onSecondaryContainer = Color.Black,
    tertiary = Gray,
    onTertiary = Color.Black,
    tertiaryContainer = LightGray,
    onTertiaryContainer = Color.Black,
    background = Color.Black,
    onBackground = Color.White,
    surface = DarkGray,
    onSurface = Color.White,
    surfaceVariant = Gray,
    onSurfaceVariant = Color.White,
    surfaceTint = DarkGray,
    inverseSurface = Color.White,
    inverseOnSurface = Color.Black,
    error = ErrorRed,
    onError = Color.White,
    errorContainer = ErrorRed,
    onErrorContainer = Color.White,
    outline = Gray,
    outlineVariant = LightGray,
    scrim = Scrim
)

private val lightColorPalette = lightColorScheme(
    primary = Green500,
    onPrimary = Color.White,
    primaryContainer = Green200,
    onPrimaryContainer = Color.Black,
    inversePrimary = Color.White,
    secondary = Orange200,
    onSecondary = Color.Black,
    secondaryContainer = Orange200,
    onSecondaryContainer = Color.Black,
    tertiary = LightGray,
    onTertiary = Color.Black,
    tertiaryContainer = Gray,
    onTertiaryContainer = Color.White,
    background = Color.White,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black,
    surfaceVariant = LightGray,
    onSurfaceVariant = Color.Black,
    surfaceTint = Gray,
    inverseSurface = Color.Black,
    inverseOnSurface = Color.White,
    error = ErrorRed,
    onError = Color.White,
    errorContainer = ErrorRed,
    onErrorContainer = Color.White,
    outline = Gray,
    outlineVariant = DarkGray,
    scrim = Scrim
)

@Composable
fun TalkPairingsGeneratorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) {
        darkColorPalette
    } else {
        lightColorPalette
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}