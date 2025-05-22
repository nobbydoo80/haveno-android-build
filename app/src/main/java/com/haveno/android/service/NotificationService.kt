package com.haveno.android.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationService @Inject constructor() : Service() {
    
    override fun onBind(intent: Intent?): IBinder? = null
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Mock notification service - would handle notifications
        return START_NOT_STICKY
    }
}