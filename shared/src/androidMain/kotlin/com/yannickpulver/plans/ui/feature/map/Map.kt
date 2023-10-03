package com.yannickpulver.plans.ui.feature.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.yannickpulver.plans.data.dto.Place
import com.yannickpulver.plans.ui.components.EmptyScreen
import com.yannickpulver.plans.ui.feature.locations.detail.LoadingScreen

@Composable
actual fun Map(locations: List<Place>?) {
    Box(Modifier.fillMaxSize()) {
        when {
            locations == null -> LoadingScreen()
            locations.isEmpty() -> EmptyScreen("No locations found")
            else -> MapContent(locations)
        }
    }
}


@Composable
private fun MapContent(locations: List<Place>) {
    val cameraPositionState = rememberCameraPositionState()

    LaunchedEffect(Unit) {
        val bounds = LatLngBounds.builder().apply {
            locations.forEach {
                include(LatLng(it.geometry.location.lat, it.geometry.location.lng))
            }
        }.build()
        cameraPositionState.move(CameraUpdateFactory.newLatLngBounds(bounds, 200))
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize().clip(MaterialTheme.shapes.medium),
        cameraPositionState = cameraPositionState
    ) {
        locations.forEach {
            val latLng = LatLng(it.geometry.location.lat, it.geometry.location.lng)
            Marker(
                state = MarkerState(position = latLng),
                title = it.name
            )
        }
    }
}
