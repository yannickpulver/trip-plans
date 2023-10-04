package com.yannickpulver.plans.ui.feature.plans.add

import com.yannickpulver.plans.ui.feature.plans.detail.PlanStyle

data class AddPlanViewState (
    val title: String = "",
    val style: PlanStyle? = null,
)