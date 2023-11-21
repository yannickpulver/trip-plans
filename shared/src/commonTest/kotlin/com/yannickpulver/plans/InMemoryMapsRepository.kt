package com.yannickpulver.plans

import com.yannickpulver.plans.data.dto.Place
import com.yannickpulver.plans.data.dto.PlacePrediction
import com.yannickpulver.plans.domain.MapsRepository

class InMemoryMapsRepository : MapsRepository {

    val placePredictions = listOf(PlacePrediction("1", "Test"), PlacePrediction("2", "Test 2"))

    override suspend fun fetchPlace(placeId: String): Place {
        return Place.Preview
    }

    override suspend fun fetchPredictions(query: String): List<PlacePrediction> {
        return if (query.isBlank()) {
            emptyList()
        } else {
            placePredictions
        }
    }
}