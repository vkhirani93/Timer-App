package com.iotis.timerapp.demo.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.iotis.timerapp.demo.R

/**
 * Font family created from fonts added to the project.
 * */
private val timerFontFamily = FontFamily(
    Font(R.font.font_timer, FontWeight.W900)
)

private val contentFontFamily = FontFamily(
    Font(R.font.font_button, FontWeight.W500),
)

/**
 * Typography used in the project.
 * */
val AppTypography = Typography(
    headlineLarge = TextStyle(
        fontFamily = timerFontFamily,
        fontSize = 64.sp
    ),
    titleMedium = TextStyle(
        fontFamily = contentFontFamily,
        fontSize = 24.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = contentFontFamily,
        fontSize = 16.sp
    )
)