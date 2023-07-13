import com.yannickpulver.tripplans.ui.feature.plans.PlansState
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class PlansViewModel() : ViewModel() {

    val state = MutableStateFlow(PlansState())

}