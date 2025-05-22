package com.haveno.core.api

import com.google.protobuf.Empty
import com.haveno.protobuf.*
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.Status
import io.grpc.StatusRuntimeException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Kotlin wrapper for the Haveno gRPC API client
 * Provides coroutine-based access to all Haveno daemon functionality
 */
@Singleton
class HavenoApiClient @Inject constructor() {
    
    private val logger = LoggerFactory.getLogger(HavenoApiClient::class.java)
    
    private var channel: ManagedChannel? = null
    private var accountService: AccountGrpc.AccountBlockingStub? = null
    private var walletsService: WalletsGrpc.WalletsBlockingStub? = null
    private var offersService: OffersGrpc.OffersBlockingStub? = null
    private var tradesService: TradesGrpc.TradesBlockingStub? = null
    private var disputesService: DisputesGrpc.DisputesBlockingStub? = null
    private var notificationsService: NotificationsGrpc.NotificationsBlockingStub? = null
    private var xmrConnectionService: XmrConnectionsGrpc.XmrConnectionsBlockingStub? = null
    private var getVersionService: GetVersionGrpc.GetVersionBlockingStub? = null
    
    @Volatile
    private var isConnected = false
    
    /**
     * Connect to the Haveno daemon
     */
    suspend fun connect(host: String, port: Int, password: String? = null) = withContext(Dispatchers.IO) {
        try {
            disconnect() // Ensure clean state
            
            logger.info("Connecting to Haveno daemon at $host:$port")
            
            val channelBuilder = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext() // TODO: Add TLS support
                .keepAliveTime(30, TimeUnit.SECONDS)
                .keepAliveTimeout(5, TimeUnit.SECONDS)
                .keepAliveWithoutCalls(true)
                .maxInboundMessageSize(16 * 1024 * 1024) // 16MB
            
            channel = channelBuilder.build()
            
            // Create service stubs
            val baseStub = if (password != null) {
                // Add password authentication interceptor
                val interceptor = PasswordAuthInterceptor(password)
                channel!!.newCall(null, null).apply { 
                    // TODO: Implement password authentication
                }
            } else {
                channel!!
            }
            
            accountService = AccountGrpc.newBlockingStub(channel)
            walletsService = WalletsGrpc.newBlockingStub(channel)
            offersService = OffersGrpc.newBlockingStub(channel)
            tradesService = TradesGrpc.newBlockingStub(channel)
            disputesService = DisputesGrpc.newBlockingStub(channel)
            notificationsService = NotificationsGrpc.newBlockingStub(channel)
            xmrConnectionService = XmrConnectionsGrpc.newBlockingStub(channel)
            getVersionService = GetVersionGrpc.newBlockingStub(channel)
            
            // Test connection
            val version = getVersionService?.getVersion(GetVersionRequest.newBuilder().build())
            logger.info("Connected to Haveno daemon version: ${version?.version}")
            
            isConnected = true
            
        } catch (e: Exception) {
            logger.error("Failed to connect to Haveno daemon", e)
            disconnect()
            throw HavenoApiException("Connection failed: ${e.message}", e)
        }
    }
    
    /**
     * Disconnect from the Haveno daemon
     */
    suspend fun disconnect() = withContext(Dispatchers.IO) {
        try {
            channel?.shutdown()?.awaitTermination(5, TimeUnit.SECONDS)
        } catch (e: Exception) {
            logger.warn("Error during graceful shutdown", e)
            channel?.shutdownNow()
        } finally {
            channel = null
            isConnected = false
            logger.info("Disconnected from Haveno daemon")
        }
    }
    
    /**
     * Check if connected to daemon
     */
    fun isConnected(): Boolean = isConnected
    
    // Account Management
    suspend fun isAccountRegistered(): Boolean = withContext(Dispatchers.IO) {
        try {
            val response = accountService?.isAccountRegistered(Empty.newBuilder().build())
            response?.isAccountRegistered ?: false
        } catch (e: StatusRuntimeException) {
            handleGrpcError(e)
            false
        }
    }
    
