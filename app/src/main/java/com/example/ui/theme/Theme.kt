package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = GoldAccent,
    onPrimary = DarkBackground,
    primaryContainer = EmeraldContainer,
    onPrimaryContainer = GoldLight,
    secondary = EmeraldLight,
    onSecondary = TextLightPrimary,
    secondaryContainer = DarkSurfaceVariant,
    onSecondaryContainer = GoldLight,
    tertiary = GoldLight,
    onTertiary = DarkBackground,
    background = DarkBackground,
    onBackground = TextLightPrimary,
    surface = DarkSurface,
    onSurface = TextLightPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextLightSecondary,
    outline = EmeraldLight.copy(alpha = 0.5f)
)

private val LightColorScheme = lightColorScheme(
    primary = EmeraldPrimary,
    onPrimary = CreamSurface,
    primaryContainer = CreamSurfaceVariant,
    onPrimaryContainer = EmeraldDark,
    secondary = GoldDark,
    onSecondary = CreamSurface,
    secondaryContainer = CreamSurfaceVariant,
    onSecondaryContainer = GoldDark,
    tertiary = GoldAccent,
    onTertiary = TextDarkPrimary,
    background = CreamBackground,
    onBackground = TextDarkPrimary,
    surface = CreamSurface,
    onSurface = TextDarkPrimary,
    surfaceVariant = CreamSurfaceVariant,
    onSurfaceVariant = TextDarkSecondary,
    outline = GoldAccent.copy(alpha = 0.4f)
)

@Composable
fun SurahRahmanTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
