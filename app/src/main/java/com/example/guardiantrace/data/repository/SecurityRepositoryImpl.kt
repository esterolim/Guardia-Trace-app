package com.example.guardiantrace.data.repository

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.guardiantrace.data.encryption.HashingManager
import com.example.guardiantrace.domain.repository.SecurityRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SecurityRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val hashingManager: HashingManager
) : SecurityRepository {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
            name = "security_prefs"
        )

        private val PIN_HASH_KEY = stringPreferencesKey("pin_hash")
        private val BIOMETRIC_ENABLED_KEY = booleanPreferencesKey("biometric_enabled")
    }

    private val dataStore = context.dataStore

    /**
     * Validates PIN against stored hash
     */
    override suspend fun validatePin(pin: String): Result<Boolean> {
        return try {
            val storedHash = dataStore.data.first()[PIN_HASH_KEY]

            if (storedHash == null) {
                Result.success(false)
            } else {
                val inputHash = hashingManager.sha256(pin)
                Result.success(inputHash == storedHash)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Sets new PIN (stores hash only)
     */
    override suspend fun setPin(pin: String): Result<Unit> {
        return try {
            val hash = hashingManager.sha256(pin)
            dataStore.edit { preferences ->
                preferences[PIN_HASH_KEY] = hash
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Checks if PIN is configured
     */
    override suspend fun isPinConfigured(): Result<Boolean> {
        return try {
            val hash = dataStore.data.first()[PIN_HASH_KEY]
            Result.success(hash != null)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Checks if biometric is enabled
     */
    override suspend fun isBiometricEnabled(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[BIOMETRIC_ENABLED_KEY] ?: false
        }
    }

    /**
     * Sets biometric enabled status
     */
    override suspend fun setBiometricEnabled(enabled: Boolean): Result<Unit> {
        return try {
            dataStore.edit { preferences ->
                preferences[BIOMETRIC_ENABLED_KEY] = enabled
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Checks if biometric authentication is available on device
     */
    override suspend fun isBiometricAvailable(): Result<Boolean> {
        return try {
            val biometricManager = BiometricManager.from(context)
            val canAuthenticate = biometricManager.canAuthenticate(
                BiometricManager.Authenticators.BIOMETRIC_STRONG or
                        BiometricManager.Authenticators.DEVICE_CREDENTIAL
            )

            val isAvailable = canAuthenticate == BiometricManager.BIOMETRIC_SUCCESS
            Result.success(isAvailable)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
