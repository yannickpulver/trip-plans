package com.yannickpulver.plans.ui.feature.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.yannickpulver.plans.MR
import com.yannickpulver.plans.ui.feature.locations.LocationsTab
import com.yannickpulver.plans.ui.feature.plans.PlansTab
import com.yannickpulver.plans.ui.feature.plans.add.AddPlanRoute
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
    TabNavigator(PlansTab) {
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
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.secondaryContainer
        }
    )

    val contentColor = animateColorAsState(
        targetValue = if (currentTab is LocationsTab) {
            MaterialTheme.colorScheme.onPrimaryContainer
        } else {
            MaterialTheme.colorScheme.onSecondaryContainer
        }
    )

    AnimatedVisibility(
        visible = currentTab in listOf(LocationsTab, PlansTab) && !addState.value,
        enter = fadeIn() + slideInHorizontally { it / 2 },
        exit = fadeOut() + slideOutHorizontally { it / 2 }
    ) {
        FloatingActionButton(
            onClick = {
                scope.launch {
                    when (currentTab) {
                        is LocationsTab -> AddStateHolder.show(true)
                        is PlansTab -> navigator?.parent?.push(AddPlanRoute)
                    }
                }
            },
            content = {
                AnimatedContent(currentTab, transitionSpec = { fadeIn(tween(300)) togetherWith fadeOut(tween(300)) }) { tab ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = stringResource(MR.strings.locations_add_button)
                        )
                        Text(
                            text = stringResource(
                                if (tab is LocationsTab) {
                                    MR.strings.locations_add_button
                                } else {
                                    MR.strings.plans_add_button
                                }
                            ),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            },
            containerColor = color.value,
            contentColor = contentColor.value,
            modifier = Modifier
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
