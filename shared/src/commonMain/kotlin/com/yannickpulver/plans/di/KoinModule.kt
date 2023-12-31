package com.yannickpulver.plans.di

import LocationsViewModel
import com.yannickpulver.plans.data.remote.FirebaseDataRepository
import com.yannickpulver.plans.data.remote.GoogleMapsRepository
import com.yannickpulver.plans.domain.DataRepository
import com.yannickpulver.plans.domain.MapsRepository
import com.yannickpulver.plans.ui.feature.locations.detail.LocationDetailViewModel
import com.yannickpulver.plans.ui.feature.map.MapViewModel
import com.yannickpulver.plans.ui.feature.plans.PlansViewModel
import com.yannickpulver.plans.ui.feature.plans.add.AddPlanViewModel
import com.yannickpulver.plans.ui.feature.plans.detail.PlanDetailViewModel
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

            single<DataRepository> { FirebaseDataRepository() }
            single<MapsRepository> { GoogleMapsRepository(get()) }
            viewModelDefinition { LocationsViewModel(get(), get()) }
            viewModelDefinition { LocationDetailViewModel(get()) }
            viewModelDefinition { MapViewModel(get()) }
            viewModelDefinition { PlanDetailViewModel(get(), get()) }
            viewModelDefinition { PlansViewModel(get()) }
            viewModelDefinition { AddPlanViewModel(get()) }
        },
        getPlatformModule()
    )
}
