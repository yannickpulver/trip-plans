package com.yannickpulver.plans.ui.feature.map

import com.yannickpulver.plans.domain.DataRepository
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MapViewModel(dataRepository: DataRepository) : ViewModel() {

    val state = dataRepository.observeLocations().map {
        MapState(it.filterNotNull())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MapState())
}
