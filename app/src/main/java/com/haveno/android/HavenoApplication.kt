package com.haveno.android

import android.app.Application

/**
 * Simple application class for Haveno Android
 * This is a basic version for initial build testing
 */
class HavenoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // Basic initialization
    }

    companion object {
        // App constants
        const val TAG = "HavenoAndroid"
    }
}