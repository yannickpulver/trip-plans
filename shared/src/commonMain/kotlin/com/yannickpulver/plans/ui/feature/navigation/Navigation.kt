package com.yannickpulver.plans.ui.feature.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import com.yannickpulver.plans.ui.feature.home.HomeRoute

@Composable
fun AppNavigation() {
    Navigator(HomeRoute)
}
