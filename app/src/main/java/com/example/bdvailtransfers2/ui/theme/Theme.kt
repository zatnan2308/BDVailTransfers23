package com.example.bdvailtransfers2.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

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
    onError = BDVailOnError,

    outline = BDVailOutline
)

@Composable
fun BDVailTransfersTheme(
    darkTheme: Boolean = false, // параметр оставлен для совместимости, но игнорируется
    colorSchemeOverride: ColorScheme? = null,
    content: @Composable () -> Unit
) {
    val colorScheme = colorSchemeOverride ?: LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
