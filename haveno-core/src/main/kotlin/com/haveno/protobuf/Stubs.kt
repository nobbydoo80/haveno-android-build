package com.haveno.protobuf

// Placeholder protobuf stubs - these would normally be generated from .proto files

class Empty {
    companion object {
        fun newBuilder(): Builder = Builder()
    }
    
    class Builder {
        fun build(): Empty = Empty()
    }
}

class GetVersionRequest {
    companion object {
        fun newBuilder(): Builder = Builder()
    }
    
    class Builder {
        fun build(): GetVersionRequest = GetVersionRequest()
    }
}

class GetVersionReply {
    val version: String = "1.0.0-stub"
}

class CreateAccountRequest {
    companion object {
        fun newBuilder(): Builder = Builder()
    }
    
    class Builder {
        fun setPassword(password: String): Builder = this
        fun build(): CreateAccountRequest = CreateAccountRequest()
    }
}

class OpenAccountRequest {
    companion object {
        fun newBuilder(): Builder = Builder()
    }
    
    class Builder {
        fun setPassword(password: String): Builder = this
        fun build(): OpenAccountRequest = OpenAccountRequest()
    }
}

class GetBalancesRequest {
    companion object {
        fun newBuilder(): Builder = Builder()
    }
    
    class Builder {
        fun build(): GetBalancesRequest = GetBalancesRequest()
    }
}

class GetBalancesReply {
    val availableBalance: Long = 0
}

class GetOffersRequest {
    companion object {
        fun newBuilder(): Builder = Builder()
    }
    
    class Builder {
        fun setDirection(direction: String): Builder = this
        fun setCurrencyCode(currencyCode: String): Builder = this
        fun build(): GetOffersRequest = GetOffersRequest()
    }
}

class GetOffersReply {
    val offersList: List<Any> = emptyList()
}

class CreateOfferRequest {
    companion object {
        fun newBuilder(): Builder = Builder()
    }
    
    class Builder {
        fun setCurrencyCode(currencyCode: String): Builder = this
        fun setDirection(direction: String): Builder = this
        fun setPrice(price: String): Builder = this
        fun setUseMarketBasedPrice(useMarketBasedPrice: Boolean): Builder = this
        fun setMarketPriceMargin(marketPriceMargin: Double): Builder = this
        fun setAmount(amount: Long): Builder = this
        fun setMinAmount(minAmount: Long): Builder = this
        fun setBuyerSecurityDeposit(buyerSecurityDeposit: Double): Builder = this
        fun setPaymentAccountId(paymentAccountId: String): Builder = this
        fun build(): CreateOfferRequest = CreateOfferRequest()
    }
}

class CreateOfferReply

class GetTradesRequest {
    companion object {
        fun newBuilder(): Builder = Builder()
    }
    
    class Builder {
        fun build(): GetTradesRequest = GetTradesRequest()
    }
}

class GetTradesReply {
    val tradesList: List<Any> = emptyList()
}

class TakeOfferRequest {
    companion object {
        fun newBuilder(): Builder = Builder()
    }
    
    class Builder {
        fun setOfferId(offerId: String): Builder = this
        fun setPaymentAccountId(paymentAccountId: String): Builder = this
        fun setAmount(amount: Long): Builder = this
        fun build(): TakeOfferRequest = TakeOfferRequest()
    }
}

class TakeOfferReply

// Stub gRPC service classes
object AccountGrpc {
    fun newBlockingStub(channel: Any): AccountBlockingStub = AccountBlockingStub()
    
    class AccountBlockingStub {
        fun isAccountRegistered(request: Empty): IsAccountRegisteredReply = IsAccountRegisteredReply()
        fun createAccount(request: CreateAccountRequest) {}
        fun openAccount(request: OpenAccountRequest) {}
    }
    
    class IsAccountRegisteredReply {
        val isAccountRegistered: Boolean = false
    }
}

object WalletsGrpc {
    fun newBlockingStub(channel: Any): WalletsBlockingStub = WalletsBlockingStub()
    
    class WalletsBlockingStub {
        fun getBalances(request: GetBalancesRequest): GetBalancesReply = GetBalancesReply()
        fun getXmrPrimaryAddress(request: Empty): GetXmrPrimaryAddressReply = GetXmrPrimaryAddressReply()
    }
    
    class GetXmrPrimaryAddressReply {
        val primaryAddress: String = "stub-address"
    }
}

object OffersGrpc {
    fun newBlockingStub(channel: Any): OffersBlockingStub = OffersBlockingStub()
    
    class OffersBlockingStub {
        fun getOffers(request: GetOffersRequest): GetOffersReply = GetOffersReply()
        fun createOffer(request: CreateOfferRequest): CreateOfferReply = CreateOfferReply()
    }
}

object TradesGrpc {
    fun newBlockingStub(channel: Any): TradesBlockingStub = TradesBlockingStub()
    
    class TradesBlockingStub {
        fun getTrades(request: GetTradesRequest): GetTradesReply = GetTradesReply()
        fun takeOffer(request: TakeOfferRequest): TakeOfferReply = TakeOfferReply()
    }
}

object DisputesGrpc {
    fun newBlockingStub(channel: Any): DisputesBlockingStub = DisputesBlockingStub()
    class DisputesBlockingStub
}

object NotificationsGrpc {
    fun newBlockingStub(channel: Any): NotificationsBlockingStub = NotificationsBlockingStub()
    class NotificationsBlockingStub
}

object XmrConnectionsGrpc {
    fun newBlockingStub(channel: Any): XmrConnectionsBlockingStub = XmrConnectionsBlockingStub()
    class XmrConnectionsBlockingStub
}

object GetVersionGrpc {
    fun newBlockingStub(channel: Any): GetVersionBlockingStub = GetVersionBlockingStub()
    
    class GetVersionBlockingStub {
        fun getVersion(request: GetVersionRequest): GetVersionReply = GetVersionReply()
    }
}