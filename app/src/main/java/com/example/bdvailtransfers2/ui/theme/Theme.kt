package com.example.bdvailtransfers2.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = BDVailPrimary,
    onPrimary = BDVailOnPrimary,
    primaryContainer = BDVailPrimaryVariant,
    onPrimaryContainer = BDVailOnPrimary,

    secondary = BDVailSecondary,
    onSecondary = BDVailOnSecondary,
    secondaryContainer = BDVailSecondaryVariant,
    onSecondaryContainer = BDVailOnSecondary,

    background = BDVailBackground,
    onBackground = BDVailOnBackground,

    surface = BDVailSurface,
    onSurface = BDVailOnSurface,

    error = BDVailError,
    onError = BDVailOnError
)

private val LightColorScheme = lightColorScheme(
    primary = BDVailPrimary,
    onPrimary = BDVailOnPrimary,
    primaryContainer = BDVailPrimaryVariant,
    onPrimaryContainer = BDVailOnPrimary,

    secondary = BDVailSecondary,
    onSecondary = BDVailOnSecondary,
    secondaryContainer = BDVailSecondaryVariant,
    onSecondaryContainer = BDVailOnSecondary,

    background = BDVailBackground,
    onBackground = BDVailOnBackground,

    surface = BDVailSurface,
    onSurface = BDVailOnSurface,

    error = BDVailError,
    onError = BDVailOnError
)

@Composable
fun BDVailTransfersTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    colorSchemeOverride: ColorScheme? = null,
    content: @Composable () -> Unit
) {
    val colorScheme = colorSchemeOverride ?: if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
