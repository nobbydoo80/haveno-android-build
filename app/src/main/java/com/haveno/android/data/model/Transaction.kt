package com.haveno.android.data.model

import java.math.BigDecimal
import java.util.Date

/**
 * Represents a wallet transaction
 */
data class Transaction(
    val txId: String,
    val hash: String,
    val type: TransactionType,
    val direction: TransactionDirection,
    val amount: BigDecimal,
    val fee: BigDecimal?,
    val confirmations: Int,
    val blockHeight: Long?,
    val timestamp: Date,
    val memo: String?,
    val address: String?,
    val isConfirmed: Boolean,
    val tradingPeer: String? = null,
    val offerId: String? = null,
    val tradeId: String? = null
) {
    
    /**
     * Gets the display amount with proper formatting
     */
    fun getDisplayAmount(): String {
        val prefix = when (direction) {
            TransactionDirection.INCOMING -> "+"
            TransactionDirection.OUTGOING -> "-"
        }
        return "$prefix${amount.toPlainString()} XMR"
    }
    
    /**
     * Gets the display fee if available
     */
    fun getDisplayFee(): String? {
        return fee?.let { "${it.toPlainString()} XMR" }
    }
    
    /**
     * Gets the confirmation status text
     */
    fun getConfirmationStatus(): String {
        return when {
            !isConfirmed && confirmations == 0 -> "Unconfirmed"
            !isConfirmed -> "Pending ($confirmations/10)"
            else -> "Confirmed"
        }
    }
    
    /**
     * Gets the transaction type display name
     */
    fun getTypeDisplayName(): String = when (type) {
        TransactionType.DEPOSIT -> "Deposit"
        TransactionType.WITHDRAWAL -> "Withdrawal"
        TransactionType.TRADE_DEPOSIT -> "Trade Deposit"
        TransactionType.TRADE_PAYOUT -> "Trade Payout"
        TransactionType.OFFER_FEE -> "Offer Fee"
        TransactionType.TRADE_FEE -> "Trade Fee"
        TransactionType.DISPUTE_PAYOUT -> "Dispute Payout"
        TransactionType.UNKNOWN -> "Unknown"
    }
    
    /**
     * Gets a short transaction ID for display
     */
    fun getShortTxId(): String {
        return if (txId.length >= 16) {
            "${txId.substring(0, 8)}...${txId.substring(txId.length - 8)}"
        } else {
            txId
        }
    }
    
    /**
     * Checks if this transaction needs user attention
     */
    fun needsAttention(): Boolean {
        return !isConfirmed && confirmations == 0
    }
}

enum class TransactionType {
    DEPOSIT,
    WITHDRAWAL,
    TRADE_DEPOSIT,
    TRADE_PAYOUT,
    OFFER_FEE,
    TRADE_FEE,
    DISPUTE_PAYOUT,
    UNKNOWN
}

enum class TransactionDirection {
    INCOMING,
    OUTGOING
}