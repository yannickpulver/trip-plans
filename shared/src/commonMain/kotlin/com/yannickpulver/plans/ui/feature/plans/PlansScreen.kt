package com.yannickpulver.plans.ui.feature.plans

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.TravelExplore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.yannickpulver.plans.MR
import com.yannickpulver.plans.data.dto.Plan
import com.yannickpulver.plans.ui.components.EmptyScreen
import com.yannickpulver.plans.ui.feature.plans.detail.PlansDetailRoute
import dev.icerock.moko.graphics.parseColor
import dev.icerock.moko.resources.compose.stringResource
import org.koin.compose.koinInject

internal object PlansTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Rounded.TravelExplore)
            val title = stringResource(MR.strings.nav_plans)

            return remember {
                TabOptions(index = 0u, title = title, icon = icon)
            }
        }

    @Composable
    override fun Content() {
        PlansScreen()
    }
}

@Composable
fun PlansScreen(viewModel: PlansViewModel = koinInject()) {
    val state = viewModel.state.collectAsState()

    when {
        state.value.isEmpty() -> EmptyScreen("No plans yet")
        else -> PlansContent(state.value)
    }
}

@Composable
private fun PlansContent(
    list: List<Plan>,
) {
    val navigator = LocalNavigator.current
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = WindowInsets.statusBars.asPaddingValues() + PaddingValues(
            top = 16.dp,
            bottom = 200.dp,
            start = 16.dp,
            end = 16.dp
        )
    ) {
        items(list) {
            PlansCard(it) { navigator?.parent?.push(PlansDetailRoute(it.id)) }
        }
    }
}

@Composable
fun PlansCard(plan: Plan, onClick: () -> Unit) {
    Column(Modifier.clickable(onClick = onClick)) {
        Card(
            modifier = Modifier.fillMaxWidth().height(160.dp).padding(bottom = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = plan.uiColor()
            )
        ) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(plan.icon)
            }
        }
        Text(plan.title, style = MaterialTheme.typography.titleMedium)
        Text("${plan.locations.size} locations", style = MaterialTheme.typography.labelMedium, modifier = Modifier.alpha(0.5f))
    }
}

operator fun PaddingValues.plus(other: PaddingValues): PaddingValues {
    return PaddingValues(
        start = this.calculateLeftPadding(LayoutDirection.Ltr) + other.calculateLeftPadding(
            LayoutDirection.Ltr
        ),
        top = this.calculateTopPadding() + other.calculateTopPadding(),
        end = this.calculateRightPadding(LayoutDirection.Ltr) + other.calculateRightPadding(
            LayoutDirection.Ltr
        ),
        bottom = this.calculateBottomPadding() + other.calculateBottomPadding()
    )
}

fun Plan.uiColor(): Color {
    return try {
        Color(dev.icerock.moko.graphics.Color.parseColor(color).argb)
    } catch (exception: Exception) {
        Color(0xFF000000)
    }
}