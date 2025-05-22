package com.haveno.android.util

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CrashReporter @Inject constructor() {
    
    fun initialize() {
        // Stub implementation - would initialize crash reporting
    }
    
    fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        // Stub implementation - would log to crash reporting service
    }
    
    fun recordException(e: Exception) {
        // Stub implementation - would record exception
    }
}