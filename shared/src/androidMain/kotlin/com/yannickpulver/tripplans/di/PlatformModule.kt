package com.yannickpulver.tripplans.di

import PlansViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun getPlatformModule(): Module = module {
    viewModel {
        PlansViewModel()
    }
}
