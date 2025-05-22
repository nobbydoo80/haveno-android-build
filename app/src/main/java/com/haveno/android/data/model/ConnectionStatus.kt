package com.haveno.android.data.model

data class ConnectionStatus(
    val isConnectedToNode: Boolean,
    val isConnectedToPeers: Boolean,
    val blocksSynced: Long,
    val blocksRemaining: Long,
    val syncProgress: Double, // 0.0 to 100.0
    val connectionUptime: Long // milliseconds since connected
)