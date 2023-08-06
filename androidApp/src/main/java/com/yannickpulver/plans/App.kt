package com.yannickpulver.plans

import android.app.Application
import com.yannickpulver.plans.di.initKoin
import org.koin.android.ext.koin.androidContext

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(androidContext = this@App)
        }
    }
}