package com.yannickpulver.plans.data

import com.yannickpulver.plans.data.dto.Place
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
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
        val uid = userId.firstOrNull()
        val ref = db.collection("plans").document(uid.orEmpty()).collection("locations")
            .document(place.id)
        ref.set(place)
    }

    fun getLocations(): Flow<List<Place?>> {
        return userId.flatMapLatest { uid ->
            val ref = db.collection("plans").document(uid).collection("locations")
            ref.snapshots.map { it.documents.map { it.data() } }
        }
    }

    fun getLocation(id: String): Flow<Place?> {
        return userId.flatMapLatest { uid ->
            val ref = db.collection("plans").document(uid).collection("locations")
                .document(id)
            ref.snapshots.map { it.data() }
        }
    }

    suspend fun removePlan(id: String) {
        val uid = userId.firstOrNull()
        val ref = db.collection("plans").document(uid.orEmpty()).collection("locations")
            .document(id)
        ref.delete()
    }
}
