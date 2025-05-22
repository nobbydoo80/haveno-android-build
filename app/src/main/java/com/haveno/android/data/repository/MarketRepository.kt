package com.haveno.android.data.repository

import com.haveno.android.data.model.Offer
import com.haveno.android.data.model.OfferDirection
import kotlinx.coroutines.delay
import java.math.BigDecimal
import java.util.*
import kotlin.random.Random

/**
 * Repository for market data (order book, offers)
 * Currently provides sample data, will be connected to real Haveno daemon
 */
class MarketRepository {
    
    /**
     * Get offers for a specific currency
     * TODO: Replace with real Haveno daemon gRPC calls
     */
    suspend fun getOffers(currencyCode: String): List<Offer> {
        // Simulate network delay
        delay(1000)
        
        // Generate sample offers for demonstration
        return generateSampleOffers(currencyCode)
    }
    
    /**
     * Create a new offer
     * TODO: Connect to Haveno daemon
     */
    suspend fun createOffer(
        direction: OfferDirection,
        currencyCode: String,
        price: BigDecimal,
        amount: BigDecimal,
        minAmount: BigDecimal,
        paymentMethod: String
    ): Result<Offer> {
        return try {
            delay(2000) // Simulate creation time
            
            val offer = Offer(
                id = UUID.randomUUID().toString(),
                direction = direction,
                currencyCode = currencyCode,
                price = price,
                amount = amount,
                minAmount = minAmount,
                maxAmount = amount,
                paymentMethod = paymentMethod,
                makerFeePct = BigDecimal("0.001"), // 0.1%
                buyerSecurityDeposit = BigDecimal("0.15"), // 15%
                sellerSecurityDeposit = BigDecimal("0.15"), // 15%
                creationDate = Date(),
                isMyOffer = true,
                ownerNodeAddress = "sample-node-address"
            )
            
            Result.success(offer)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Take an existing offer
     * TODO: Connect to Haveno daemon
     */
    suspend fun takeOffer(
        offerId: String,
        amount: BigDecimal
    ): Result<String> {
        return try {
            delay(3000) // Simulate trade initiation time
            Result.success("trade-${UUID.randomUUID()}")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private fun generateSampleOffers(currencyCode: String): List<Offer> {
        val basePrice = when (currencyCode) {
            "USD" -> BigDecimal("150.00")
            "EUR" -> BigDecimal("140.00")
            "GBP" -> BigDecimal("120.00")
            "JPY" -> BigDecimal("20000.00")
            "CAD" -> BigDecimal("200.00")
            else -> BigDecimal("150.00")
        }
        
        val offers = mutableListOf<Offer>()
        
        // Generate buy offers (users wanting to buy XMR)
        repeat(5) { i ->
            val priceVariation = Random.nextDouble(-0.05, 0.02) // -5% to +2%
            val price = basePrice.multiply(BigDecimal(1.0 + priceVariation))
            
            offers.add(
                Offer(
                    id = "buy-offer-$i",
                    direction = OfferDirection.BUY,
                    currencyCode = currencyCode,
                    price = price.setScale(2, BigDecimal.ROUND_HALF_UP),
                    amount = BigDecimal(Random.nextDouble(0.1, 2.0)).setScale(4, BigDecimal.ROUND_HALF_UP),
                    minAmount = BigDecimal(Random.nextDouble(0.01, 0.1)).setScale(4, BigDecimal.ROUND_HALF_UP),
                    maxAmount = BigDecimal(Random.nextDouble(2.0, 5.0)).setScale(4, BigDecimal.ROUND_HALF_UP),
                    paymentMethod = listOf("Bank Transfer", "PayPal", "Cash", "Zelle").random(),
                    makerFeePct = BigDecimal("0.001"),
                    buyerSecurityDeposit = BigDecimal("0.15"),
                    sellerSecurityDeposit = BigDecimal("0.15"),
                    creationDate = Date(System.currentTimeMillis() - Random.nextLong(86400000)), // Last 24h
                    isMyOffer = i == 0, // First one is ours
                    ownerNodeAddress = "node-address-$i"
                )
            )
        }
        
        // Generate sell offers (users wanting to sell XMR)
        repeat(5) { i ->
            val priceVariation = Random.nextDouble(-0.02, 0.05) // -2% to +5%
            val price = basePrice.multiply(BigDecimal(1.0 + priceVariation))
            
            offers.add(
                Offer(
                    id = "sell-offer-$i",
                    direction = OfferDirection.SELL,
                    currencyCode = currencyCode,
                    price = price.setScale(2, BigDecimal.ROUND_HALF_UP),
                    amount = BigDecimal(Random.nextDouble(0.1, 3.0)).setScale(4, BigDecimal.ROUND_HALF_UP),
                    minAmount = BigDecimal(Random.nextDouble(0.01, 0.1)).setScale(4, BigDecimal.ROUND_HALF_UP),
                    maxAmount = BigDecimal(Random.nextDouble(3.0, 10.0)).setScale(4, BigDecimal.ROUND_HALF_UP),
                    paymentMethod = listOf("Bank Transfer", "PayPal", "Cash", "Zelle", "Wise").random(),
                    makerFeePct = BigDecimal("0.001"),
                    buyerSecurityDeposit = BigDecimal("0.15"),
                    sellerSecurityDeposit = BigDecimal("0.15"),
                    creationDate = Date(System.currentTimeMillis() - Random.nextLong(86400000)),
                    isMyOffer = false,
                    ownerNodeAddress = "node-address-sell-$i"
                )
            )
        }
        
        // Sort by price - buy orders descending (highest first), sell orders ascending (lowest first)
        return offers.sortedWith { a, b ->
            when {
                a.direction == OfferDirection.BUY && b.direction == OfferDirection.BUY -> 
                    b.price.compareTo(a.price) // Descending for buy orders
                a.direction == OfferDirection.SELL && b.direction == OfferDirection.SELL -> 
                    a.price.compareTo(b.price) // Ascending for sell orders
                a.direction == OfferDirection.BUY -> -1 // Buy orders first
                else -> 1 // Sell orders second
            }
        }
    }
}