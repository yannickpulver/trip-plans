package com.yannickpulver.tripplans.ui.feature.plans

import PlansViewModel
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.seiko.imageloader.model.ImageRequest
import com.seiko.imageloader.model.ImageResult
import com.seiko.imageloader.rememberImageAction
import com.seiko.imageloader.rememberImageActionPainter
import org.koin.compose.koinInject

@Composable
fun PlansRoute(viewModel: PlansViewModel = koinInject()) {
    val state by viewModel.state.collectAsState()
    PlansScreen(state)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlansScreen(state: PlansState) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { },
                content = { Icon(Icons.Default.Add, contentDescription = "Add") })
        }, modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            Modifier.padding(it).fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(state.plans) {
                PlanItem(name = it)
            }
        }
    }
}

@Composable
fun PlanItem(name: String, modifier: Modifier = Modifier) {
    Card(modifier.fillMaxWidth()) {
        Column {
            CardImage(name, Modifier.fillMaxWidth().height(200.dp))
            Text("Item $name", Modifier.padding(16.dp))
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CardImage(name: String, modifier: Modifier = Modifier) {
    val request = remember { ImageRequest("https://source.unsplash.com/random/700x400?${name}") }
    val action by rememberImageAction(request)
    val painter = rememberImageActionPainter(action)
    println("action: $action")

    AnimatedContent(action) {
        when (it) {
            is ImageResult -> {
                Image(
                    painter = painter,
                    contentDescription = "name",
                    contentScale = ContentScale.Crop,
                    modifier = modifier
                )
            }

            else -> Unit
        }
    }
}
