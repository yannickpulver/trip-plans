package com.yannickpulver.plans

import com.yannickpulver.plans.data.dto.Place
import com.yannickpulver.plans.data.dto.Plan
import com.yannickpulver.plans.domain.DataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class InMemoryDataRepository : DataRepository {
    override fun observeLocations(): Flow<List<Place?>> {
        return flowOf(emptyList())
    }

    override fun observePlans(): Flow<List<Plan?>> {
        return flowOf(emptyList())
    }

    override fun observePlanLocations(planId: String): Flow<List<Place?>> {
        return flowOf(emptyList())
    }

    override fun getLocation(id: String): Flow<Place?> {
        return flowOf(null)
    }

    override fun getPlan(id: String): Flow<Plan?> {
        return flowOf(null)
    }

    override suspend fun addLocation(place: Place) {
    }

    override suspend fun addLocationToPlan(planId: String, place: Place) {
    }

    override suspend fun addPlan(title: String, color: String, emoji: String): Plan {
        return Plan("", title, color, emoji)
    }

    override suspend fun removePlan(id: String) {
    }
}