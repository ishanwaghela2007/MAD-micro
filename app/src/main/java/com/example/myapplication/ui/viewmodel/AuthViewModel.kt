package com.example.myapplication.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.network.AuthResponse
import com.example.myapplication.data.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val repository = AuthRepository()

    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)
    var isLoggedIn by mutableStateOf(false)
    var isRegistered by mutableStateOf(false)
    var authResponse by mutableStateOf<AuthResponse?>(null)

    fun login(email: String, password: String) {
        viewModelScope.launch {
            isLoading = true
            error = null
            repository.login(email, password).onSuccess {
                com.example.myapplication.data.network.TokenManager.token = it.accessToken
                authResponse = it
                isLoggedIn = true
                isLoading = false
            }.onFailure {
                error = it.message ?: "Login failed"
                isLoading = false
            }
        }
    }

    fun loginWithGoogle(email: String, name: String, googleId: String) {
        viewModelScope.launch {
            isLoading = true
            error = null
            repository.loginOAuth("google", googleId, email, name).onSuccess {
                com.example.myapplication.data.network.TokenManager.token = it.accessToken
                authResponse = it
                isLoggedIn = true
                isLoading = false
            }.onFailure {
                error = it.message ?: "Google Login failed"
                isLoading = false
            }
        }
    }

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            isLoading = true
            error = null
            repository.register(email, password, name).onSuccess {
                com.example.myapplication.data.network.TokenManager.token = it.accessToken
                isRegistered = true
                isLoading = false
            }.onFailure {
                error = it.message ?: "Registration failed"
                isLoading = false
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout() // tells backend to blacklist the token in Redis
            com.example.myapplication.data.network.TokenManager.token = null
            authResponse = null
            isLoggedIn = false
            isRegistered = false
        }
    }
}
