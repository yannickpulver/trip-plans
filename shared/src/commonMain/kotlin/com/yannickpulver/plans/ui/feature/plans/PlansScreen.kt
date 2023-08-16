package com.yannickpulver.plans.ui.feature.plans

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.TravelExplore
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.seiko.imageloader.rememberImagePainter
import com.yannickpulver.plans.MR
import dev.icerock.moko.resources.compose.stringResource

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
fun PlansScreen() {

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

        items(10) {
            PlansCard(onClick = {})
        }
    }
}

@Composable
fun PlansCard(onClick: () -> Unit) {
    Column(Modifier.clickable(onClick = onClick)) {
        Card(
            modifier = Modifier.fillMaxWidth().height(200.dp).padding(bottom = 8.dp)
        ) {
            val painter =
                rememberImagePainter(url = "https://firebasestorage.googleapis.com/v0/b/trip-plans-31758.appspot.com/o/locations%2FChIJd312ZkkNOUYRCAretD6gQp4%2FAUacShj4HKV71_b_C1YQ3odpJ6sBUhbdfrqUdtf0a4be2BBaKfpCavlVPspZ9My5w6t-lUIKTKJBVP-QS3h66lbPfdf4JW3Y_6El75ITq3l3XgovY5pyKmRPV4IJrVCWWhQH00yHBYPSmgC_uruPxcI_Cchc9uFkH1TdppsBmxRibmRVjA0z.jpg?alt=media")
            Image(
                painter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        Text("Title", style = MaterialTheme.typography.titleMedium)
        Text("Subtitle", style = MaterialTheme.typography.labelMedium)
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
