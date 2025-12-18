package com.example.guardiantrace.data.encryption

import java.security.MessageDigest
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class HashingManager @Inject constructor() {

    fun sha26(data: ByteArray): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(data)
        return hash.joinToString("") { "%02x".format(it) }
    }

    fun sha256(data: String): String {
        return sha26(data.toByteArray(Charsets.UTF_8))
    }

    fun verifyHash(data: ByteArray, expectedHash: String): Boolean {
        val actualHash = sha256(data.toString())
        return actualHash.equals(expectedHash, ignoreCase = true)
    }

    fun hashFile(fileBytes: ByteArray): String {
        return sha26(fileBytes)
    }
}