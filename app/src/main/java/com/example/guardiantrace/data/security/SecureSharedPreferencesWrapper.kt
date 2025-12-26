package com.example.guardiantrace.data.security

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Wrapper for EncryptedSharedPreferences providing secure storage of sensitive preferences.
 *
 * Automatically encrypts/decrypts all data at rest using AndroidKeyStore.
 * Follows OWASP Mobile Security Guidelines for sensitive data storage.
 */
@Singleton
class SecureSharedPreferencesWrapper @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val masterKey: MasterKey by lazy {
        MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
    }

    private val encryptedPrefs: SharedPreferences by lazy {
        EncryptedSharedPreferences.create(
            context,
            PREFS_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    companion object {
        private const val PREFS_NAME = "guardian_trace_secure_prefs"
        private const val PIN_KEY = "pin_hash"
        private const val BIOMETRIC_ENABLED_KEY = "biometric_enabled"
        private const val DEVICE_ID_KEY = "device_id"
    }

    /**
     * Stores encrypted PIN hash
     */
    fun setPinHash(pinHash: String) {
        encryptedPrefs.edit().putString(PIN_KEY, pinHash).apply()
    }

    /**
     * Retrieves encrypted PIN hash
     */
    fun getPinHash(): String? {
        return encryptedPrefs.getString(PIN_KEY, null)
    }

    /**
     * Stores biometric enabled status
     */
    fun setBiometricEnabled(enabled: Boolean) {
        encryptedPrefs.edit().putBoolean(BIOMETRIC_ENABLED_KEY, enabled).apply()
    }

    /**
     * Retrieves biometric enabled status
     */
    fun isBiometricEnabled(): Boolean {
        return encryptedPrefs.getBoolean(BIOMETRIC_ENABLED_KEY, false)
    }

    /**
     * Stores device ID for integrity checks
     */
    fun setDeviceId(deviceId: String) {
        encryptedPrefs.edit().putString(DEVICE_ID_KEY, deviceId).apply()
    }

    /**
     * Retrieves device ID
     */
    fun getDeviceId(): String? {
        return encryptedPrefs.getString(DEVICE_ID_KEY, null)
    }

    /**
     * Securely stores a string value
     */
    fun putString(key: String, value: String) {
        if (isValidKey(key)) {
            encryptedPrefs.edit().putString(key, value).apply()
        } else {
            throw IllegalArgumentException("Invalid preference key: $key")
        }
    }

    /**
     * Securely retrieves a string value
     */
    fun getString(key: String, defaultValue: String? = null): String? {
        return encryptedPrefs.getString(key, defaultValue)
    }

    /**
     * Securely stores an integer value
     */
    fun putInt(key: String, value: Int) {
        if (isValidKey(key)) {
            encryptedPrefs.edit().putInt(key, value).apply()
        } else {
            throw IllegalArgumentException("Invalid preference key: $key")
        }
    }

    /**
     * Securely retrieves an integer value
     */
    fun getInt(key: String, defaultValue: Int = 0): Int {
        return encryptedPrefs.getInt(key, defaultValue)
    }

    /**
     * Securely stores a long value
     */
    fun putLong(key: String, value: Long) {
        if (isValidKey(key)) {
            encryptedPrefs.edit().putLong(key, value).apply()
        } else {
            throw IllegalArgumentException("Invalid preference key: $key")
        }
    }

    /**
     * Securely retrieves a long value
     */
    fun getLong(key: String, defaultValue: Long = 0L): Long {
        return encryptedPrefs.getLong(key, defaultValue)
    }

    /**
     * Securely stores a boolean value
     */
    fun putBoolean(key: String, value: Boolean) {
        if (isValidKey(key)) {
            encryptedPrefs.edit().putBoolean(key, value).apply()
        } else {
            throw IllegalArgumentException("Invalid preference key: $key")
        }
    }

    /**
     * Securely retrieves a boolean value
     */
    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return encryptedPrefs.getBoolean(key, defaultValue)
    }

    /**
     * Removes a preference
     */
    fun remove(key: String) {
        encryptedPrefs.edit().remove(key).apply()
    }

    /**
     * Clears all preferences
     */
    fun clear() {
        encryptedPrefs.edit().clear().apply()
    }

    /**
     * Checks if a key exists
     */
    fun contains(key: String): Boolean {
        return encryptedPrefs.contains(key)
    }

    /**
     * Validates preference key to prevent injection attacks
     */
    private fun isValidKey(key: String): Boolean {
        // Allow only alphanumeric characters, underscores, and dots
        return key.matches(Regex("^[a-zA-Z0-9_.]+$"))
    }
}

