package com.yannickpulver.plans.ui.feature.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.yannickpulver.plans.MR
import com.yannickpulver.plans.ui.feature.locations.LocationsTab
import com.yannickpulver.plans.ui.feature.plans.PlansTab
import com.yannickpulver.plans.ui.feature.plans.detail.PlansDetailRoute
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
            floatingActionButton = { FloatingActionButton(it.current) },
            bottomBar = { BottomBar() },
            modifier = Modifier.fillMaxSize(),
            content = { CurrentTab() }
        )
    }
}

@Composable
private fun FloatingActionButton(currentTab: Tab) {
    val addState = AddStateHolder.state.collectAsState()
    val scope = rememberCoroutineScope()
    val navigator = LocalNavigator.current

    val color = animateColorAsState(
        targetValue = if (currentTab is LocationsTab) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.tertiary
        }
    )

    val contentColor = animateColorAsState(
        targetValue = if (currentTab is LocationsTab) {
            MaterialTheme.colorScheme.onPrimary
        } else {
            MaterialTheme.colorScheme.onTertiary
        }
    )

    val rotation = animateFloatAsState(
        targetValue = if (currentTab is LocationsTab) 0f else 90f
    )

    AnimatedVisibility(
        visible = currentTab in listOf(LocationsTab, PlansTab) && !addState.value,
        enter = fadeIn() + slideInVertically { it / 2 },
        exit = fadeOut() + slideOutVertically { it / 2 }
    ) {
        FloatingActionButton(
            onClick = {
                scope.launch {
                    when (currentTab) {
                        is LocationsTab -> AddStateHolder.show(true)
                        is PlansTab -> navigator?.parent?.push(PlansDetailRoute(null))
                    }
                }
            },
            content = {
                Icon(
                    Icons.Default.Add,
                    contentDescription = stringResource(MR.strings.locations_add_button)
                )
            },
            containerColor = color.value,
            contentColor = contentColor.value,
            modifier = Modifier.rotate(rotation.value)
        )
    }
}

@Composable
private fun BottomBar() {
    NavigationBar {
        TabItems()
    }
}

@Composable
fun RowScope.TabItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current

    NavigationBarItem(
        selected = tabNavigator.current.key == tab.key,
        icon = {
            tab.options.icon?.let { Icon(painter = it, contentDescription = tab.options.title) }
        },
        label = { Text(text = tab.options.title) },
        onClick = { tabNavigator.current = tab }
    )
}
