package com.yannickpulver.tripplans

import android.app.Application
import com.yannickpulver.tripplans.di.initKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }
}