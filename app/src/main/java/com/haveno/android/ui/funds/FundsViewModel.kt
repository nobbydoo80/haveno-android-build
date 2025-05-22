package com.haveno.android.ui.funds

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haveno.android.data.model.Transaction
import com.haveno.android.data.model.TransactionDirection
import com.haveno.android.data.model.WalletBalance
import com.haveno.android.data.repository.WalletRepository
import kotlinx.coroutines.launch

class FundsViewModel : ViewModel() {
    
    private val walletRepository = WalletRepository()
    
    private val _walletBalance = MutableLiveData<WalletBalance?>()
    val walletBalance: LiveData<WalletBalance?> = _walletBalance
    
    private val _transactions = MutableLiveData<List<Transaction>>()
    val transactions: LiveData<List<Transaction>> = _transactions
    
    private val _allTransactions = MutableLiveData<List<Transaction>>()
    
    private val _receiveAddress = MutableLiveData<String?>()
    val receiveAddress: LiveData<String?> = _receiveAddress
    
    private val _isLoadingBalance = MutableLiveData<Boolean>()
    val isLoadingBalance: LiveData<Boolean> = _isLoadingBalance
    
    private val _isLoadingTransactions = MutableLiveData<Boolean>()
    val isLoadingTransactions: LiveData<Boolean> = _isLoadingTransactions
    
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage
    
    private val _selectedTransaction = MutableLiveData<Transaction?>()
    val selectedTransaction: LiveData<Transaction?> = _selectedTransaction
    
    private var currentFilter = "ALL"
    
    fun loadWalletData() {
        loadBalance()
        loadTransactions()
        loadReceiveAddress()
    }
    
    fun refreshWalletData() {
        _errorMessage.value = null
        loadWalletData()
    }
    
    private fun loadBalance() {
        viewModelScope.launch {
            try {
                _isLoadingBalance.value = true
                _errorMessage.value = null
                
                val balance = walletRepository.getBalance()
                _walletBalance.value = balance
                
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load balance: ${e.message}"
            } finally {
                _isLoadingBalance.value = false
            }
        }
    }
    
    private fun loadTransactions() {
        viewModelScope.launch {
            try {
                _isLoadingTransactions.value = true
                
                val transactionList = walletRepository.getTransactions()
                _allTransactions.value = transactionList
                
                // Apply current filter
                applyFilter(currentFilter)
                
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load transactions: ${e.message}"
                _transactions.value = emptyList()
            } finally {
                _isLoadingTransactions.value = false
            }
        }
    }
    
    private fun loadReceiveAddress() {
        viewModelScope.launch {
            try {
                val address = walletRepository.getReceiveAddress()
                _receiveAddress.value = address
            } catch (e: Exception) {
                // Don't show error for address loading failure
            }
        }
    }
    
    fun filterTransactions(filter: String) {
        currentFilter = filter
        applyFilter(filter)
    }
    
    private fun applyFilter(filter: String) {
        val allTxs = _allTransactions.value ?: emptyList()
        
        val filtered = when (filter) {
            "INCOMING" -> allTxs.filter { it.direction == TransactionDirection.INCOMING }
            "OUTGOING" -> allTxs.filter { it.direction == TransactionDirection.OUTGOING }
            "PENDING" -> allTxs.filter { !it.isConfirmed }
            else -> allTxs // "ALL"
        }
        
        _transactions.value = filtered
    }
    
    fun selectTransaction(transaction: Transaction) {
        _selectedTransaction.value = transaction
    }
    
    fun generateNewReceiveAddress() {
        viewModelScope.launch {
            try {
                val newAddress = walletRepository.generateNewAddress()
                _receiveAddress.value = newAddress
            } catch (e: Exception) {
                _errorMessage.value = "Failed to generate address: ${e.message}"
            }
        }
    }
    
    fun copyAddressToClipboard() {
        val address = _receiveAddress.value
        if (address != null) {
            // TODO: Implement clipboard copy
            // ClipboardManager implementation would go here
        }
    }
    
    fun showBalanceDetails() {
        // TODO: Show detailed balance breakdown dialog
    }
    
    fun sendTransaction(toAddress: String, amount: String) {
        viewModelScope.launch {
            try {
                _isLoadingTransactions.value = true
                
                val result = walletRepository.sendTransaction(toAddress, amount)
                if (result.isSuccess) {
                    // Refresh data after successful send
                    loadWalletData()
                } else {
                    _errorMessage.value = "Failed to send transaction: ${result.exceptionOrNull()?.message}"
                }
                
            } catch (e: Exception) {
                _errorMessage.value = "Send transaction error: ${e.message}"
            } finally {
                _isLoadingTransactions.value = false
            }
        }
    }
}