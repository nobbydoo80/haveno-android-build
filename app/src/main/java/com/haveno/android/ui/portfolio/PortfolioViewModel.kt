package com.haveno.android.ui.portfolio

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haveno.android.data.model.Trade
import com.haveno.android.data.repository.TradeRepository
import kotlinx.coroutines.launch

class PortfolioViewModel : ViewModel() {
    
    private val tradeRepository = TradeRepository()
    
    private val _trades = MutableLiveData<List<Trade>>()
    val trades: LiveData<List<Trade>> = _trades
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage
    
    private val _selectedTrade = MutableLiveData<Trade?>()
    val selectedTrade: LiveData<Trade?> = _selectedTrade
    
    fun loadTrades() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _errorMessage.value = null
                
                val tradeList = tradeRepository.getActiveTrades()
                _trades.value = tradeList
                
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load trades: ${e.message}"
                _trades.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun refreshTrades() {
        loadTrades()
    }
    
    fun selectTrade(trade: Trade) {
        _selectedTrade.value = trade
    }
}