package com.yannickpulver.plans.ui.feature.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.FadeTransition
import cafe.adriel.voyager.transitions.SlideTransition
import com.yannickpulver.plans.ui.feature.home.HomeRoute

@Composable
fun AppNavigation() {
    Navigator(HomeRoute) { navigator ->
        FadeTransition(navigator)
    }
}
