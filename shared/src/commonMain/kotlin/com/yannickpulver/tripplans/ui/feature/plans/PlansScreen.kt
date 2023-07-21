package com.yannickpulver.tripplans.ui.feature.plans

import PlansViewModel
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
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

    PlansScreen(state = state, addPlan = viewModel::addPlan)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlansScreen(state: PlansState, addPlan: () -> Unit) {
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
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            state = lazyListState
        ) {
            items(state.plans, key = { it }) {
                PlanItem(name = it)
            }
        }
    }
}

@Composable
private fun NavigationBar() {
    val selectedIndex = remember { mutableStateOf(0) }
    NavigationBar {
        NavigationBarItem(
            selected = selectedIndex.value == 0,
            icon = { Icon(Icons.Rounded.Home, contentDescription = "Plans") },
            label = { Text("Plans") },
            onClick = { selectedIndex.value = 0 })
        NavigationBarItem(
            selected = selectedIndex.value == 1,
            icon = { Icon(Icons.Rounded.Person, contentDescription = "Other") },
            label = { Text("Other") },
            onClick = { selectedIndex.value = 1 })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanItem(name: String, modifier: Modifier = Modifier) {
    Card(modifier.fillMaxWidth()) {
        Column {
            CardImage(name, Modifier.fillMaxWidth().height(200.dp))
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    "Item $name",
                    style = MaterialTheme.typography.titleMedium
                )
                Chip("Iceberg")
            }
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
