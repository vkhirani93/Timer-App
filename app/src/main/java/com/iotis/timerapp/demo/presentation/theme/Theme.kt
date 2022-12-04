package com.iotis.timerapp.demo.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import com.google.accompanist.systemuicontroller.rememberSystemUiController

/**
 * Dark Color Scheme for dark mode
 * */
private val DarkColorScheme = darkColorScheme(
    primary = PrimaryDark,
    onPrimary = TextColorLight,
    secondary = SecondaryDark,
    onSecondary = TextColorDark,
    tertiary = TertiaryDark,
    onTertiary = TextColorDark,
    background = BackgroundDark,
    onBackground = TextColorDark,
    surface = SurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onSurface = TextColorDark,
)

/**
 * Light Color Scheme for normal mode
 * */
private val LightColorScheme = lightColorScheme(
    primary = PrimaryLight,
    onPrimary = TextColorDark,
    secondary = SecondaryLight,
    onSecondary = TextColorLight,
    tertiary = TertiaryLight,
    onTertiary = TextColorLight,
    background = BackgroundLight,
    onBackground = TextColorLight,
    surface = SurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    onSurface = TextColorLight
)

/**
 * Material Design 3 Theme
 * */
@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    /**
     * Providing color and content color to status bar with SystemUiController.
     * */
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(colorScheme.secondary)
        systemUiController.statusBarDarkContentEnabled = darkTheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}