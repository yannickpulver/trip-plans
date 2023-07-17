package com.yannickpulver.tripplans.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class PlanDto(
    val items: List<String> = emptyList()
)

