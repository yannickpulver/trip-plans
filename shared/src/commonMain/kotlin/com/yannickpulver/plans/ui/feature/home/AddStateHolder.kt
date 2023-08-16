package com.yannickpulver.plans.ui.feature.home

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object AddStateHolder {
    private val _state = MutableStateFlow(value = false)
    val state = _state.asStateFlow()

    fun show(show: Boolean) {
        _state.value = show
    }
}
