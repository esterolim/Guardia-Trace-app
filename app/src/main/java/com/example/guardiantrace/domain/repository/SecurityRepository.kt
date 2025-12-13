package com.example.guardiantrace.domain.repository

interface SecurityRepository {

    suspend fun validatePin(pin: String): Result<Boolean>

    suspend fun setPin(pin: String): Result<Unit>

    suspend fun isPinConfigured(): Result<Boolean>

    suspend fun isBiometricEnabled(): Result<Boolean>

    suspend fun setBiometricEnabled(enabled: Boolean): Result<Unit>

    suspend fun isBiometricAvailable(): Result<Boolean>
}