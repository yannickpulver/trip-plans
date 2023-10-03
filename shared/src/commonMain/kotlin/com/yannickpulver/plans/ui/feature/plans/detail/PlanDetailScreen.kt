package com.yannickpulver.plans.ui.feature.plans.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.seiko.imageloader.rememberImagePainter
import com.yannickpulver.plans.data.dto.Place
import com.yannickpulver.plans.data.dto.PlacePrediction
import com.yannickpulver.plans.data.dto.Plan
import com.yannickpulver.plans.ui.feature.locations.AddLocationItem
import com.yannickpulver.plans.ui.feature.locations.LocationItem
import com.yannickpulver.plans.ui.feature.locations.detail.LocationDetailRoute
import com.yannickpulver.plans.ui.feature.locations.detail.LocationDetailScreen
import org.koin.compose.koinInject

data class PlansDetailRoute(val id: String?) : Screen {
    @Composable
    override fun Content() {
        PlansDetailScreen(id)
    }
}

@Composable
fun PlansDetailScreen(id: String?, viewModel: PlanDetailViewModel = koinInject()) {
    val state = viewModel.state.collectAsState()

    LaunchedEffect(id) {
        viewModel.setId(id)
    }

    if (id == null && state.value.plan == null) {
        AddPlanContent(
            state = state.value,
            onTitleChanged = viewModel::onTitleChanged,
            onSave = { viewModel.save() }
        )
    } else {
        state.value.plan?.let {
            PlanDetail(
                plan = it,
                locations = state.value.locations,
                state.value.predictions,
                viewModel::addLocation,
                viewModel::updateQuery
            )
        }
    }
}

@Composable
private fun PlanDetail(
    plan: Plan,
    locations: List<Place>,
    predictions: List<PlacePrediction>,
    addLocation: (String) -> Unit,
    updateQuery: (String) -> Unit
) {
    Scaffold(topBar = {
        Box {
            Image(
                painter = rememberImagePainter("https://source.unsplash.com/random/1200x600?${plan.title}"),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().height(160.dp).padding(bottom = 20.dp),
                contentScale = ContentScale.Crop
            )
            Card(
                Modifier.align(Alignment.BottomStart).fillMaxWidth().padding(horizontal = 16.dp),
                shape = CircleShape
            ) {
                Text(
                    text = plan.title,
                    modifier = Modifier.padding(12.dp).fillMaxWidth(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }) {
        LazyColumn(
            Modifier.padding(top = 160.dp - 16.dp).fillMaxSize(),
            contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp)
        ) {
            item {
                val focusRequester = remember { FocusRequester() }
                AddLocationItem(focusRequester, addLocation, updateQuery, predictions)
            }


            items(locations) { place ->
                val navigator = LocalNavigator.current
                LocationItem(place = place, onClick = { navigator?.push(LocationDetailRoute(place.id, plan.id)) })
            }
        }
    }
}

@Composable
fun AddPlanContent(
    state: PlanDetailViewState,
    onTitleChanged: (String) -> Unit,
    onSave: () -> Unit
) {
    Scaffold {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)
                .padding(WindowInsets.ime.asPaddingValues())
        ) {
            Card(
                Modifier.height(200.dp).aspectRatio(0.8f),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.secondaryContainer)
            ) {
                AnimatedVisibility(
                    visible = state.imageUrl.isNotEmpty(),
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Box(Modifier.fillMaxSize()) {
                        CircularProgressIndicator(Modifier.align(Alignment.Center))
                    }
                    Image(
                        painter = rememberImagePainter(state.imageUrl),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            val title = remember { mutableStateOf("") }

            OutlinedTextField(
                value = title.value,
                onValueChange = { title.value = it; onTitleChanged(it) },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Go
                ),
                keyboardActions = KeyboardActions(
                    onGo = { onSave() }
                )
            )

            if (state.title.isNotEmpty()) {
                Button(
                    onClick = onSave,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
                ) {
                    Text("Save")
                }
            }
        }
    }
}
