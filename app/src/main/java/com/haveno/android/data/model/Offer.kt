package com.haveno.android.data.model

import java.math.BigDecimal
import java.util.Date

/**
 * Represents a trading offer in the Haveno marketplace
 */
data class Offer(
    val id: String,
    val direction: OfferDirection,
    val currencyCode: String,
    val price: BigDecimal,
    val amount: BigDecimal,
    val minAmount: BigDecimal,
    val maxAmount: BigDecimal,
    val paymentMethod: String,
    val makerFeePct: BigDecimal,
    val buyerSecurityDeposit: BigDecimal,
    val sellerSecurityDeposit: BigDecimal,
    val creationDate: Date,
    val isMyOffer: Boolean = false,
    val ownerNodeAddress: String,
    val pubKeyRing: String? = null,
    val versionNr: String? = null,
    val protocolVersion: Int = 1
) {
    
    /**
     * Gets the display price with currency formatting
     */
    fun getDisplayPrice(): String {
        return "${price.toPlainString()} $currencyCode"
    }
    
    /**
     * Gets the display amount in XMR
     */
    fun getDisplayAmount(): String {
        return "${amount.toPlainString()} XMR"
    }
    
    /**
     * Gets the display range if min amount differs from amount
     */
    fun getDisplayRange(): String {
        return if (minAmount < amount) {
            "${minAmount.toPlainString()} - ${amount.toPlainString()} XMR"
        } else {
            getDisplayAmount()
        }
    }
    
    /**
     * Calculates the volume in fiat currency
     */
    fun getVolume(): BigDecimal {
        return price.multiply(amount)
    }
    
    /**
     * Gets the minimum volume in fiat currency
     */
    fun getMinVolume(): BigDecimal {
        return price.multiply(minAmount)
    }
    
    /**
     * Checks if this is a buy offer (buying XMR with fiat)
     */
    fun isBuyOffer(): Boolean = direction == OfferDirection.BUY
    
    /**
     * Checks if this is a sell offer (selling XMR for fiat)
     */
    fun isSellOffer(): Boolean = direction == OfferDirection.SELL
}

enum class OfferDirection {
    BUY,  // Buying XMR with fiat
    SELL; // Selling XMR for fiat
    
    fun getDisplayName(): String = when (this) {
        BUY -> "Buy"
        SELL -> "Sell"
    }
}