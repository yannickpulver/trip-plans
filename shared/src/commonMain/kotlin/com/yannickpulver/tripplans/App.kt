package com.yannickpulver.tripplans

import androidx.compose.runtime.Composable
import com.yannickpulver.tripplans.ui.feature.plans.PlansRoute
import com.yannickpulver.tripplans.ui.theme.AppTheme

@Composable
fun App(darkTheme: Boolean, dynamicColor: Boolean) {
    AppTheme(useDarkTheme = darkTheme, dynamicColor = dynamicColor) {
        PlansRoute()
    }
}

