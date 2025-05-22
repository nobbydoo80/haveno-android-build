package com.haveno.android

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import com.haveno.android.util.CrashHandler

/**
 * Haveno Android Application with Hilt setup
 */
@HiltAndroidApp
class HavenoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        
        try {
            // Initialize Timber for logging
            if (BuildConfig.DEBUG) {
                Timber.plant(Timber.DebugTree())
            }
            
            Timber.d("=== HAVENO APPLICATION STARTING ===")
            Timber.d("Application onCreate() called")
            Timber.d("BuildConfig.DEBUG: ${BuildConfig.DEBUG}")
            Timber.d("Package name: ${packageName}")
            Timber.d("Version: ${BuildConfig.VERSION_NAME}")
            
            // Log Hilt initialization
            Timber.d("Hilt initialization starting...")
            
            // Set up global crash handler
            Thread.setDefaultUncaughtExceptionHandler(CrashHandler(this))
            Timber.d("Global crash handler installed")
            
            Timber.d("=== HAVENO APPLICATION STARTED SUCCESSFULLY ===")
            
        } catch (e: Exception) {
            Timber.e(e, "CRITICAL ERROR in Application.onCreate()")
            // Still throw to see the crash
            throw e
        }
    }

    companion object {
        const val TAG = "HavenoAndroid"
    }
}