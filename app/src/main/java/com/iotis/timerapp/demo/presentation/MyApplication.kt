package com.iotis.timerapp.demo.presentation

import android.app.Application
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import com.iotis.timerapp.demo.di.module.homeModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

@ExperimentalLifecycleComposeApi
@ExperimentalMaterial3Api
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            /**
             * Log Koin into Android logger
             * */
            androidLogger()

            /**
             * Reference Android context
             * */
            androidContext(this@MyApplication)

            /**
             * Use properties from assets/koin.properties
             * */
            androidFileProperties()

            /**
             * Load modules
             * */
            modules(homeModule)
        }
    }
}