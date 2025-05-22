package com.haveno.android.data.model

import java.math.BigDecimal
import java.util.Date

/**
 * Represents an active trade in the Haveno marketplace
 */
data class Trade(
    val id: String,
    val offerId: String,
    val shortId: String, // First 8 characters of ID for display
    val role: TradeRole,
    val state: TradeState,
    val phase: TradePhase,
    val amount: BigDecimal,
    val price: BigDecimal,
    val volume: BigDecimal,
    val currencyCode: String,
    val paymentMethod: String,
    val tradeDate: Date,
    val contractAsJson: String? = null,
    val tradingPeerNodeAddress: String,
    val tradingPeerPubKeyRing: String? = null,
    val isDepositPublished: Boolean = false,
    val isDepositConfirmed: Boolean = false,
    val isPaymentSent: Boolean = false,
    val isPaymentReceived: Boolean = false,
    val isCompleted: Boolean = false,
    val errorMessage: String? = null
) {
    
    /**
     * Gets a user-friendly display of the trade amount and currency
     */
    fun getDisplayAmount(): String {
        return "${amount.toPlainString()} XMR"
    }
    
    /**
     * Gets a user-friendly display of the trade volume
     */
    fun getDisplayVolume(): String {
        return "${volume.toPlainString()} $currencyCode"
    }
    
    /**
     * Gets a user-friendly display of the trade price
     */
    fun getDisplayPrice(): String {
        return "${price.toPlainString()} $currencyCode/XMR"
    }
    
    /**
     * Gets the current status for display
     */
    fun getStatusText(): String = when (state) {
        TradeState.PREPARATION -> "Preparing"
        TradeState.TAKER_FEE_PUBLISHED -> "Fee Published"
        TradeState.MAKER_SENT_PUBLISH_DEPOSIT_TX_REQUEST -> "Deposit Requested"
        TradeState.MAKER_SAW_ARRIVED_PUBLISH_DEPOSIT_TX_REQUEST -> "Deposit Seen"
        TradeState.MAKER_STORED_IN_MAILBOX_PUBLISH_DEPOSIT_TX_REQUEST -> "Deposit Stored"
        TradeState.ARBITRATOR_SENT_PUBLISH_DEPOSIT_TX_REQUEST -> "Arbitrator Requested"
        TradeState.DEPOSIT_TX_PUBLISHED_IN_BLOCKCHAIN -> "Deposit Published"
        TradeState.DEPOSIT_TX_CONFIRMED_IN_BLOCKCHAIN -> "Deposit Confirmed"
        TradeState.BUYER_CONFIRMED_IN_UI_PAYMENT_SENT -> "Payment Sent"
        TradeState.BUYER_SENT_PAYMENT_SENT_MSG -> "Payment Message Sent"
        TradeState.SELLER_SAW_ARRIVED_PAYMENT_SENT_MSG -> "Payment Received"
        TradeState.SELLER_CONFIRMED_IN_UI_PAYMENT_RECEIPT -> "Payment Confirmed"
        TradeState.SELLER_SENT_PAYMENT_RECEIVED_MSG -> "Release Initiated"
        TradeState.PAYOUT_TX_PUBLISHED_IN_BLOCKCHAIN -> "Payout Published"
        TradeState.TRADE_COMPLETED -> "Completed"
    }
    
    /**
     * Checks if the trade is in a final state
     */
    fun isFinal(): Boolean = state == TradeState.TRADE_COMPLETED
    
    /**
     * Checks if payment can be sent (buyer's perspective)
     */
    fun canSendPayment(): Boolean = 
        isDepositConfirmed && !isPaymentSent && role == TradeRole.BUYER
    
    /**
     * Checks if payment can be confirmed (seller's perspective)
     */
    fun canConfirmPayment(): Boolean = 
        isPaymentSent && !isPaymentReceived && role == TradeRole.SELLER
}

enum class TradeRole {
    BUYER,  // Buying XMR
    SELLER; // Selling XMR
    
    fun getDisplayName(): String = when (this) {
        BUYER -> "Buyer"
        SELLER -> "Seller"
    }
}

enum class TradeState {
    PREPARATION,
    TAKER_FEE_PUBLISHED,
    MAKER_SENT_PUBLISH_DEPOSIT_TX_REQUEST,
    MAKER_SAW_ARRIVED_PUBLISH_DEPOSIT_TX_REQUEST,
    MAKER_STORED_IN_MAILBOX_PUBLISH_DEPOSIT_TX_REQUEST,
    ARBITRATOR_SENT_PUBLISH_DEPOSIT_TX_REQUEST,
    DEPOSIT_TX_PUBLISHED_IN_BLOCKCHAIN,
    DEPOSIT_TX_CONFIRMED_IN_BLOCKCHAIN,
    BUYER_CONFIRMED_IN_UI_PAYMENT_SENT,
    BUYER_SENT_PAYMENT_SENT_MSG,
    SELLER_SAW_ARRIVED_PAYMENT_SENT_MSG,
    SELLER_CONFIRMED_IN_UI_PAYMENT_RECEIPT,
    SELLER_SENT_PAYMENT_RECEIVED_MSG,
    PAYOUT_TX_PUBLISHED_IN_BLOCKCHAIN,
    TRADE_COMPLETED
}

enum class TradePhase {
    INIT,
    DEPOSIT_PUBLISHED,
    DEPOSIT_CONFIRMED,
    PAYMENT_SENT,
    PAYMENT_RECEIVED,
    COMPLETED
}