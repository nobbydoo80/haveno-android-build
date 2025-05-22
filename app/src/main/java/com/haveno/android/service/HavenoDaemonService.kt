package com.haveno.android.service

import android.app.Service
import android.app.PendingIntent
import android.content.Intent
import android.os.IBinder
import android.os.Binder
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.haveno.android.HavenoApplication
import com.haveno.android.R
import com.haveno.android.data.repository.DaemonRepository
import com.haveno.android.ui.main.MainActivity
import com.haveno.android.util.ConnectionState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import timber.log.Timber
import javax.inject.Inject

/**
 * Foreground service that manages the Haveno daemon lifecycle
 * Runs the core Haveno functionality in the background
 */
@AndroidEntryPoint
class HavenoDaemonService : Service() {

    @Inject
    lateinit var daemonRepository: DaemonRepository

    @Inject
    lateinit var torService: TorService

    private val binder = DaemonBinder()
    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    
    private val _connectionState = MutableLiveData<ConnectionState>()
    val connectionState: LiveData<ConnectionState> = _connectionState
    
    private val _daemonStatus = MutableLiveData<DaemonStatus>()
    val daemonStatus: LiveData<DaemonStatus> = _daemonStatus
    
    private var isStarted = false
    private var heartbeatJob: Job? = null

    override fun onCreate() {
        super.onCreate()
        Timber.d("HavenoDaemonService created")
        _connectionState.postValue(ConnectionState.DISCONNECTED)
        _daemonStatus.postValue(DaemonStatus.STOPPED)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.d("HavenoDaemonService onStartCommand")
        
        when (intent?.action) {
            ACTION_START_DAEMON -> startDaemon()
            ACTION_STOP_DAEMON -> stopDaemon()
            ACTION_RESTART_DAEMON -> restartDaemon()
            else -> startDaemon()
        }
        
        return START_STICKY // Restart service if killed
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.d("HavenoDaemonService destroyed")
        cleanup()
    }

    private fun startDaemon() {
        if (isStarted) {
            Timber.d("Daemon already started")
            return
        }
        
        startForeground(HavenoApplication.DAEMON_NOTIFICATION_ID, createNotification())
        isStarted = true
        
        serviceScope.launch {
            try {
                _daemonStatus.postValue(DaemonStatus.STARTING)
                _connectionState.postValue(ConnectionState.CONNECTING)
                
                // Start Tor service first
                if (!torService.isRunning()) {
                    Timber.d("Starting Tor service")
                    torService.start()
                    torService.waitForConnection()
                }
                
                // Initialize and start daemon
                Timber.d("Starting Haveno daemon")
                daemonRepository.startDaemon()
                
                // Wait for daemon to be ready
                daemonRepository.waitForReady()
                
                _daemonStatus.postValue(DaemonStatus.RUNNING)
                _connectionState.postValue(ConnectionState.CONNECTED)
                
                // Start heartbeat monitoring
                startHeartbeat()
                
                Timber.i("Haveno daemon started successfully")
                
            } catch (e: Exception) {
                Timber.e(e, "Failed to start Haveno daemon")
                _daemonStatus.postValue(DaemonStatus.ERROR)
                _connectionState.postValue(ConnectionState.ERROR)
                
                // Stop service on error
                stopSelf()
            }
        }
    }

    private fun stopDaemon() {
        Timber.d("Stopping Haveno daemon")
        
        serviceScope.launch {
            try {
                _daemonStatus.postValue(DaemonStatus.STOPPING)
                _connectionState.postValue(ConnectionState.DISCONNECTING)
                
                // Stop heartbeat
                heartbeatJob?.cancel()
                
                // Stop daemon
                daemonRepository.stopDaemon()
                
                // Stop Tor service if no other services need it
                if (torService.isRunning() && !hasOtherTorDependents()) {
                    torService.stop()
                }
                
                _daemonStatus.postValue(DaemonStatus.STOPPED)
                _connectionState.postValue(ConnectionState.DISCONNECTED)
                
                Timber.i("Haveno daemon stopped successfully")
                
            } catch (e: Exception) {
                Timber.e(e, "Error stopping Haveno daemon")
            } finally {
                isStarted = false
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
            }
        }
    }

    private fun restartDaemon() {
        Timber.d("Restarting Haveno daemon")
        
        serviceScope.launch {
            stopDaemon()
            delay(2000) // Wait 2 seconds before restart
            startDaemon()
        }
    }

    private fun startHeartbeat() {
        heartbeatJob = serviceScope.launch {
            while (isActive && isStarted) {
                try {
                    val isHealthy = daemonRepository.checkHealth()
                    if (!isHealthy) {
                        Timber.w("Daemon health check failed")
                        _connectionState.postValue(ConnectionState.ERROR)
                        // Consider restarting daemon here
                    } else {
                        _connectionState.postValue(ConnectionState.CONNECTED)
                    }
                } catch (e: Exception) {
                    Timber.e(e, "Heartbeat error")
                    _connectionState.postValue(ConnectionState.ERROR)
                }
                
                delay(HavenoApplication.HEARTBEAT_INTERVAL_SECONDS * 1000)
            }
        }
    }

    private fun hasOtherTorDependents(): Boolean {
        // Check if other services or components need Tor
        // For now, assume daemon is the only Tor user
        return false
    }

    private fun createNotification() = NotificationCompat.Builder(this, HavenoApplication.DAEMON_CHANNEL_ID)
        .setContentTitle(getString(R.string.notification_daemon_title))
        .setContentText(getString(R.string.notification_daemon_text))
        .setSmallIcon(R.drawable.ic_haveno_notification)
        .setContentIntent(createPendingIntent())
        .setOngoing(true)
        .setShowWhen(false)
        .setLocalOnly(true)
        .build()

    private fun createPendingIntent(): PendingIntent {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        return PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun cleanup() {
        serviceScope.cancel()
        heartbeatJob?.cancel()
        isStarted = false
    }

    inner class DaemonBinder : Binder() {
        fun getService(): HavenoDaemonService = this@HavenoDaemonService
    }

    enum class DaemonStatus {
        STOPPED,
        STARTING,
        RUNNING,
        STOPPING,
        ERROR
    }

    companion object {
        const val ACTION_START_DAEMON = "com.haveno.android.START_DAEMON"
        const val ACTION_STOP_DAEMON = "com.haveno.android.STOP_DAEMON"
        const val ACTION_RESTART_DAEMON = "com.haveno.android.RESTART_DAEMON"
    }
}