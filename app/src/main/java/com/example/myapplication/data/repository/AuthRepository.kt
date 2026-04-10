package com.example.myapplication.data.repository

import com.example.myapplication.data.network.AuthResponse
import com.example.myapplication.data.network.LoginRequest
import com.example.myapplication.data.network.NetworkModule
import com.example.myapplication.data.network.RegisterRequest
import com.example.myapplication.data.network.RegisterResponse

class AuthRepository {
    private val apiService = NetworkModule.apiService

    suspend fun login(email: String, password: String): Result<AuthResponse> {
        return try {
            val response = apiService.login(LoginRequest(email, password))
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun loginOAuth(provider: String, providerId: String, email: String, name: String): Result<AuthResponse> {
        return try {
            val response = apiService.loginOAuth(com.example.myapplication.data.network.AuthRequest(provider, providerId, email, name))
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(email: String, password: String, name: String): Result<RegisterResponse> {
        return try {
            val response = apiService.register(RegisterRequest(email, password, name))
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout(): Result<Unit> {
        return try {
            apiService.logout()
            Result.success(Unit)
        } catch (e: Exception) {
            // Even if backend call fails, we still want to log out locally
            Result.success(Unit)
        }
    }
}
