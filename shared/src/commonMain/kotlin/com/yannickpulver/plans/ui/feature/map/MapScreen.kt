package com.yannickpulver.plans.ui.feature.map

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.yannickpulver.plans.MR
import com.yannickpulver.plans.data.dto.Place
import dev.icerock.moko.resources.compose.stringResource
import org.koin.compose.koinInject

internal object MapTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Default.Search)
            val title = stringResource(MR.strings.nav_maps)

            return remember {
                TabOptions(index = 0u, title = title, icon = icon)
            }
        }

    @Composable
    override fun Content() {
        MapScreen()
    }
}

@Composable
fun MapScreen(viewModel: MapViewModel = koinInject()) {
    val state = viewModel.state.collectAsState()
    Map(state.value.locations)
}


@Composable
expect fun Map(locations: List<Place>)