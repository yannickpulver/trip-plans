package com.yannickpulver.tripplans

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.seiko.imageloader.LocalImageLoader
import com.yannickpulver.tripplans.ui.feature.plans.PlansRoute
import com.yannickpulver.tripplans.ui.generateImageLoader
import com.yannickpulver.tripplans.ui.theme.AppTheme

@Composable
fun App(darkTheme: Boolean, dynamicColor: Boolean) {
    AppTheme(useDarkTheme = darkTheme, dynamicColor = dynamicColor) {
        CompositionLocalProvider(LocalImageLoader provides generateImageLoader()) {
            PlansRoute()
        }
    }
}

