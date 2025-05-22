package com.haveno.android.ui.market

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haveno.android.data.model.Offer
import com.haveno.android.data.repository.MarketRepository
import kotlinx.coroutines.launch
import java.math.BigDecimal

class MarketViewModel : ViewModel() {
    
    private val marketRepository = MarketRepository()
    
    private val _orderBook = MutableLiveData<List<Offer>>()
    val orderBook: LiveData<List<Offer>> = _orderBook
    
    private val _selectedCurrency = MutableLiveData<String>()
    val selectedCurrency: LiveData<String> = _selectedCurrency
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage
    
    private val _selectedOffer = MutableLiveData<Offer?>()
    val selectedOffer: LiveData<Offer?> = _selectedOffer
    
    fun loadOrderBook(currency: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _errorMessage.value = null
                _selectedCurrency.value = currency
                
                // Get offers from repository
                val offers = marketRepository.getOffers(currency)
                _orderBook.value = offers
                
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load order book: ${e.message}"
                _orderBook.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun refreshOrderBook() {
        val currentCurrency = _selectedCurrency.value ?: "USD"
        loadOrderBook(currentCurrency)
    }
    
    fun selectOffer(offer: Offer) {
        _selectedOffer.value = offer
    }
    
    fun clearSelectedOffer() {
        _selectedOffer.value = null
    }
}