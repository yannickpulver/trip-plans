import com.yannickpulver.tripplans.BuildKonfig
import com.yannickpulver.tripplans.data.FirebaseRepo
import com.yannickpulver.tripplans.ui.feature.plans.PredictionDto
import com.yannickpulver.tripplans.ui.feature.plans.PlansState
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class PlansViewModel(private val firebaseRepo: FirebaseRepo) : ViewModel() {

    private val _state = MutableStateFlow(PlansState.Empty)
    val state = combine(_state, firebaseRepo.getPlans()) { state, plans ->
        state.copy(locations = plans?.items.orEmpty().reversed())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PlansState.Empty)

    private val _query = MutableStateFlow("")

    init {
        viewModelScope.launch {
            _query.debounce(300).collect(::getAutoComplete)
        }
    }

    fun add(location: String) {
        viewModelScope.launch {
            firebaseRepo.addPlan(location)
            updateQuery("")
        }
    }

    fun removePlan(plan: String) {
        viewModelScope.launch {
            firebaseRepo.removePlan(plan)
        }
    }

    fun updateQuery(query: String) {
        _query.value = query
    }

    private val client = HttpClient() {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }

    private fun getAutoComplete(location: String) {
        if (location.isBlank() || location.length < 3) {
            _state.value = _state.value.copy(predictions = emptyList())
            return
        }

        // TODO: Move this to appropriate arch/layer
        viewModelScope.launch {
            val results =
                client.get("https://maps.googleapis.com/maps/api/place/autocomplete/json?input=$location&key=${BuildKonfig.MAPS_API_KEY}")
            val response = results.body<PredictionDto>()

            response.predictions.forEach {
                println(it.description)
            }

            _state.value = _state.value.copy(predictions = response.predictions)
        }
    }
}