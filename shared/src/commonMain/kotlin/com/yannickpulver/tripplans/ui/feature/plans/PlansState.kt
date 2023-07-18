package com.yannickpulver.tripplans.ui.feature.plans

data class PlansState(val plans: List<String> = emptyList()) {

    companion object {
        val Preview = PlansState(plans = listOf("Antarctica", "Falkland Islands", "Ilulissat", "Svalbard"))
        val Empty = PlansState()
    }
}
