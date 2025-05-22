package com.haveno.android.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import com.haveno.android.service.HavenoDaemonService
import com.haveno.android.util.ConnectionState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    
    private val _connectionState = MutableLiveData<ConnectionState>()
    val connectionState: LiveData<ConnectionState> = _connectionState
    
    private val _daemonStatus = MutableLiveData<HavenoDaemonService.DaemonStatus>()
    val daemonStatus: LiveData<HavenoDaemonService.DaemonStatus> = _daemonStatus
    
    private val _authenticationState = MutableLiveData<Boolean>()
    val authenticationState: LiveData<Boolean> = _authenticationState
    
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage
    
    fun updateConnectionState(state: ConnectionState) {
        _connectionState.value = state
    }
    
    fun updateDaemonStatus(status: HavenoDaemonService.DaemonStatus) {
        _daemonStatus.value = status
    }
    
    fun isUserAuthenticated(): Boolean = true // Stub implementation
    
    fun isSetupComplete(): Boolean = true // Stub implementation
    
    fun clearError() {
        _errorMessage.value = null
    }
}