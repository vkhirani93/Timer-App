package com.iotis.timerapp.demo.presentation.ui.feature_home

import android.app.Application
import android.os.CountDownTimer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.viewModelScope
import com.iotis.timerapp.demo.utility.NotificationUtility
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.util.concurrent.TimeUnit
import kotlin.math.floor

/**
 * Enum for timer states i.e. Play, Pause, Resume & Stop
 * */
enum class TimerState {
    STATE_START,
    STATE_PAUSE,
    STATE_RESUME,
    STATE_STOP
}

/**
 * UI state for the Home route.
 *
 * This is derived from [HomeViewModelState], but split into two possible subclasses to more
 * precisely represent the state available to render the UI.
 */
sealed interface HomeUiState {
    val timerState: TimerState
    val displayTime: String
    val progress: Float
    val actionOneText: String
    val actionTwoText: String
    val isTimeOut: Boolean
    val shouldPostNotification: Boolean

    /**
     * There are no posts to render.
     *
     * This could either be because they are still loading or they failed to load, and we are
     * waiting to reload them.
     */
    data class HomeData(
        override val timerState: TimerState,
        override val displayTime: String,
        override val progress: Float,
        override val actionOneText: String,
        override val actionTwoText: String,
        override val isTimeOut: Boolean,
        override val shouldPostNotification: Boolean
    ) : HomeUiState
}

/**
 * An internal representation of the Home route state, in a raw form
 */
private data class HomeViewModelState(
    val timerState: TimerState,
    val displayTime: String,
    val progress: Float,
    val actionOneText: String,
    val actionTwoText: String,
    val isTimeOut: Boolean,
    val shouldPostNotification: Boolean
) {
    /**
     * Converts this [HomeViewModelState] into a more strongly typed [HomeUiState] for driving
     * the ui.
     */
    fun toUiState(): HomeUiState = HomeUiState.HomeData(
        timerState = timerState,
        displayTime = displayTime,
        progress = progress,
        actionOneText = actionOneText,
        actionTwoText = actionTwoText,
        isTimeOut = isTimeOut,
        shouldPostNotification = shouldPostNotification
    )
}

/**
 * ViewModel that handles the business logic of the Home screen
 */
