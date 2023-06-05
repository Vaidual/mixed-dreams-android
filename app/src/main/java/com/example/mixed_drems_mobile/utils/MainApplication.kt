package com.example.mixed_drems_mobile.utils

import android.app.Application
import android.content.Context

class MainApplication : Application() {
    companion object {
        var context: Context? = null
    }

    override fun onCreate() {
        super.onCreate()

        MainApplication.context = applicationContext
    }
}