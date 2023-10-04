package com.yannickpulver.plans.ui.feature.locations

import LocationsViewModel
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.rounded.ViewStream
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissState
import androidx.compose.material3.DismissValue
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.seiko.imageloader.model.ImageRequest
import com.seiko.imageloader.model.ImageResult
import com.seiko.imageloader.rememberImageAction
import com.seiko.imageloader.rememberImageActionPainter
import com.yannickpulver.plans.MR
import com.yannickpulver.plans.data.dto.Place
import com.yannickpulver.plans.data.dto.PlacePrediction
import com.yannickpulver.plans.ui.feature.home.AddStateHolder
import com.yannickpulver.plans.ui.feature.locations.detail.LocationDetailRoute
import dev.icerock.moko.resources.compose.stringResource
import org.koin.compose.koinInject

internal object LocationsTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Rounded.ViewStream)
            val title = stringResource(MR.strings.nav_locations)

            return remember {
                TabOptions(index = 0u, title = title, icon = icon)
            }
        }

    @Composable
    override fun Content() {
        LocationsScreen()
    }
}

@Composable
fun LocationsScreen(viewModel: LocationsViewModel = koinInject()) {
    val state by viewModel.state.collectAsState()
    LocationsScreen(
        state = state,
        add = viewModel::add,
        remove = viewModel::remove,
        query = viewModel::updateQuery
    )
}

@Composable
fun LocationsScreen(
    state: LocationsState,
    add: (String) -> Unit,
    remove: (String) -> Unit,
    query: (String) -> Unit
) {
    val addState = AddStateHolder.state.collectAsState()

    PlanScreenContent(
        state = state,
        add = {
            add(it)
            AddStateHolder.show(false)
        },
        remove = remove,
        addVisible = addState.value,
        query = query
    )
}

@Composable
fun PlanScreenContent(
    state: LocationsState,
    addVisible: Boolean,
    add: (String) -> Unit,
    remove: (String) -> Unit,
    query: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }
    val lazyListState = rememberLazyListState()

    LaunchedEffect(state.locations) {
        lazyListState.animateScrollToItem(0)
    }

    LazyColumn(
        modifier.fillMaxWidth(),
        state = lazyListState,
        contentPadding = WindowInsets.statusBars.asPaddingValues()
    ) {
        if (addVisible) {
            item {
                AddLocationItem(
                    focusRequester = focusRequester,
                    add = add,
                    query = query,
                    state.predictions
                )
                Divider(Modifier.fillMaxSize())
            }
        }

        itemsIndexed(state.locations, key = { index, it -> it.id }) { index, location ->
            if (index > 0) {
                Divider(Modifier.fillMaxSize())
            }
            DismissableLocationItem(remove, location)
        }
    }

    LaunchedEffect(addVisible) {
        if (addVisible) {
            lazyListState.animateScrollToItem(0)
            focusRequester.requestFocus()
        }
    }
}

@Composable
fun AddLocationItem(
    focusRequester: FocusRequester,
    add: (String) -> Unit,
    query: (String) -> Unit,
    predictions: List<PlacePrediction>,
    predictionsBelow: Boolean = true,
    modifier: Modifier = Modifier
) {
    val (textState, onTextChanged) = remember { mutableStateOf("") }
    Column(
        modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!predictionsBelow) {
            PredictionsList(textState, predictions, add, onTextChanged)
        }
        OutlinedTextField(
            value = textState,
            onValueChange = {
                onTextChanged(it)
                query(it)
            },
            placeholder = { Text(stringResource(MR.strings.locations_add_hint)) },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .background(MaterialTheme.colorScheme.surface),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
        )
        if (predictionsBelow) {
            PredictionsList(textState, predictions, add, onTextChanged)
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PredictionsList(
    textState: String,
    predictions: List<PlacePrediction>,
    add: (String) -> Unit,
    onTextChanged: (String) -> Unit,
) {
    val keyboard = LocalSoftwareKeyboardController.current

    if (textState.isNotEmpty()) {
        OutlinedCard(
            shape = RoundedCornerShape(8.dp)
        ) {
            predictions.forEach {
                Text(
                    text = it.description,
                    modifier = Modifier.clickable {
                        add(it.id)
                        onTextChanged("")
                        keyboard?.hide()
                    }.padding(12.dp).fillMaxWidth(),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DismissableLocationItem(remove: (String) -> Unit, place: Place) {
    val dismissState = rememberDismissState(
        positionalThreshold = { it / 2 },
        confirmValueChange = {
            if (it == DismissValue.DismissedToStart) {
                remove(place.id)
                true
            } else {
                false
            }
        }
    )
    val navigator = LocalNavigator.current
    SwipeToDismiss(
        dismissContent = {
            LocationItem(
                place = place,
                onClick = { navigator?.parent?.push(LocationDetailRoute(it)) })
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
            contentDescription = null,
            modifier = Modifier.scale(scale)
        )
    }
}

@Composable
fun LocationItem(place: Place, modifier: Modifier = Modifier, onClick: (String) -> Unit) {

    Surface(
        modifier.fillMaxWidth()
            .clickable(onClick = { onClick(place.id) })
    ) {
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
                    place.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    place.city?.short_name ?: place.formatted_address,
                    style = MaterialTheme.typography.labelMedium
                )
            }
            CardImage(
                link = place.photoUrls.firstOrNull(),
                modifier = Modifier.size(72.dp).clip(RoundedCornerShape(8.dp))
            )
        }
    }
}

@Composable
fun CardImage(link: String?, modifier: Modifier = Modifier) {
    val color = LocalContentColor.current.copy(0.2f)
    Box(modifier.fillMaxSize().background(color)) {
        if (!link.isNullOrEmpty()) {
            val request = remember { ImageRequest(link) }
            val action by rememberImageAction(request)
            val painter = rememberImageActionPainter(action)
            AnimatedVisibility(
                visible = action is ImageResult,
                enter = fadeIn(),
                modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    painter = painter,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
