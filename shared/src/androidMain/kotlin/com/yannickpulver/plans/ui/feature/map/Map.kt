package com.yannickpulver.plans.ui.feature.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.yannickpulver.plans.data.dto.Place

@Composable
actual fun Map(locations: List<Place>) {
    if (locations.isEmpty()) return

    val cameraPositionState = rememberCameraPositionState {
        val bounds = LatLngBounds.builder().apply {
            locations.forEach {
                include(LatLng(it.geometry.location.lat, it.geometry.location.lng))
            }
        }.build()

        position = CameraPosition.fromLatLngZoom(bounds.center, 5f)
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize().clip(MaterialTheme.shapes.medium),
        cameraPositionState = cameraPositionState,
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