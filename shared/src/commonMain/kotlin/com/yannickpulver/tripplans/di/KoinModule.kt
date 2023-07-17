package com.yannickpulver.tripplans.di

import PlansViewModel
import com.yannickpulver.tripplans.data.FirebaseRepo
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.module

fun initKoin(extras: KoinApplication.() -> Unit): KoinApplication {
    return startKoin(extras = extras)
}

// iOS
fun initKoin(): KoinApplication {
    return startKoin(extras = {})
}

private fun startKoin(extras: KoinApplication.() -> Unit) = startKoin {
    extras()
    modules(
        module {
            single { FirebaseRepo() }


            viewModelDefinition<PlansViewModel> { PlansViewModel(get()) }
        },
        getPlatformModule()
    )
}


