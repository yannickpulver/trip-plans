package com.yannickpulver.plans

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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