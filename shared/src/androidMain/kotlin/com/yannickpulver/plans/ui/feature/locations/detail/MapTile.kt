package com.yannickpulver.plans.ui.feature.locations.detail

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.yannickpulver.plans.data.dto.Location

@Composable
actual fun MapTile(location: Location, modifier: Modifier) {
    val latLng = LatLng(location.lat, location.lng)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(latLng, 12f)
    }

    GoogleMap(
        modifier = modifier.fillMaxWidth().height(200.dp).clip(MaterialTheme.shapes.medium),
        cameraPositionState = cameraPositionState
    ) {
        Marker(
            state = MarkerState(position = latLng),
            title = "Marker"
        )
    }
}
