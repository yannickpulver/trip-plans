import com.yannickpulver.tripplans.data.FirebaseRepo
import com.yannickpulver.tripplans.ui.feature.plans.PlansState
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PlansViewModel(private val firebaseRepo: FirebaseRepo) : ViewModel() {

    val _state = MutableStateFlow(PlansState.Empty)

    val state = combine(_state, firebaseRepo.getPlans()) { state, plans ->
        state.copy(locations = plans?.items.orEmpty().reversed())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PlansState.Empty)

    fun add(location: String) {
        viewModelScope.launch {
            firebaseRepo.addPlan(location)
        }
    }

    fun removePlan(plan: String) {
        viewModelScope.launch {
            firebaseRepo.removePlan(plan)
        }
    }
}