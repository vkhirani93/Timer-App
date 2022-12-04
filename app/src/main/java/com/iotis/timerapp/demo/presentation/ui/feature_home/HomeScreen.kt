package com.iotis.timerapp.demo.presentation.ui.feature_home

import android.media.MediaPlayer
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.dimensionResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iotis.timerapp.demo.R
import com.iotis.timerapp.demo.presentation.components.MyCircularProgress
import org.koin.androidx.compose.koinViewModel

@ExperimentalMaterial3Api
@ExperimentalLifecycleComposeApi
@Composable
fun HomeScreen(
    innerPadding: PaddingValues, homeViewModel: HomeViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()
    val animatedProgress: Float by animateFloatAsState(
        targetValue = uiState.progress,
        animationSpec = tween(
            durationMillis = 1000,
            easing = LinearEasing
        )
    )

    /**
     * Disposable effect to observe the change in lifecycle state
     * */
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                homeViewModel.invalidateStaleValues()
            }

            if (event == Lifecycle.Event.ON_PAUSE) {
                homeViewModel.shouldPostNotification()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    /**
     * Disposable effect to observe time out value in order to play notification sound.
     * */
    DisposableEffect(uiState.isTimeOut) {
        val mediaPlayerTimeUp = MediaPlayer.create(context, R.raw.raw_time_up)

        if (uiState.isTimeOut) {
            mediaPlayerTimeUp.start()
        }

        onDispose {
            mediaPlayerTimeUp.stop()
        }
    }

    /**
     * We can use scaffold inside each screen as well.
     * Avoiding it here as it is a single screen app.
     * */
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(innerPadding)
            .padding(dimensionResource(id = R.dimen.dimen_16dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            MyCircularProgress(
                progress = animatedProgress
            )

            Text(
                text = uiState.displayTime,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Button(modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.dimen_52dp))
                .weight(1f)
                .padding(end = dimensionResource(id = R.dimen.dimen_8dp)),
                shape = MaterialTheme.shapes.extraSmall,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                enabled = uiState.progress != 0f,
                onClick = {
                    homeViewModel.toggleCountDownTimerState()
                }) {
                Text(
                    text = uiState.actionOneText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            Button(modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.dimen_52dp))
                .weight(1f)
                .padding(start = dimensionResource(id = R.dimen.dimen_8dp)),
                shape = MaterialTheme.shapes.extraSmall,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                enabled = uiState.progress != 1f,
                onClick = {
                    homeViewModel.stopCountDownTimer()
                }) {
                Text(
                    text = uiState.actionTwoText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}