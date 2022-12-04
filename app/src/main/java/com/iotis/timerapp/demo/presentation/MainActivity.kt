package com.iotis.timerapp.demo.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import com.iotis.timerapp.demo.R
import com.iotis.timerapp.demo.presentation.theme.AppTheme
import com.iotis.timerapp.demo.presentation.ui.feature_home.HomeScreen


@ExperimentalLifecycleComposeApi
@ExperimentalMaterial3Api
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            /**
             * Wrapping the code inside TimerAppTheme
             * */
            AppTheme {
                /**
                 * Using the Scaffold at the top most level i.e. MainActivity
                 * */
                /**
                 * Using the Scaffold at the top most level i.e. MainActivity
                 * */
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = stringResource(id = R.string.app_name),
                                    style = MaterialTheme.typography.titleMedium
                                )
                            },
                            colors = TopAppBarDefaults.smallTopAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                titleContentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    },
                    containerColor = MaterialTheme.colorScheme.background
                ) { innerPadding ->
                    /**
                     * Passing on PaddingValues to HomeScreen to use it for the container.
                     *
                     * Ideally, it should contain NavHost/AnimatedNavHost so that one can load NavGraphs in it.
                     * Since, we have only one composable, I've not used it here.
                     * */
                    /**
                     * Passing on PaddingValues to HomeScreen to use it for the container.
                     *
                     * Ideally, it should contain NavHost/AnimatedNavHost so that one can load NavGraphs in it.
                     * Since, we have only one composable, I've not used it here.
                     * */
                    HomeScreen(innerPadding)
                }
            }
        }
    }
}

@ExperimentalMaterial3Api
@ExperimentalLifecycleComposeApi
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AppTheme {
        val innerPadding = PaddingValues(
            all = dimensionResource(id = R.dimen.dimen_0dp)
        )

        HomeScreen(innerPadding)
    }
}