package com.yannickpulver.plans.ui.feature.plans.detail

import com.yannickpulver.plans.data.FirebaseRepo
import com.yannickpulver.plans.data.GoogleMapsRepo
import com.yannickpulver.plans.data.dto.Place
import com.yannickpulver.plans.data.dto.PlacePrediction
import com.yannickpulver.plans.data.dto.Plan
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PlanDetailViewModel(
    private val firebaseRepo: FirebaseRepo,
    private val googleMapsRepo: GoogleMapsRepo
) : ViewModel() {

    private val _id = MutableStateFlow<String?>(null)
    private val _plan = MutableStateFlow<Plan?>(null)
    private val _locations = MutableStateFlow<List<Place>>(emptyList())
    private val _predictions = MutableStateFlow<List<PlacePrediction>>(emptyList())
    private val _query = MutableStateFlow("")

    val state =
        combine(
            _plan,
            _locations,
            _predictions
        ) { plan, locations, predictions ->
            PlanDetailViewState(
                plan = plan,
                locations = locations.sortedByDescending { plan?.locations?.get(it.id)?.creationDate ?: 0 },
                predictions = predictions
            )
        }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PlanDetailViewState())

    init {
        viewModelScope.launch {
            _id.filterNotNull()
                .flatMapLatest { firebaseRepo.getPlan(it) }
                .collect { _plan.value = it }
        }

        viewModelScope.launch {
            _id.filterNotNull()
                .flatMapLatest { firebaseRepo.observePlanLocations(it) }
                .collect { _locations.value = it.filterNotNull() }
        }

        viewModelScope.launch {
            _query.debounce(300).collect(::fetchPredictions)
        }
    }

    fun setId(id: String?) {
        _id.value = id
    }

    private fun fetchPredictions(query: String) {
        if (query.isBlank() || query.length < 3) {
            _predictions.value = emptyList()
            return
        }

        viewModelScope.launch {
            val predictions = googleMapsRepo.fetchPredictions(query)
            _predictions.value = predictions
        }
    }

    fun addLocation(id: String) {
        viewModelScope.launch {
            val place = googleMapsRepo.fetchPlace(id)
            firebaseRepo.addLocationToPlan(_id.value.orEmpty(), place)
            updateQuery("")
        }
    }

    fun remove(id: String) {
        viewModelScope.launch {
            firebaseRepo.removePlan(id)
        }
    }

    fun updateQuery(query: String) {
        _query.value = query
    }
}
