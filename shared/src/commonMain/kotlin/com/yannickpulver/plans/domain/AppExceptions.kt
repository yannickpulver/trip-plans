package com.yannickpulver.plans.domain

sealed class AppExceptions(override val message: String?, override val cause: Throwable?) :
    Exception(message, cause) {

    class NoFirebaseUserAvailable(message: String? = null, cause: Throwable? = null) :
        AppExceptions(message, cause)
}
