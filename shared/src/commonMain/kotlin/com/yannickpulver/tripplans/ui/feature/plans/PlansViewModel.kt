import com.yannickpulver.tripplans.data.FirebaseRepo
import com.yannickpulver.tripplans.ui.feature.plans.PlansState
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class PlansViewModel constructor(private val firebaseRepo: FirebaseRepo) : ViewModel() {

    val state = MutableStateFlow(PlansState.Preview)
}