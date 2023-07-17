package com.yannickpulver.tripplans.data

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.auth
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
}