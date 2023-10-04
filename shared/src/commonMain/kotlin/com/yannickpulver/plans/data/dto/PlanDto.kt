package com.yannickpulver.plans.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class PlanDto(
    val title: String = "",
    val color: String = "",
    val icon: String = "",
    val locations: Map<String, PlanLocation> = emptyMap()
)

@Serializable
data class Plan(
    val id: String = "",
    val title: String = "",
    val color: String = "",
    val icon: String = "",
    val locations: Map<String, PlanLocation> = emptyMap(),
)

@Serializable
data class PlanLocation(
    val comment: String,
    val creationDate: Long
)