package com.iotis.timerapp.demo.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.dimensionResource
import com.iotis.timerapp.demo.R

/**
 * Custom view created for circular progress in the app.
 * */
@Composable
fun MyCircularProgress(progress: Float) {
    val backgroundColor = MaterialTheme.colorScheme.surfaceVariant
    val foregroundColor = MaterialTheme.colorScheme.tertiary

    Canvas(
        modifier = Modifier
            .size(dimensionResource(id = R.dimen.dimen_240dp))
            .padding(dimensionResource(id = R.dimen.dimen_8dp))
    ) {
        drawCircle(
            SolidColor(backgroundColor),
            size.width / 2,
            style = Stroke(
                width = 8f,
                cap = StrokeCap.Round
            )
        )

        val convertedValue = progress * 360
        drawArc(
            brush = SolidColor(foregroundColor),
            startAngle = -90f,
            sweepAngle = convertedValue,
            useCenter = false,
            style = Stroke(
                width = 16f,
                cap = StrokeCap.Round
            )
        )
    }
}