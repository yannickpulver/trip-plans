package com.yannickpulver.tripplans.ui.feature.plans

data class PlansState(val locations: List<String> = emptyList(), val predictions: List<PlacePrediction> = emptyList()) {

    companion object {
        val Preview = PlansState(locations = listOf("Antarctica", "Falkland Islands", "Ilulissat", "Svalbard"))
        val Empty = PlansState()
    }
}
