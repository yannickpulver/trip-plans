package com.yannickpulver.tripplans.data

import com.yannickpulver.tripplans.data.dto.PlanDto
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.FieldPath
import dev.gitlive.firebase.firestore.FieldValue
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

class FirebaseRepo {
    init {
        CoroutineScope(Dispatchers.IO).launch {
            getUser()
        }
    }

    suspend fun getUser(): FirebaseUser? {
        return Firebase.auth.currentUser ?: signInAnonymously()
    }

    private suspend fun signInAnonymously(): FirebaseUser? {
        return Firebase.auth.signInAnonymously().user
    }

    suspend fun addPlan(name: String) {
        val user = getUser()
        val ref = Firebase.firestore.collection("plans").document(user?.uid.orEmpty())

        if (!ref.get().exists) {
            ref.set(PlanDto(items = listOf(name)))
        } else {
            if (ref.get().data<PlanDto>().items.contains(name)) {
                ref.update("items" to FieldValue.arrayRemove(name))
            } else {
                ref.update("items" to FieldValue.arrayUnion(name))
            }
        }
    }
}