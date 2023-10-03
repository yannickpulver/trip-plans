package com.yannickpulver.plans.ui.feature.map

import com.yannickpulver.plans.data.FirebaseRepo
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MapViewModel(firebaseRepo: FirebaseRepo) : ViewModel() {

    val state = firebaseRepo.observeLocations().map {
        MapState(it.filterNotNull())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MapState())
}
