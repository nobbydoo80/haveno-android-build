package com.haveno.android.data.model

import java.math.BigDecimal

/**
 * Represents wallet balance information
 */
data class WalletBalance(
    val availableBalance: BigDecimal,
    val pendingBalance: BigDecimal,
    val reservedOfferBalance: BigDecimal,
    val reservedTradeBalance: BigDecimal,
    val totalBalance: BigDecimal,
    val unconfirmedBalance: BigDecimal
) {
    
    /**
     * Gets the display format for available balance
     */
    fun getDisplayAvailableBalance(): String {
        return "${availableBalance.toPlainString()} XMR"
    }
    
    /**
     * Gets the display format for total balance
     */
    fun getDisplayTotalBalance(): String {
        return "${totalBalance.toPlainString()} XMR"
    }
    
    /**
     * Gets the display format for pending balance
     */
    fun getDisplayPendingBalance(): String {
        return "${pendingBalance.toPlainString()} XMR"
    }
    
    /**
     * Gets the display format for reserved balance
     */
    fun getDisplayReservedBalance(): String {
        val totalReserved = reservedOfferBalance.add(reservedTradeBalance)
        return "${totalReserved.toPlainString()} XMR"
    }
    
    /**
     * Checks if there are any pending transactions
     */
    fun hasPendingTransactions(): Boolean {
        return pendingBalance > BigDecimal.ZERO || unconfirmedBalance > BigDecimal.ZERO
    }
    
    /**
     * Checks if there are any reserved funds
     */
    fun hasReservedFunds(): Boolean {
        return reservedOfferBalance > BigDecimal.ZERO || reservedTradeBalance > BigDecimal.ZERO
    }
}