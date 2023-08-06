package com.yannickpulver.plans.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PredictionDto(
    val predictions: List<PlacePrediction>
)


@Serializable
data class PlacePrediction(
    val description: String,
    @SerialName("place_id") val id: String
)