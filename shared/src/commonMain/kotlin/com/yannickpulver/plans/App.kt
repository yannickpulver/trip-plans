package com.yannickpulver.plans

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.seiko.imageloader.LocalImageLoader
import com.yannickpulver.plans.ui.feature.navigation.AppNavigation
import com.yannickpulver.plans.ui.generateImageLoader
import com.yannickpulver.plans.ui.theme.AppTheme

@Composable
fun App(darkTheme: Boolean, dynamicColor: Boolean) {
    AppTheme(useDarkTheme = darkTheme, dynamicColor = dynamicColor) {
        CompositionLocalProvider(LocalImageLoader provides generateImageLoader()) {
            AppNavigation()
        }
    }
}
