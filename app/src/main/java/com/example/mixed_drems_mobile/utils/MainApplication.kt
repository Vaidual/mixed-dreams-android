package com.example.mixed_drems_mobile.utils

import android.app.Application
import android.content.Context

class MainApplication : Application() {
    companion object {
        lateinit var instance: MainApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}