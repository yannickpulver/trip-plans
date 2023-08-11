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
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class FirebaseRepo constructor(private val json: Json) {

    private val auth = Firebase.auth
    private val db = Firebase.firestore

    private val userFlow: MutableStateFlow<FirebaseUser?> = MutableStateFlow(auth.currentUser)

    init {
        CoroutineScope(Dispatchers.IO).launch {
            getUser()
        }
    }

    suspend fun getUser(): FirebaseUser? {
        return (auth.currentUser ?: signInAnonymously()).apply { userFlow.value = this }
    }

    private suspend fun signInAnonymously(): FirebaseUser? {
        return auth.signInAnonymously().user
    }

    suspend fun addLocation(place: Place) {
        val user = getUser()
        val ref = db.collection("plans").document(user?.uid.orEmpty()).collection("locations")
            .document(place.id)
        ref.set(place)
    }

    fun getLocations(): Flow<List<Place?>> {
        return userFlow.flatMapLatest {
            it?.let { user ->
                val ref = db.collection("plans").document(user.uid).collection("locations")
                ref.snapshots.map { it.documents.map { it.data() } }
            } ?: flowOf(emptyList())
        }
    }

    fun getLocation(id: String) : Flow<Place?> {
        return userFlow.flatMapLatest {
            it?.let { user ->
                val ref = db.collection("plans").document(user.uid).collection("locations")
                    .document(id)
                ref.snapshots.map { it.data() }
            } ?: flowOf(null)
        }
    }


    suspend fun removePlan(id: String) {
        val user = getUser()
        val ref = db.collection("plans").document(user?.uid.orEmpty()).collection("locations")
            .document(id)
        ref.delete()
    }
}