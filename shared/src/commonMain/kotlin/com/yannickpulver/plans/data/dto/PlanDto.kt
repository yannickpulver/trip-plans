package com.yannickpulver.plans.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class PlanDto(
    val title: String = "",
    val locations: List<String> = emptyList()
)

@Serializable
data class Plan(
    val id: String = "",
    val title: String = "",
    val locations: List<String> = emptyList()
)
