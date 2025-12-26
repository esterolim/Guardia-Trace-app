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
import com.example.guardiantrace.data.security.IntegrityChecker
import com.example.guardiantrace.data.security.MemoryZeroingUtil
import com.example.guardiantrace.data.security.SecureLogger
import com.example.guardiantrace.data.security.SecureSharedPreferencesWrapper
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
    private val hashingManager: HashingManager,
    private val integrityChecker: IntegrityChecker,
    private val securePrefs: SecureSharedPreferencesWrapper,
    private val memoryZeroingUtil: MemoryZeroingUtil
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
     * Validates PIN against stored hash using constant-time comparison
     */
    override suspend fun validatePin(pin: String): Result<Boolean> {
        return try {
            // First, check app integrity
            if (!integrityChecker.performIntegrityCheck()) {
                SecureLogger.securityEvent("PIN Validation Blocked", "Integrity check failed")
                return Result.success(false)
            }

            val storedHash = securePrefs.getPinHash()

            if (storedHash == null) {
                Result.success(false)
            } else {
                val inputHash = hashingManager.sha256(pin)
                val isValid = hashingManager.verifyHash(inputHash, storedHash)

                if (!isValid) {
                    SecureLogger.securityEvent("Failed PIN Attempt", "Invalid PIN provided")
                }

                // Zero out sensitive data from memory
                val pinChars = pin.toCharArray()
                memoryZeroingUtil.zeroCharArray(pinChars)

                Result.success(isValid)
            }
        } catch (e: Exception) {
            SecureLogger.e("Error validating PIN", e)
            Result.failure(e)
        }
    }

    /**
     * Sets new PIN (stores hash only, never stores plain PIN)
     */
    override suspend fun setPin(pin: String): Result<Unit> {
        return try {
            if (!integrityChecker.performIntegrityCheck()) {
                return Result.failure(Exception("Integrity check failed"))
            }

            val hash = hashingManager.sha256(pin)
            securePrefs.setPinHash(hash)

            // Zero out sensitive data
            val pinChars = pin.toCharArray()
            memoryZeroingUtil.zeroCharArray(pinChars)

            SecureLogger.securityEvent("PIN Set", "New PIN configured")
            Result.success(Unit)
        } catch (e: Exception) {
            SecureLogger.e("Error setting PIN", e)
            Result.failure(e)
        }
    }

    /**
     * Checks if PIN is configured
     */
    override suspend fun isPinConfigured(): Result<Boolean> {
        return try {
            val hash = securePrefs.getPinHash()
            Result.success(hash != null)
        } catch (e: Exception) {
            SecureLogger.e("Error checking PIN configuration", e)
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
            securePrefs.setBiometricEnabled(enabled)
            dataStore.edit { preferences ->
                preferences[BIOMETRIC_ENABLED_KEY] = enabled
            }
            SecureLogger.securityEvent("Biometric Setting Changed", "Biometric enabled=$enabled")
            Result.success(Unit)
        } catch (e: Exception) {
            SecureLogger.e("Error setting biometric", e)
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
            SecureLogger.e("Error checking biometric availability", e)
            Result.failure(e)
        }
    }

    /**
     * Gets integrity report for security monitoring
     */
    fun getIntegrityReport(): IntegrityChecker.IntegrityReport {
        return integrityChecker.getIntegrityReport()
    }
}
