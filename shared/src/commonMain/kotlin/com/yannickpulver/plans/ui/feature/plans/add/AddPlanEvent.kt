package com.yannickpulver.plans.ui.feature.plans.add

sealed class AddPlanEvent {
    data class Success(val id: String) : AddPlanEvent()
}