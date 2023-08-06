package com.yannickpulver.plans.di

import LocationsViewModel
import com.yannickpulver.plans.data.FirebaseRepo
import com.yannickpulver.plans.data.GoogleMapsRepo
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
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
            single {
                Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                }
            }
            single {
                HttpClient {
                    install(ContentNegotiation) {
                        json(get())
                    }
                }
            }

            single { FirebaseRepo(get()) }
            single { GoogleMapsRepo(get()) }
            viewModelDefinition<LocationsViewModel> { LocationsViewModel(get(), get()) }
        },
        getPlatformModule()
    )
}

