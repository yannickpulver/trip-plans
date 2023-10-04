package com.yannickpulver.plans.ui.feature.plans.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.yannickpulver.plans.data.dto.Place
import com.yannickpulver.plans.data.dto.PlacePrediction
import com.yannickpulver.plans.data.dto.Plan
import com.yannickpulver.plans.ui.components.AppBar
import com.yannickpulver.plans.ui.feature.locations.AddLocationItem
import com.yannickpulver.plans.ui.feature.locations.LocationItem
import com.yannickpulver.plans.ui.feature.locations.detail.LocationDetailRoute
import com.yannickpulver.plans.ui.feature.plans.add.AddPlanContent
import com.yannickpulver.plans.ui.feature.plans.uiColor
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
    val navigator = LocalNavigator.current
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { AppBar(onClick = { navigator?.pop() }) },
        bottomBar = {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(8.dp, 8.dp, 0.dp, 0.dp),
                shadowElevation = 4.dp,
                tonalElevation = 2.dp
            ) {
                val focusRequester = remember { FocusRequester() }
                AddLocationItem(
                    focusRequester = focusRequester,
                    add = addLocation,
                    query = updateQuery,
                    predictions = predictions,
                    modifier = Modifier.navigationBarsPadding().imePadding()
                        .padding(bottom = 4.dp, top = 12.dp),
                    predictionsBelow = false
                )
            }
        }
    ) {
        BackgroundImage(plan)

        LazyColumn(
            Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 100.dp, bottom = 100.dp)
        ) {
            item {
                Header(plan)
            }

            items(locations) { place ->
                LocationItem(
                    place = place,
                    onClick = { navigator?.push(LocationDetailRoute(place.id)) })
            }

            item { Spacer(Modifier.padding(WindowInsets.ime.asPaddingValues()).height(24.dp)) }
        }
    }
}

@Composable
private fun Header(plan: Plan) {
    Surface(
        shadowElevation = 10.dp,
        shape = RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp,
            bottomStart = 0.dp,
            bottomEnd = 0.dp
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    plan.icon,
                    style = MaterialTheme.typography.headlineMedium,
                )
                Text(
                    plan.title,
                    style = MaterialTheme.typography.headlineMedium,
                )
            }
            Text(
                "${plan.locations.size} locations",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.alpha(0.5f)
            )
            Divider(color = DividerDefaults.color.copy(0.2f))
        }
    }
}

@Composable
private fun BackgroundImage(plan: Plan) {
    Box(
        Modifier.fillMaxWidth().height(250.dp).background(plan.uiColor()),
        contentAlignment = Alignment.Center
    ) {
        //Text(plan.icon, modifier = Modifier.padding(bottom = 24.dp))
    }
}

