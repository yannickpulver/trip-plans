package com.yannickpulver.plans.ui.feature.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.yannickpulver.plans.MR
import com.yannickpulver.plans.ui.feature.locations.LocationsTab
import com.yannickpulver.plans.ui.feature.profile.ProfileTab
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.launch

object HomeRoute : Screen {
    @Composable
    override fun Content() {
        HomeScreen()
    }
}

@Composable
fun HomeScreen() {
    TabNavigator(LocationsTab) {
        Scaffold(
            floatingActionButton = { FloatingActionButton(it.current == LocationsTab) },
            bottomBar = { BottomBar() },
            modifier = Modifier.fillMaxSize(),
            content = { CurrentTab() })
    }
}

@Composable
private fun FloatingActionButton(show: Boolean = true) {
    val addState = AddStateHolder.state.collectAsState()
    val scope = rememberCoroutineScope()

    AnimatedVisibility(
        visible = show && !addState.value,
        enter = fadeIn() + slideInVertically { it / 2 },
        exit = fadeOut() + slideOutVertically { it / 2 }
    ) {
        FloatingActionButton(
            onClick = {
                scope.launch {
                    AddStateHolder.show(true)
                }
            },
            content = {
                Icon(
                    Icons.Default.Add,
                    contentDescription = stringResource(MR.strings.locations_add_button)
                )
            })
    }
}

@Composable
private fun BottomBar() {
    NavigationBar {
        TabItem(LocationsTab)
        TabItem(ProfileTab)
    }
}

@Composable
private fun RowScope.TabItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current

    NavigationBarItem(
        selected = tabNavigator.current.key == tab.key,
        icon = {
            Icon(painter = tab.options.icon!!, contentDescription = tab.options.title)
        },
        label = { Text(stringResource(MR.strings.nav_profile)) },
        onClick = { tabNavigator.current = tab })
}