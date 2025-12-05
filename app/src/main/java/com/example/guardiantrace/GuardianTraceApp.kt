package com.example.guardiantrace

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GuardianTraceApp : Application() {

    override fun onCreate() {
        super.onCreate()

        initializeSecurity()
    }

    private fun initializeSecurity() {
        // Disable screenshots in production
        // Will be implemented in MainActivity
    }
}