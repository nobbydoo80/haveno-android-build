package com.haveno.android.data.model

data class NodeInfo(
    val nodeAddress: String,
    val isConnected: Boolean,
    val blockHeight: Long,
    val peersConnected: Int,
    val networkType: String,
    val version: String
)