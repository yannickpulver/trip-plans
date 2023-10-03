package com.yannickpulver.plans.data

import com.benasher44.uuid.uuid4
import com.yannickpulver.plans.data.dto.Place
import com.yannickpulver.plans.data.dto.Plan
import com.yannickpulver.plans.data.dto.PlanDto
import com.yannickpulver.plans.data.util.randomEmoji
import com.yannickpulver.plans.data.util.randomPastelColor
import com.yannickpulver.plans.domain.AppExceptions
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.DocumentReference
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.firestore.orderBy
import dev.gitlive.firebase.firestore.where
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

class FirebaseRepo {

    private val auth = Firebase.auth
    private val db = Firebase.firestore

    private val userFlow: MutableStateFlow<FirebaseUser?> = MutableStateFlow(auth.currentUser)
    private val userId get() = userFlow.map { it?.uid.orEmpty() } // flowOf("1234")

    init {
        CoroutineScope(Dispatchers.IO).launch {
            getUser()
        }
    }

    private suspend fun getUser(): FirebaseUser? {
        return (auth.currentUser ?: signInAnonymously()).apply { userFlow.value = this }
    }

    private suspend fun signInAnonymously(): FirebaseUser? {
        return auth.signInAnonymously().user
    }

    suspend fun addLocation(place: Place) {
        val uid = userId.firstOrNull() ?: throw AppExceptions.NoFirebaseUserAvailable()
        locationRef(uid, place.id).set(place)
    }

    suspend fun addLocationToPlan(planId: String, place: Place) {
        val uid = userId.firstOrNull() ?: throw AppExceptions.NoFirebaseUserAvailable()
        locationRef(uid, place.id).set(place)

        val locationData = mapOf(
            "comment" to "Some Comment",
            "creationDate" to Clock.System.now().epochSeconds
        )

        planRef(uid, planId).update("locations.${place.id}" to locationData)
    }

    fun observeLocations(): Flow<List<Place?>> {
        return userId.filterNotNull().filter { it.isNotEmpty() }.flatMapLatest { uid ->
            locationsRef(uid).snapshots.map { it.documents.map { it.data() } }
        }
    }

    fun observePlans(): Flow<List<Plan?>> {
        return userId.filterNotNull().filter { it.isNotEmpty() }.flatMapLatest { uid ->
            plansRef(uid).snapshots.map { it.documents.map { it.data<Plan>().copy(id = it.id) } }
        }
    }

    fun getLocation(id: String): Flow<Place?> {
        return userId.filterNotNull().flatMapLatest { uid ->
            locationRef(uid, id).snapshots.map { it.data() }
        }
    }

    fun getPlan(id: String): Flow<Plan?> {
        return userId.filterNotNull().flatMapLatest { uid ->
            val ref = planRef(uid, id)
            ref.snapshots.map { it.data<Plan>().copy(id = it.id) }
        }
    }

    fun observePlanLocations(planId: String): Flow<List<Place?>> {
        return userId.filterNotNull().flatMapLatest { uid ->
            val planIdRef = planRef(uid, planId)

            planIdRef.snapshots.flatMapLatest {
                val locations = it.data<Plan>().locations.keys.toList().ifEmpty { listOf("") }
                locationsRef(uid).where("reference", locations).snapshots.map { it.documents.map { it.data() } }
            }
        }
    }

    suspend fun removePlan(id: String) {
        val uid = userId.firstOrNull() ?: throw AppExceptions.NoFirebaseUserAvailable()
        locationRef(uid, id).delete()
    }

    suspend fun addPlan(title: String): Plan {
        val uid = userId.firstOrNull() ?: throw AppExceptions.NoFirebaseUserAvailable()
        val planId = uuid4().toString()
        val dto = PlanDto(title = title, color = randomPastelColor(), icon = randomEmoji())
        val ref = planRef(uid, planId)
        ref.set(dto)
        return ref.get().data<Plan>().copy(id = planId)
    }

    private fun uidRef(uid: String) = db.collection("plans").document(uid)

    private fun planRef(uid: String, planId: String): DocumentReference {
        return plansRef(uid).document(planId)
    }

    private fun plansRef(uid: String) = uidRef(uid).collection("plans")
    private fun locationRef(uid: String, locationId: String): DocumentReference {
        return locationsRef(uid).document(locationId)
    }

    private fun locationsRef(uid: String) = uidRef(uid).collection("locations")
}
