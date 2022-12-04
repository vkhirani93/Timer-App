package com.iotis.timerapp.demo.di.module

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import com.iotis.timerapp.demo.presentation.ui.feature_home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

@ExperimentalMaterial3Api
@ExperimentalLifecycleComposeApi
val homeModule = module {
    /**
     * Providing HomeViewModel with constructor injection
     * */
    viewModelOf(::HomeViewModel)
}