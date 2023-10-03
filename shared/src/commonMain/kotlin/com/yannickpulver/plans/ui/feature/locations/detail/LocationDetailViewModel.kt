package com.yannickpulver.plans.ui.feature.locations.detail

import com.yannickpulver.plans.data.FirebaseRepo
import com.yannickpulver.plans.data.dto.Place
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class LocationDetailViewModel(private val firebaseRepo: FirebaseRepo) : ViewModel() {

    private val _id = MutableStateFlow<String?>(null)
    private val _planId = MutableStateFlow<String?>(null)
    private val _state = MutableStateFlow<Place?>(null)

    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            combine(_id.filterNotNull(), _planId.filterNotNull()) { id, planId -> id to planId }
                .flatMapLatest { (id, planId) -> firebaseRepo.getLocation(id, planId) }
                .collect { _state.value = it }
        }
    }

    fun getLocation(id: String, planId: String) {
        _planId.value = planId
        _id.value = id
    }
}
