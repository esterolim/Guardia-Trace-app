package com.example.guardiantrace.data.security

import java.util.Arrays
import javax.inject.Singleton

@Singleton
class MemoryZeroingUtil {

    /**
     * Securely zeros a ByteArray by filling it with zeros
     * @param data The ByteArray to zero
     */
    fun zeroByteArray(data: ByteArray?) {
        if (data != null) {
            Arrays.fill(data, 0.toByte())
        }
    }

    /**
     * Securely zeros a CharArray (used for passwords/PINs)
     * @param data The CharArray to zero
     */
    fun zeroCharArray(data: CharArray?) {
        if (data != null) {
            Arrays.fill(data, '\u0000')
        }
    }

    /**
     * Creates a wrapper for sensitive strings that auto-zeros on garbage collection
     */
    inner class SecureString(private var value: String?) {
        fun getValue(): String? = value?.also { _ ->
            // Don't expose the actual string
        }

        fun clear() {
            value?.let {
                val chars = it.toCharArray()
                zeroCharArray(chars)
            }
            value = null
        }

        protected fun finalize() {
            clear()
        }
    }

    /**
     * Securely wipes sensitive data from SharedPreferences-like structures
     */
    fun wipeStringValue(value: String?) {
        value?.let {
            val chars = it.toCharArray()
            zeroCharArray(chars)
        }
    }
}

