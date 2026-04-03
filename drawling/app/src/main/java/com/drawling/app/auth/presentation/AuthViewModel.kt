package com.drawling.app.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drawling.app.auth.domain.repository.AuthRepository
import com.drawling.app.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state

    private val _isLoggedIn = MutableStateFlow<Boolean?>(null)
    val isLoggedIn: StateFlow<Boolean?> = _isLoggedIn

    init {
        checkLoginStatus()
    }

    private fun checkLoginStatus() = viewModelScope.launch {
        _isLoggedIn.value = authRepository.isLoggedIn()
    }

    fun sendOtp(phoneNumber: String) = viewModelScope.launch {
        _state.value = AuthState(isLoading = true)
        when (val result = authRepository.sendOtp(phoneNumber)) {
            is Resource.Success -> _state.value = AuthState(isSuccess = true)
            is Resource.Error -> _state.value = AuthState(error = result.message)
            else -> Unit
        }
    }

    fun verifyOtp(phoneNumber: String, otp: String, onSuccess: () -> Unit) = viewModelScope.launch {
        _state.value = AuthState(isLoading = true)
        when (val result = authRepository.verifyOtp(phoneNumber, otp)) {
            is Resource.Success -> { _state.value = AuthState(isSuccess = true); onSuccess() }
            is Resource.Error -> _state.value = AuthState(error = result.message)
            else -> Unit
        }
    }

    fun clearError() { _state.value = _state.value.copy(error = null) }
}