    suspend fun createAccount(password: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val request = CreateAccountRequest.newBuilder()
                .setPassword(password)
                .build()
            accountService?.createAccount(request)
            true
        } catch (e: StatusRuntimeException) {
            handleGrpcError(e)
            false
        }
    }
    
    suspend fun openAccount(password: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val request = OpenAccountRequest.newBuilder()
                .setPassword(password)
                .build()
            accountService?.openAccount(request)
            true
        } catch (e: StatusRuntimeException) {
            handleGrpcError(e)
            false
        }
    }
    
    // Wallet Operations
    suspend fun getBalance(): GetBalancesReply? = withContext(Dispatchers.IO) {
        try {
            walletsService?.getBalances(GetBalancesRequest.newBuilder().build())
        } catch (e: StatusRuntimeException) {
            handleGrpcError(e)
            null
        }
    }
    
    suspend fun getXmrPrimaryAddress(): String? = withContext(Dispatchers.IO) {
        try {
            val response = walletsService?.getXmrPrimaryAddress(Empty.newBuilder().build())
            response?.primaryAddress
        } catch (e: StatusRuntimeException) {
            handleGrpcError(e)
            null
        }
    }
    
    // Offers
    suspend fun getOffers(direction: String, currencyCode: String): GetOffersReply? = withContext(Dispatchers.IO) {
        try {
            val request = GetOffersRequest.newBuilder()
                .setDirection(direction)
                .setCurrencyCode(currencyCode)
                .build()
            offersService?.getOffers(request)
        } catch (e: StatusRuntimeException) {
            handleGrpcError(e)
            null
        }
    }
    
    suspend fun createOffer(
        currencyCode: String,
        direction: String,
        price: String,
        useMarketBasedPrice: Boolean,
        marketPriceMargin: Double,
        amount: Long,
        minAmount: Long,
        buyerSecurityDeposit: Double,
        paymentAccountId: String
    ): CreateOfferReply? = withContext(Dispatchers.IO) {
        try {
            val request = CreateOfferRequest.newBuilder()
                .setCurrencyCode(currencyCode)
                .setDirection(direction)
                .setPrice(price)
                .setUseMarketBasedPrice(useMarketBasedPrice)
                .setMarketPriceMargin(marketPriceMargin)
                .setAmount(amount)
                .setMinAmount(minAmount)
                .setBuyerSecurityDeposit(buyerSecurityDeposit)
                .setPaymentAccountId(paymentAccountId)
                .build()
            offersService?.createOffer(request)
        } catch (e: StatusRuntimeException) {
            handleGrpcError(e)
            null
        }
    }
    
    // Trades
    suspend fun getTrades(): GetTradesReply? = withContext(Dispatchers.IO) {
        try {
            tradesService?.getTrades(GetTradesRequest.newBuilder().build())
        } catch (e: StatusRuntimeException) {
            handleGrpcError(e)
            null
        }
    }
    
    suspend fun takeOffer(
        offerId: String,
        paymentAccountId: String,
        amount: Long
    ): TakeOfferReply? = withContext(Dispatchers.IO) {
        try {
            val request = TakeOfferRequest.newBuilder()
                .setOfferId(offerId)
                .setPaymentAccountId(paymentAccountId)
                .setAmount(amount)
                .build()
            tradesService?.takeOffer(request)
        } catch (e: StatusRuntimeException) {
            handleGrpcError(e)
            null
        }
    }
    
    // Health Check
    suspend fun checkHealth(): Boolean = withContext(Dispatchers.IO) {
        try {
            getVersionService?.getVersion(GetVersionRequest.newBuilder().build())
            true
        } catch (e: Exception) {
            logger.warn("Health check failed", e)
            false
        }
    }
    
    private fun handleGrpcError(e: StatusRuntimeException) {
        val status = e.status
        val message = when (status.code) {
            Status.Code.UNAVAILABLE -> "Daemon unavailable"
            Status.Code.UNAUTHENTICATED -> "Authentication failed"
            Status.Code.PERMISSION_DENIED -> "Permission denied"
            Status.Code.NOT_FOUND -> "Resource not found"
            Status.Code.ALREADY_EXISTS -> "Resource already exists"
            Status.Code.INVALID_ARGUMENT -> "Invalid argument"
            else -> "API error: ${status.description}"
        }
        
        logger.error("gRPC error: $message", e)
        throw HavenoApiException(message, e)
    }
}

/**
 * Custom exception for Haveno API errors
 */
class HavenoApiException(message: String, cause: Throwable? = null) : Exception(message, cause)

/**
 * Password authentication interceptor for gRPC calls
 */
private class PasswordAuthInterceptor(private val password: String) {
    // TODO: Implement password authentication for gRPC calls
}