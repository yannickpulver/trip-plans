package com.yannickpulver.plans.data

import com.benasher44.uuid.uuid4
import com.yannickpulver.plans.data.dto.Place
import com.yannickpulver.plans.data.dto.Plan
import com.yannickpulver.plans.data.dto.PlanDto
import com.yannickpulver.plans.domain.AppExceptions
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
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
        val ref = db.collection("plans").document(uid).collection("locations")
            .document(place.id)
        ref.set(place)
    }

    suspend fun addLocationToPlan(id: String, place: Place) {
        val uid = userId.firstOrNull() ?: throw AppExceptions.NoFirebaseUserAvailable()
        val ref = db.collection("plans").document(uid).collection("plans").document(id)
            .collection("locations")
            .document(place.id)
        ref.set(place)
    }

    fun observeLocations(): Flow<List<Place?>> {
        return userId.filterNotNull().filter { it.isNotEmpty() }.flatMapLatest { uid ->
            val ref = db.collection("plans").document(uid).collection("locations")
            ref.snapshots.map { it.documents.map { it.data() } }
        }
    }

    fun observePlans(): Flow<List<Plan?>> {
        return userId.filterNotNull().filter { it.isNotEmpty() }.flatMapLatest { uid ->
            val ref = db.collection("plans").document(uid).collection("plans")
            ref.snapshots.map { it.documents.map { it.data<Plan>().copy(id = it.id) } }
        }
    }

    fun getLocation(id: String, planId: String): Flow<Place?> {
        return userId.filterNotNull().flatMapLatest { uid ->
            val ref = db.collection("plans")
                .document(uid)
                .collection("plans")
                .document(planId)
                .collection("locations")
                .document(id)
            ref.snapshots.map { it.data() }
        }
    }

    fun getPlan(id: String): Flow<Plan?> {
        return userId.filterNotNull().flatMapLatest { uid ->
            val ref = db.collection("plans").document(uid).collection("plans").document(id)
            ref.snapshots.map { it.data<Plan>().copy(id = it.id) }
        }
    }

    fun observePlanLocations(id: String): Flow<List<Place?>> {
        return userId.filterNotNull().flatMapLatest { uid ->
            val ref = db.collection("plans")
                .document(uid)
                .collection("plans")
                .document(id)
                .collection("locations")
            ref.snapshots.map { it.documents.map { it.data() } }
        }
    }

    suspend fun removePlan(id: String) {
        val uid = userId.firstOrNull() ?: throw AppExceptions.NoFirebaseUserAvailable()
        val ref = db.collection("plans").document(uid).collection("locations")
            .document(id)
        ref.delete()
    }

    suspend fun addPlan(title: String): Plan {
        val uid = userId.firstOrNull() ?: throw AppExceptions.NoFirebaseUserAvailable()
        val planId = uuid4().toString()
        val dto = PlanDto(title)
        val ref = db.collection("plans").document(uid).collection("plans").document(planId)
        ref.set(dto)
        return Plan(planId, title)
    }
}
