package com.example.guardiantrace.domain.repository

import kotlinx.coroutines.flow.Flow

interface SecurityRepository {

    suspend fun validatePin(pin: String): Result<Boolean>

    suspend fun setPin(pin: String): Result<Unit>

    suspend fun isPinConfigured(): Result<Boolean>

    suspend fun isBiometricEnabled(): Flow<Boolean>

    suspend fun setBiometricEnabled(enabled: Boolean): Result<Unit>

    suspend fun isBiometricAvailable(): Result<Boolean>
}