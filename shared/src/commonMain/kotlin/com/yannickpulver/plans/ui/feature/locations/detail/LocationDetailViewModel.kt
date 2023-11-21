package com.yannickpulver.plans.ui.feature.locations.detail

import com.yannickpulver.plans.data.dto.Place
import com.yannickpulver.plans.domain.DataRepository
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class LocationDetailViewModel(private val dataRepository: DataRepository) : ViewModel() {

    private val _id = MutableStateFlow<String?>(null)
    private val _state = MutableStateFlow<Place?>(null)

    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _id.filterNotNull()
                .flatMapLatest { dataRepository.getLocation(it) }
                .collect { _state.value = it }
        }
    }

    fun getLocation(id: String) {
        _id.value = id
    }
}
