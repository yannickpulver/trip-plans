package com.yannickpulver.plans.data.remote

import com.benasher44.uuid.uuid4
import com.yannickpulver.plans.BuildKonfig
import com.yannickpulver.plans.data.dto.Place
import com.yannickpulver.plans.data.dto.PlaceDto
import com.yannickpulver.plans.data.dto.PlacePrediction
import com.yannickpulver.plans.data.dto.PredictionDto
import com.yannickpulver.plans.domain.MapsRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class GoogleMapsRepository(private val client: HttpClient) : MapsRepository {

    private var sessionToken: String = uuid4().toString()

    override suspend fun fetchPlace(placeId: String): Place {
        val results =
            client.get("https://maps.googleapis.com/maps/api/place/details/json?place_id=$placeId&key=${BuildKonfig.MAPS_API_KEY}&sessiontoken=$sessionToken")
        return results.body<PlaceDto>().place
    }

    override suspend fun fetchPredictions(query: String): List<PlacePrediction> {
        val results = client.get("https://maps.googleapis.com/maps/api/place/autocomplete/json?input=$query&key=${BuildKonfig.MAPS_API_KEY}&sessiontoken=$sessionToken")
        val response = results.body<PredictionDto>()
        refreshSession()
        return response.predictions
    }

    private fun refreshSession() {
        sessionToken = uuid4().toString()
    }
}
