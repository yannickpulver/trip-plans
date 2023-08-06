package com.yannickpulver.plans.ui.feature.locations.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import com.yannickpulver.plans.data.dto.Place

data class LocationDetailRoute(val place: Place) : Screen {
    @Composable
    override fun Content() {
        LocationDetailScreen(place)
    }
}

@Composable
fun LocationDetailScreen(place: Place) {
    Surface(Modifier.fillMaxSize()) {
        Column {
            Text(place.name)
            Text(place.formatted_address)
        }
    }
}