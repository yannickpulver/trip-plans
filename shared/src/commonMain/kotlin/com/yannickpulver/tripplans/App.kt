package com.yannickpulver.tripplans

import PlansViewModel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.yannickpulver.tripplans.ui.feature.plans.PlansScreen
import com.yannickpulver.tripplans.ui.theme.AppTheme
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory

@Composable
fun App(darkTheme: Boolean, dynamicColor: Boolean) {
    AppTheme(useDarkTheme = darkTheme, dynamicColor = dynamicColor) {
        val viewModel = getViewModel(
            key = "plan-list-screen",
            factory = viewModelFactory { PlansViewModel() })
        val state by viewModel.state.collectAsState()
        PlansScreen(state)
    }
}

