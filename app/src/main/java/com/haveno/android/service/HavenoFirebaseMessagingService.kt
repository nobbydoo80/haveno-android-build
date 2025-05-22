package com.haveno.android.service

import android.app.Service
import android.content.Intent
import android.os.IBinder

class HavenoFirebaseMessagingService : Service() {
    
    override fun onBind(intent: Intent?): IBinder? = null
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Mock Firebase messaging service - would handle push notifications
        return START_NOT_STICKY
    }
}