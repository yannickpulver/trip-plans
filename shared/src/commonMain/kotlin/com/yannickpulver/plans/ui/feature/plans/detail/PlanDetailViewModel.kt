package com.yannickpulver.plans.ui.feature.plans.detail

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PlanDetailViewModel : ViewModel() {

    private val _title = MutableStateFlow("")
    private val _imageUrl = MutableStateFlow("")

    val state =
        combine(_title, _imageUrl) { title, imageUrl -> PlanDetailViewState(title, imageUrl) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PlanDetailViewState())

    init {
        viewModelScope.launch {
            _title.debounce(300).collect {
                if (it.isEmpty()) {
                    _imageUrl.value = ""
                } else {
                    _imageUrl.value = "https://source.unsplash.com/random/800x600?${it}"
                }
            }
        }
    }

    fun onTitleChanged(title: String) {
        _title.value = title
    }
}