@ExperimentalLifecycleComposeApi
@ExperimentalMaterial3Api
class HomeViewModel(
    private val application: Application
) : ViewModel() {
    private lateinit var countDownTimer: CountDownTimer
    private var millisRemaining = CONST_TIMER_VALUE

    private val viewModelState = MutableStateFlow(
        HomeViewModelState(
            timerState = TimerState.STATE_STOP,
            displayTime = getDisplayTime(CONST_TIMER_VALUE),
            progress = calculateTimerProgress(CONST_TIMER_VALUE),
            actionOneText = CONST_BTN_TXT_START,
            actionTwoText = CONST_BTN_TXT_STOP,
            isTimeOut = false,
            shouldPostNotification = false
        )
    )

    init {
        initialiseCountDownTimer(CONST_TIMER_VALUE)
    }

    /**
     * Initialisation of values and count down timer in view model.
     * */
    private fun initialiseCountDownTimer(totalTimeInMillis: Long) {
        countDownTimer = object : CountDownTimer(totalTimeInMillis, CONST_TIMER_TICK) {
            override fun onTick(millisUntilFinished: Long) {
                millisRemaining = millisUntilFinished

                val displayTime = getDisplayTime(millisUntilFinished)
                val progress = calculateTimerProgress(millisUntilFinished)

                viewModelScope.launch {
                    viewModelState.update {
                        it.copy(
                            displayTime = displayTime,
                            progress = progress
                        )
                    }
                }
            }

            override fun onFinish() {
                millisRemaining = 0L

                val displayTime = getDisplayTime(millisRemaining)
                val progress = calculateTimerProgress(millisRemaining)

                viewModelScope.launch {
                    viewModelState.update {
                        it.copy(
                            timerState = TimerState.STATE_STOP,
                            displayTime = displayTime,
                            progress = progress,
                            actionOneText = CONST_BTN_TXT_START,
                            actionTwoText = CONST_BTN_TXT_RESET,
                            isTimeOut = true
                        )
                    }
                }

                postNotification()
            }
        }
    }

    /**
     * UI state exposed to the UI
     * */
    val uiState = viewModelState
        .map(HomeViewModelState::toUiState)
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value.toUiState()
        )

    /**
     * Function that gets called when action number 1 is clicked.
     * */
    internal fun toggleCountDownTimerState() {
        when (uiState.value.timerState) {
            TimerState.STATE_STOP -> {
                initialiseCountDownTimer(CONST_TIMER_VALUE)
                countDownTimer.start()

                viewModelScope.launch {
                    viewModelState.update {
                        it.copy(
                            timerState = TimerState.STATE_START,
                            actionOneText = CONST_BTN_TXT_PAUSE,
                            isTimeOut = false
                        )
                    }
                }
            }

            TimerState.STATE_PAUSE -> {
                initialiseCountDownTimer(millisRemaining)
                countDownTimer.start()

                viewModelScope.launch {
                    viewModelState.update {
                        it.copy(
                            timerState = TimerState.STATE_RESUME,
                            actionOneText = CONST_BTN_TXT_PAUSE
                        )
                    }
                }
            }

            else -> {
                countDownTimer.cancel()

                viewModelScope.launch {
                    viewModelState.update {
                        it.copy(
                            timerState = TimerState.STATE_PAUSE,
                            actionOneText = CONST_BTN_TXT_RESUME
                        )
                    }
                }
            }
        }
    }

    /**
     * Function that gets called when action number 2 is clicked.
     * */
    internal fun stopCountDownTimer() {
        countDownTimer.cancel()
        millisRemaining = CONST_TIMER_VALUE

        viewModelScope.launch {
            viewModelState.update {
                it.copy(
                    timerState = TimerState.STATE_STOP,
                    progress = calculateTimerProgress(CONST_TIMER_VALUE),
                    displayTime = getDisplayTime(CONST_TIMER_VALUE),
                    actionOneText = CONST_BTN_TXT_START,
                    actionTwoText = CONST_BTN_TXT_STOP
                )
            }
        }
    }

    /**
     * Invalidate notification and timeout when the app
     * is in foreground to avoid consuming stale values.
     * */
    internal fun invalidateStaleValues() {
        viewModelScope.launch {
            viewModelState.update {
                it.copy(
                    isTimeOut = false,
                    shouldPostNotification = false
                )
            }
        }
    }

    /**
     * Function responsible for deciding the mode of notification i.e.
     *
     * - App notification if the app is in background OR
     * - Media sound if the app is in foreground
     * */
    internal fun shouldPostNotification() {
        viewModelScope.launch {
            viewModelState.update {
                it.copy(
                    shouldPostNotification = true
                )
            }
        }
    }

    /**
     * Function responsible for notifying user via app notification if it is in background.
     * */
    private fun postNotification() {
        if (uiState.value.shouldPostNotification) {
            NotificationUtility.notifyUser(application.applicationContext)
        }
    }

    /**
     * Function responsible for formatting time like 00:00
     * */
    private fun getDisplayTime(remainingTimeInMillis: Long): String {
        val numberFormat = DecimalFormat("00")

        val minutesRemaining = numberFormat.format(
            TimeUnit.MILLISECONDS.toMinutes(remainingTimeInMillis)
        )

        val secondsRemaining = numberFormat.format(
            (TimeUnit.MILLISECONDS.toSeconds(remainingTimeInMillis) % 60)
        )

        return "$minutesRemaining:$secondsRemaining"
    }

    /**
     * Calculating timer progress on every single tick of the timer
     * */
    private fun calculateTimerProgress(remainingTimeInMillis: Long): Float {
        /**
         * Doing this next step because of lag in CountDownTimer
         * */
        val exactTimeRemaining =
            floor(remainingTimeInMillis.toFloat() / CONST_TIMER_TICK) * CONST_TIMER_TICK
        return exactTimeRemaining / CONST_TIMER_VALUE
    }

    /**
     * Constants used in the view model.
     * */
    private companion object {
        private const val CONST_TIMER_VALUE = 2 * 60 * 1000L
        private const val CONST_TIMER_TICK = 1 * 1000L

        private const val CONST_BTN_TXT_START = "START"
        private const val CONST_BTN_TXT_PAUSE = "PAUSE"
        private const val CONST_BTN_TXT_RESUME = "RESUME"
        private const val CONST_BTN_TXT_STOP = "STOP"
        private const val CONST_BTN_TXT_RESET = "RESET"
    }
}