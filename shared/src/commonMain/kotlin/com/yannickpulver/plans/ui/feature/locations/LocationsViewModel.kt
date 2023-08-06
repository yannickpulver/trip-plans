import com.yannickpulver.plans.data.FirebaseRepo
import com.yannickpulver.plans.data.GoogleMapsRepo
import com.yannickpulver.plans.ui.feature.locations.LocationsState
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LocationsViewModel(private val firebaseRepo: FirebaseRepo, private val googleMapsRepo: GoogleMapsRepo) : ViewModel() {

    private val _state = MutableStateFlow(LocationsState.Empty)
    val state = combine(_state, firebaseRepo.getLocations()) { state, plans ->
        state.copy(locations = plans.filterNotNull().reversed())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), LocationsState.Empty)

    private val _query = MutableStateFlow("")

    init {
        viewModelScope.launch {
            _query.debounce(300).collect(::fetchPredictions)
        }
    }

    private fun fetchPredictions(query: String) {
        if (query.isBlank() || query.length < 3) {
            _state.value = _state.value.copy(predictions = emptyList())
            return
        }

        viewModelScope.launch {
            val predictions = googleMapsRepo.fetchPredictions(query)
            _state.value = _state.value.copy(predictions = predictions)
        }
    }

    fun add(id: String) {
        viewModelScope.launch {
            val place = googleMapsRepo.fetchPlace(id)
            firebaseRepo.addLocation(place)
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