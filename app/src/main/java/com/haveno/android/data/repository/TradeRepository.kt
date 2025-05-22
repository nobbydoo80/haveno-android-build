package com.haveno.android.data.repository

import com.haveno.android.data.model.*
import kotlinx.coroutines.delay
import java.math.BigDecimal
import java.util.*
import kotlin.random.Random

/**
 * Repository for trade data and operations
 * Currently provides sample data, will be connected to real Haveno daemon
 */
class TradeRepository {
    
    /**
     * Get active trades
     * TODO: Replace with real Haveno daemon gRPC calls
     */
    suspend fun getActiveTrades(): List<Trade> {
        delay(1000) // Simulate network delay
        
        return generateSampleTrades()
    }
    
    /**
     * Get trade by ID
     * TODO: Connect to Haveno daemon
     */
    suspend fun getTrade(tradeId: String): Trade? {
        delay(500)
        return generateSampleTrades().find { it.id == tradeId }
    }
    
    /**
     * Send payment confirmation
     * TODO: Connect to Haveno daemon
     */
    suspend fun confirmPaymentSent(tradeId: String): Result<Boolean> {
        return try {
            delay(2000)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Confirm payment received
     * TODO: Connect to Haveno daemon
     */
    suspend fun confirmPaymentReceived(tradeId: String): Result<Boolean> {
        return try {
            delay(2000)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private fun generateSampleTrades(): List<Trade> {
        val trades = mutableListOf<Trade>()
        val now = System.currentTimeMillis()
        
        val currencies = listOf("USD", "EUR", "GBP", "CAD")
        val paymentMethods = listOf("Bank Transfer", "PayPal", "Zelle", "Wise", "Cash")
        val tradeStates = listOf(
            TradeState.DEPOSIT_TX_CONFIRMED_IN_BLOCKCHAIN,
            TradeState.BUYER_CONFIRMED_IN_UI_PAYMENT_SENT,
            TradeState.SELLER_SAW_ARRIVED_PAYMENT_SENT_MSG,
            TradeState.PAYOUT_TX_PUBLISHED_IN_BLOCKCHAIN,
            TradeState.TRADE_COMPLETED
        )
        
        repeat(6) { i ->
            val role = if (i % 2 == 0) TradeRole.BUYER else TradeRole.SELLER
            val currency = currencies.random()
            val state = tradeStates.random()
            
            val amount = BigDecimal(Random.nextDouble(0.1, 5.0)).setScale(4, BigDecimal.ROUND_HALF_UP)
            val basePrice = when (currency) {
                "USD" -> BigDecimal("150.00")
                "EUR" -> BigDecimal("140.00")
                "GBP" -> BigDecimal("120.00")
                "CAD" -> BigDecimal("200.00")
                else -> BigDecimal("150.00")
            }
            val priceVariation = Random.nextDouble(-0.05, 0.05)
            val price = basePrice.multiply(BigDecimal(1.0 + priceVariation)).setScale(2, BigDecimal.ROUND_HALF_UP)
            val volume = price.multiply(amount).setScale(2, BigDecimal.ROUND_HALF_UP)
            
            val tradeId = "trade_${Random.nextInt(10000, 99999)}"
            val shortId = tradeId.takeLast(8)
            
            trades.add(
                Trade(
                    id = tradeId,
                    offerId = "offer_${Random.nextInt(1000, 9999)}",
                    shortId = shortId,
                    role = role,
                    state = state,
                    phase = determinePhase(state),
                    amount = amount,
                    price = price,
                    volume = volume,
                    currencyCode = currency,
                    paymentMethod = paymentMethods.random(),
                    tradeDate = Date(now - (i * 3600000L) - Random.nextLong(3600000)), // Last few hours
                    tradingPeerNodeAddress = "peer_node_${Random.nextInt(1000, 9999)}",
                    isDepositPublished = state.ordinal >= TradeState.DEPOSIT_TX_PUBLISHED_IN_BLOCKCHAIN.ordinal,
                    isDepositConfirmed = state.ordinal >= TradeState.DEPOSIT_TX_CONFIRMED_IN_BLOCKCHAIN.ordinal,
                    isPaymentSent = state.ordinal >= TradeState.BUYER_CONFIRMED_IN_UI_PAYMENT_SENT.ordinal,
                    isPaymentReceived = state.ordinal >= TradeState.SELLER_SAW_ARRIVED_PAYMENT_SENT_MSG.ordinal,
                    isCompleted = state == TradeState.TRADE_COMPLETED
                )
            )
        }
        
        // Sort by trade date (newest first)
        return trades.sortedByDescending { it.tradeDate }
    }
    
    private fun determinePhase(state: TradeState): TradePhase {
        return when (state) {
            TradeState.PREPARATION,
            TradeState.TAKER_FEE_PUBLISHED,
            TradeState.MAKER_SENT_PUBLISH_DEPOSIT_TX_REQUEST,
            TradeState.MAKER_SAW_ARRIVED_PUBLISH_DEPOSIT_TX_REQUEST,
            TradeState.MAKER_STORED_IN_MAILBOX_PUBLISH_DEPOSIT_TX_REQUEST,
            TradeState.ARBITRATOR_SENT_PUBLISH_DEPOSIT_TX_REQUEST -> TradePhase.INIT
            
            TradeState.DEPOSIT_TX_PUBLISHED_IN_BLOCKCHAIN -> TradePhase.DEPOSIT_PUBLISHED
            TradeState.DEPOSIT_TX_CONFIRMED_IN_BLOCKCHAIN -> TradePhase.DEPOSIT_CONFIRMED
            
            TradeState.BUYER_CONFIRMED_IN_UI_PAYMENT_SENT,
            TradeState.BUYER_SENT_PAYMENT_SENT_MSG -> TradePhase.PAYMENT_SENT
            
            TradeState.SELLER_SAW_ARRIVED_PAYMENT_SENT_MSG,
            TradeState.SELLER_CONFIRMED_IN_UI_PAYMENT_RECEIPT,
            TradeState.SELLER_SENT_PAYMENT_RECEIVED_MSG -> TradePhase.PAYMENT_RECEIVED
            
            TradeState.PAYOUT_TX_PUBLISHED_IN_BLOCKCHAIN,
            TradeState.TRADE_COMPLETED -> TradePhase.COMPLETED
        }
    }
}