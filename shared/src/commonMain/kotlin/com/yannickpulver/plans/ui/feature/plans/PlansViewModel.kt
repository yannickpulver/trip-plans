package com.yannickpulver.plans.ui.feature.plans

import com.yannickpulver.plans.data.FirebaseRepo
import com.yannickpulver.plans.data.dto.Plan
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class PlansViewModel(firebaseRepo: FirebaseRepo) : ViewModel() {

    val state: StateFlow<List<Plan>> = firebaseRepo.observePlans().map { it.filterNotNull() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}