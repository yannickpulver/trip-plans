package com.yannickpulver.plans.domain

import com.yannickpulver.plans.data.dto.Place
import com.yannickpulver.plans.data.dto.PlacePrediction

interface MapsRepository {
    suspend fun fetchPlace(placeId: String): Place
    suspend fun fetchPredictions(query: String): List<PlacePrediction>
}