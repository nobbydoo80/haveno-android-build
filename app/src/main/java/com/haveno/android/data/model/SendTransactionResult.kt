package com.haveno.android.data.model

import java.math.BigDecimal

sealed class SendTransactionResult {
    data class Success(
        val transactionHash: String,
        val amount: BigDecimal,
        val fee: BigDecimal
    ) : SendTransactionResult()
    
    data class Error(
        val message: String
    ) : SendTransactionResult()
}