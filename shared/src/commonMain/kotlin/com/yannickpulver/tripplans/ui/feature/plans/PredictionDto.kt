package com.yannickpulver.tripplans.ui.feature.plans

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PredictionDto(
    val predictions: List<PlacePrediction>
)


@Serializable
data class PlacePrediction(
    val description: String,
    @SerialName("place_id") val placeId: String
)