package com.yannickpulver.plans.domain

import com.yannickpulver.plans.data.dto.Place
import com.yannickpulver.plans.data.dto.Plan
import kotlinx.coroutines.flow.Flow

interface DataRepository {

    fun observeLocations(): Flow<List<Place?>>
    fun observePlans(): Flow<List<Plan?>>
    fun observePlanLocations(planId: String): Flow<List<Place?>>
    fun getLocation(id: String): Flow<Place?>
    fun getPlan(id: String): Flow<Plan?>
    suspend fun addLocation(place: Place)
    suspend fun addLocationToPlan(planId: String, place: Place)
    suspend fun addPlan(title: String, color: String, emoji: String): Plan
    suspend fun removePlan(id: String)
}