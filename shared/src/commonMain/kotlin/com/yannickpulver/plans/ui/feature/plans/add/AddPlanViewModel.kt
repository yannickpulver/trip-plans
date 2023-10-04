package com.yannickpulver.plans.ui.feature.plans.add

import com.yannickpulver.plans.data.FirebaseRepo
import com.yannickpulver.plans.domain.util.randomEmoji
import com.yannickpulver.plans.domain.util.randomPastelColor
import com.yannickpulver.plans.ui.feature.plans.detail.PlanStyle
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AddPlanViewModel(
    private val firebaseRepo: FirebaseRepo,
) : ViewModel() {

    private val _title = MutableStateFlow("")
    private val _style = MutableStateFlow<PlanStyle?>(null)

    private val _events = MutableSharedFlow<AddPlanEvent>()
    val events get() = _events.asSharedFlow()

    val state =
        combine(
            _title,
            _style
        ) { title, style ->
            AddPlanViewState(
                title = title,
                style = style
            )
        }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AddPlanViewState())

    init {
        updateStyle()
    }

    fun onTitleChanged(title: String) {
        _title.value = title
    }

    fun save() {
        viewModelScope.launch {
            val style = _style.value ?: updateStyle()
            val plan = firebaseRepo.addPlan(_title.value, style.color, style.emoji)
            _events.emit(AddPlanEvent.Success(plan.id))
        }
    }

    fun updateStyle(): PlanStyle {
        val style = PlanStyle(
            emoji = randomEmoji(),
            color = randomPastelColor()
        )
        _style.value = style
        return style
    }
}