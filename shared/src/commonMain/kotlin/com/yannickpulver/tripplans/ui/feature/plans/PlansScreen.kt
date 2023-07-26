package com.yannickpulver.tripplans.ui.feature.plans

import PlansViewModel
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissState
import androidx.compose.material3.DismissValue
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.seiko.imageloader.model.ImageRequest
import com.seiko.imageloader.model.ImageResult
import com.seiko.imageloader.rememberImageAction
import com.seiko.imageloader.rememberImageActionPainter
import com.yannickpulver.tripplans.ui.components.Chip
import org.koin.compose.koinInject

@Composable
fun PlansRoute(viewModel: PlansViewModel = koinInject()) {
    val state by viewModel.state.collectAsState()

    PlansScreen(state = state, addPlan = viewModel::addPlan, remote = viewModel::removePlan)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlansScreen(state: PlansState, addPlan: () -> Unit, remote: (String) -> Unit) {
    val lazyListState = rememberLazyListState()

    LaunchedEffect(state) {
        lazyListState.animateScrollToItem(0)
    }


    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = addPlan,
                content = { Icon(Icons.Default.Add, contentDescription = "Add") })
        },
        bottomBar = {
            NavigationBar()
        },
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            Modifier.padding(it).fillMaxWidth(),
            state = lazyListState
        ) {
            itemsIndexed(state.locations, key = { index, it -> it }) { index, location ->
                if (index > 0) {
                    Divider(Modifier.fillMaxSize())
                }

                DismissableLocationItem(remote, location)
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DismissableLocationItem(remote: (String) -> Unit, location: String) {
    val dismissState = rememberDismissState(
        positionalThreshold = { it / 2 },
        confirmValueChange = {
            if (it == DismissValue.DismissedToStart) {
                remote(location)
                true
            } else {
                false
            }
        }
    )

    SwipeToDismiss(
        dismissContent = {
            LocationItem(name = location)
        },
        background = {
            SwipeBackground(dismissState = dismissState)
        },
        state = dismissState,
        directions = setOf(DismissDirection.EndToStart)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeBackground(dismissState: DismissState) {
    val color by animateColorAsState(
        when (dismissState.targetValue) {
            DismissValue.DismissedToStart -> MaterialTheme.colorScheme.errorContainer
            else -> MaterialTheme.colorScheme.outlineVariant
        }
    )
    val alignment = Alignment.CenterEnd
    val icon = Icons.Default.Delete

    val scale by animateFloatAsState(
        if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f
    )

    Box(
        Modifier
            .fillMaxSize()
            .background(color)
            .padding(horizontal = 20.dp),
        contentAlignment = alignment
    ) {
        Icon(
            icon,
            contentDescription = "Localized description",
            modifier = Modifier.scale(scale)
        )
    }
}

@Composable
private fun NavigationBar() {
    val selectedIndex = remember { mutableStateOf(0) }
    NavigationBar {
        NavigationBarItem(
            selected = selectedIndex.value == 0,
            icon = { Icon(Icons.Rounded.List, contentDescription = "Locations") },
            label = { Text("Locations") },
            onClick = { selectedIndex.value = 0 })
        NavigationBarItem(
            selected = selectedIndex.value == 1,
            icon = { Icon(Icons.Rounded.Person, contentDescription = "Other") },
            label = { Text("Other") },
            onClick = { selectedIndex.value = 1 })
    }
}

@Composable
fun LocationItem(name: String, modifier: Modifier = Modifier) {
    Surface(modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(12.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    "Item $name",
                    style = MaterialTheme.typography.titleMedium
                )
                Chip("Iceberg")
            }
            CardImage(name, Modifier.size(72.dp).clip(RoundedCornerShape(8.dp)))
        }
    }
}

@Composable
fun CardImage(name: String, modifier: Modifier = Modifier) {
    val request = remember { ImageRequest("https://source.unsplash.com/random/700x400?${name}") }
    val action by rememberImageAction(request)
    val painter = rememberImageActionPainter(action)

    val color = LocalContentColor.current.copy(0.2f)
    Box(modifier.fillMaxSize().background(color)) {
        AnimatedVisibility(
            visible = action is ImageResult,
            enter = fadeIn(),
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painter,
                contentDescription = "name",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
