package com.haveno.android.util

object Constants {
    // Service constants
    const val DAEMON_NOTIFICATION_ID = 1001
    const val DAEMON_CHANNEL_ID = "haveno_daemon_channel"
    const val HEARTBEAT_INTERVAL_SECONDS = 30L
    
    // Network constants
    const val DEFAULT_TOR_PORT = 9050
    const val DEFAULT_DAEMON_PORT = 8080
    
    // Preferences
    const val PREF_TOR_ENABLED = "tor_enabled"
    const val PREF_AUTO_START = "auto_start_daemon"
}