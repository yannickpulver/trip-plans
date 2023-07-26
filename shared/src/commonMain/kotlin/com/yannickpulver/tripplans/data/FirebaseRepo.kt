package com.yannickpulver.tripplans.data

import com.yannickpulver.tripplans.data.dto.PlanDto
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.FieldValue
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
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class FirebaseRepo {

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

    suspend fun addPlan(name: String) {
        val user = getUser()
        val ref = db.collection("plans").document(user?.uid.orEmpty())
        val date = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        //val dateString = "${date.hour}:${date.minute}:${date.second}"

        if (!ref.get().exists) {
            ref.set(PlanDto(items = listOf(name)))
        } else {
            ref.update("items" to FieldValue.arrayUnion(name))
        }
    }

    fun getPlans(): Flow<PlanDto?> {
        return userFlow.flatMapLatest {
            it?.let { user ->
                val ref = db.collection("plans").document(user.uid)
                ref.snapshots.map { if (it.exists) it.data() else null }
            } ?: flowOf(null)

        }
    }

    suspend fun removePlan(plan: String) {
        val user = getUser()
        val ref = db.collection("plans").document(user?.uid.orEmpty())
        ref.update("items" to FieldValue.arrayRemove(plan))
    }
}