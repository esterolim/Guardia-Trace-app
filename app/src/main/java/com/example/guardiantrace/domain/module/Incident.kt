package com.example.guardiantrace.domain.module

import java.time.LocalDateTime

data class Incident(
    val id: Long = 0,
    val title: String,
    val description: String,
    val timeStamp: LocalDateTime,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val isDeleted: Boolean = false
) {
    fun isValid(): Boolean {
        return title.isNotBlank() && description.isNotBlank()
    }

    fun hasLocation(): Boolean {
        return latitude != null && longitude != null
    }
}