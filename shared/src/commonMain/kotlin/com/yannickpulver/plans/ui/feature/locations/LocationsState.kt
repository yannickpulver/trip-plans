package com.yannickpulver.plans.ui.feature.locations

import com.yannickpulver.plans.data.dto.Place
import com.yannickpulver.plans.data.dto.PlacePrediction

data class LocationsState(
    val locations: List<Place> = emptyList(),
    val predictions: List<PlacePrediction> = emptyList()
) {

    companion object {
        val Preview = LocationsState(locations = listOf(Place.Preview))
        val Empty = LocationsState()
    }
}
