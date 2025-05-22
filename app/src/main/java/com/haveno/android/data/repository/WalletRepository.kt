package com.haveno.android.data.repository

import com.haveno.android.data.model.*
import kotlinx.coroutines.delay
import java.math.BigDecimal
import java.util.*
import kotlin.random.Random

/**
 * Repository for wallet operations (balance, transactions, addresses)
 * Currently provides sample data, will be connected to real Haveno daemon
 */
class WalletRepository {
    
    /**
     * Get current wallet balance
     * TODO: Replace with real Haveno daemon gRPC calls
     */
    suspend fun getBalance(): WalletBalance {
        delay(800) // Simulate network delay
        
        // Generate realistic sample balance
        val available = BigDecimal("12.45678901")
        val pending = BigDecimal("1.23456789")
        val reservedOffer = BigDecimal("0.50000000")
        val reservedTrade = BigDecimal("2.10000000")
        val unconfirmed = BigDecimal("0.12345678")
        
        val total = available.add(pending).add(reservedOffer).add(reservedTrade).add(unconfirmed)
        
        return WalletBalance(
            availableBalance = available,
            pendingBalance = pending,
            reservedOfferBalance = reservedOffer,
            reservedTradeBalance = reservedTrade,
            totalBalance = total,
            unconfirmedBalance = unconfirmed
        )
    }
    
    /**
     * Get transaction history
     * TODO: Connect to Haveno daemon
     */
    suspend fun getTransactions(): List<Transaction> {
        delay(1200) // Simulate network delay
        
        return generateSampleTransactions()
    }
    
    /**
     * Get current receive address
     * TODO: Connect to Haveno daemon
     */
    suspend fun getReceiveAddress(): String {
        delay(500)
        return "89abcdefgh1234567890ijklmnopqrstuvwxyz9876543210ABCDEFGHIJKLMNOPQRSTUV"
    }
    
    /**
     * Generate a new receive address
     * TODO: Connect to Haveno daemon
     */
    suspend fun generateNewAddress(): String {
        delay(1000)
        return "89newaddress1234567890ijklmnopqrstuvwxyz9876543210ABCDEFGHIJKLMNOPQRSTUV"
    }
    
    /**
     * Send a transaction
     * TODO: Connect to Haveno daemon
     */
    suspend fun sendTransaction(toAddress: String, amount: String): Result<String> {
        return try {
            delay(3000) // Simulate transaction creation time
            
            // Validate inputs
            if (toAddress.isBlank()) {
                throw Exception("Address cannot be empty")
            }
            
            val amountBd = amount.toBigDecimalOrNull()
                ?: throw Exception("Invalid amount format")
            
            if (amountBd <= BigDecimal.ZERO) {
                throw Exception("Amount must be greater than zero")
            }
            
            // Simulate successful transaction
            val txId = "tx_${UUID.randomUUID().toString().replace("-", "")}"
            Result.success(txId)
            
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private fun generateSampleTransactions(): List<Transaction> {
        val transactions = mutableListOf<Transaction>()
        val now = System.currentTimeMillis()
        
        // Generate various types of transactions
        val transactionTypes = listOf(
            TransactionType.DEPOSIT,
            TransactionType.WITHDRAWAL,
            TransactionType.TRADE_DEPOSIT,
            TransactionType.TRADE_PAYOUT,
            TransactionType.OFFER_FEE,
            TransactionType.TRADE_FEE
        )
        
        repeat(15) { i ->
            val timestamp = Date(now - (i * 86400000L) - Random.nextLong(86400000)) // Last 15 days
            val type = transactionTypes.random()
            val direction = when (type) {
                TransactionType.DEPOSIT, TransactionType.TRADE_PAYOUT -> TransactionDirection.INCOMING
                else -> TransactionDirection.OUTGOING
            }
            
            val baseAmount = when (type) {
                TransactionType.OFFER_FEE, TransactionType.TRADE_FEE -> Random.nextDouble(0.001, 0.01)
                TransactionType.TRADE_DEPOSIT -> Random.nextDouble(0.1, 2.0)
                else -> Random.nextDouble(0.05, 5.0)
            }
            
            val amount = BigDecimal(baseAmount).setScale(8, BigDecimal.ROUND_HALF_UP)
            val fee = if (direction == TransactionDirection.OUTGOING) {
                BigDecimal(Random.nextDouble(0.0001, 0.001)).setScale(8, BigDecimal.ROUND_HALF_UP)
            } else null
            
            val confirmations = if (i < 3) Random.nextInt(0, 10) else Random.nextInt(10, 100)
            val isConfirmed = confirmations >= 10
            
            transactions.add(
                Transaction(
                    txId = "tx_${UUID.randomUUID().toString().replace("-", "")}",
                    hash = "hash_${Random.nextInt(100000, 999999)}",
                    type = type,
                    direction = direction,
                    amount = amount,
                    fee = fee,
                    confirmations = confirmations,
                    blockHeight = if (isConfirmed) Random.nextLong(2800000, 2900000) else null,
                    timestamp = timestamp,
                    memo = if (Random.nextBoolean()) "Trade #${Random.nextInt(1000, 9999)}" else null,
                    address = if (direction == TransactionDirection.OUTGOING) {
                        "dest_${Random.nextInt(100000, 999999)}"
                    } else null,
                    isConfirmed = isConfirmed,
                    tradingPeer = if (type in listOf(TransactionType.TRADE_DEPOSIT, TransactionType.TRADE_PAYOUT)) {
                        "peer_${Random.nextInt(1000, 9999)}"
                    } else null,
                    offerId = if (type == TransactionType.OFFER_FEE) "offer_${Random.nextInt(1000, 9999)}" else null,
                    tradeId = if (type in listOf(TransactionType.TRADE_DEPOSIT, TransactionType.TRADE_PAYOUT, TransactionType.TRADE_FEE)) {
                        "trade_${Random.nextInt(1000, 9999)}"
                    } else null
                )
            )
        }
        
        // Sort by timestamp (newest first)
        return transactions.sortedByDescending { it.timestamp }
    }
}