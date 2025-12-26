package com.example.guardiantrace.data.security

import android.util.Log
import com.example.guardiantrace.BuildConfig

/**
 * Secure logging utility that filters Personally Identifiable Information (PII)
 * and sensitive data from logs to prevent data leakage in logcat.
 *
 * Logs are disabled in production builds.
 * Implements OWASP Mobile Security Guidelines for secure logging.
 */
object SecureLogger {

    private const val TAG = "GuardianTrace"

    // Patterns for detecting sensitive data
    private val PHONE_PATTERN = Regex("""(\d{2}\s?\d{4,5}-?\d{4})|(\(\d{2}\)\s?\d{4,5}-?\d{4})""")
    private val EMAIL_PATTERN = Regex("""[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}""")
    private val CARD_PATTERN = Regex("""\d{4}[\s-]?\d{4}[\s-]?\d{4}[\s-]?\d{4}""")
    private val CPF_PATTERN = Regex("""\d{3}\.?\d{3}\.?\d{3}-?\d{2}""")
    private val IP_PATTERN = Regex("""\b(?:\d{1,3}\.){3}\d{1,3}\b""")

    /**
     * Filters out sensitive information from log messages
     * @param message The message to filter
     * @return Filtered message with sensitive data masked
     */
    fun filterSensitiveData(message: String?): String {
        if (message == null || message.isEmpty()) return ""

        var filtered = message

        // Mask phone numbers
        filtered = filtered.replace(PHONE_PATTERN) { "XXX-XXXX" }

        // Mask email addresses
        filtered = filtered.replace(EMAIL_PATTERN) { "***@***.***" }

        // Mask credit cards
        filtered = filtered.replace(CARD_PATTERN) { "XXXX-XXXX-XXXX-XXXX" }

        // Mask CPF
        filtered = filtered.replace(CPF_PATTERN) { "XXX.XXX.XXX-XX" }

        // Mask IP addresses
        filtered = filtered.replace(IP_PATTERN) { "X.X.X.X" }

        // Mask common sensitive keywords
        filtered = filtered.replace(Regex("password\\s*[:=]\\s*[^,\\s]+", RegexOption.IGNORE_CASE)) { "password=***" }
        filtered = filtered.replace(Regex("pin\\s*[:=]\\s*[^,\\s]+", RegexOption.IGNORE_CASE)) { "pin=***" }
        filtered = filtered.replace(Regex("token\\s*[:=]\\s*[^,\\s]+", RegexOption.IGNORE_CASE)) { "token=***" }
        filtered = filtered.replace(Regex("secret\\s*[:=]\\s*[^,\\s]+", RegexOption.IGNORE_CASE)) { "secret=***" }
        filtered = filtered.replace(Regex("authorization\\s*[:=]\\s*Bearer\\s+[^,\\s]+", RegexOption.IGNORE_CASE)) { "authorization=Bearer ***" }

        return filtered
    }

    /**
     * Debug level logging (disabled in production)
     */
    fun d(message: String, throwable: Throwable? = null) {
        if (BuildConfig.ENABLE_LOGGING && BuildConfig.DEBUG) {
            val filtered = filterSensitiveData(message)
            if (throwable != null) {
                Log.d(TAG, filtered, throwable)
            } else {
                Log.d(TAG, filtered)
            }
        }
    }

    /**
     * Info level logging (disabled in production)
     */
    fun i(message: String, throwable: Throwable? = null) {
        if (BuildConfig.ENABLE_LOGGING) {
            val filtered = filterSensitiveData(message)
            if (throwable != null) {
                Log.i(TAG, filtered, throwable)
            } else {
                Log.i(TAG, filtered)
            }
        }
    }

    /**
     * Warning level logging
     */
    fun w(message: String, throwable: Throwable? = null) {
        val filtered = filterSensitiveData(message)
        if (throwable != null) {
            Log.w(TAG, filtered, throwable)
        } else {
            Log.w(TAG, filtered)
        }
    }

    /**
     * Error level logging
     */
    fun e(message: String, throwable: Throwable? = null) {
        val filtered = filterSensitiveData(message)
        if (throwable != null) {
            Log.e(TAG, filtered, throwable)
        } else {
            Log.e(TAG, filtered)
        }
    }

    /**
     * Critical error logging for security events
     */
    fun securityEvent(eventName: String, details: String? = null) {
        if (BuildConfig.DEBUG) {
            val filtered = filterSensitiveData(details ?: "")
            Log.e(TAG, "[SECURITY] $eventName: $filtered")
        }
        // In production, this should be sent to security monitoring service
    }
}

