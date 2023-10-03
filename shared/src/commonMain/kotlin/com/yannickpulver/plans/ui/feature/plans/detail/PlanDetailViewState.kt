package com.yannickpulver.plans.ui.feature.plans.detail

import com.yannickpulver.plans.data.dto.Place
import com.yannickpulver.plans.data.dto.PlacePrediction
import com.yannickpulver.plans.data.dto.Plan

data class PlanDetailViewState(
    val title: String = "",
    val imageUrl: String = "",
    val plan: Plan? = null,
    val locations: List<Place> = emptyList(),
    val predictions: List<PlacePrediction> = emptyList()
)
