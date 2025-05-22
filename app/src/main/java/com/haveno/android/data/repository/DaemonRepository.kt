package com.haveno.android.data.repository

import com.haveno.android.data.model.NodeInfo
import com.haveno.android.data.model.ConnectionStatus
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DaemonRepository @Inject constructor() {
    
    suspend fun startDaemon() {
        // Mock implementation - simulate starting Haveno daemon
        delay(1000)
    }
    
    suspend fun stopDaemon() {
        // Mock implementation - simulate stopping Haveno daemon
        delay(500)
    }
    
    suspend fun waitForReady() {
        // Mock implementation - simulate daemon becoming ready
        delay(2000)
    }
    
    suspend fun checkHealth(): Boolean {
        // Mock implementation - daemon is always healthy in demo
        return true
    }
    
    suspend fun getNodeInfo(): NodeInfo {
        // Mock node connection info
        delay(300)
        return NodeInfo(
            nodeAddress = "node.haveno.exchange:18081",
            isConnected = true,
            blockHeight = 3287642,
            peersConnected = 15,
            networkType = "mainnet",
            version = "0.18.3.1-release"
        )
    }
    
    suspend fun getConnectionStatus(): ConnectionStatus {
        delay(200)
        return ConnectionStatus(
            isConnectedToNode = true,
            isConnectedToPeers = true,
            blocksSynced = 3287642,
            blocksRemaining = 0,
            syncProgress = 100.0,
            connectionUptime = System.currentTimeMillis() - 3600000 // 1 hour ago
        )
    }
}