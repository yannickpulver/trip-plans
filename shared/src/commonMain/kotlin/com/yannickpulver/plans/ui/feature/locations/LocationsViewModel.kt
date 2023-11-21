import com.yannickpulver.plans.domain.DataRepository
import com.yannickpulver.plans.domain.MapsRepository
import com.yannickpulver.plans.ui.feature.locations.LocationsState
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LocationsViewModel(private val dataRepository: DataRepository, private val mapsRepo: MapsRepository) : ViewModel() {

    private val _state = MutableStateFlow(LocationsState.Empty)
    val state = combine(_state, dataRepository.observeLocations()) { state, plans ->
        state.copy(locations = plans.filterNotNull().reversed())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), LocationsState.Empty)

    private val _query = MutableStateFlow("")

    init {
        viewModelScope.launch {
            _query.collect(::fetchPredictions)
        }
    }

    private fun fetchPredictions(query: String) {
        if (query.isBlank() || query.length < 3) {
            _state.value = _state.value.copy(predictions = emptyList())
            return
        }

        viewModelScope.launch {
            val predictions = mapsRepo.fetchPredictions(query)
            _state.update { LocationsState(predictions = predictions) }
        }
    }

    fun add(id: String) {
        viewModelScope.launch {
            val place = mapsRepo.fetchPlace(id)
            dataRepository.addLocation(place)
            updateQuery("")
        }
    }

    fun remove(id: String) {
        viewModelScope.launch {
            dataRepository.removePlan(id)
        }
    }

    fun updateQuery(query: String) {
        _query.value = query
    }
}
