package com.example.guardiantrace.domain.module

data class EmergencyContact(
    val id: Long = 0,
    val name: String,
    val phoneNumber: String,
    val email: String? = null,
    val isActive: Boolean = true,
    val priority: Int = 1,
    val relationship: String
) {
    fun isValid(): Boolean {
        return name.isNotBlank() && phoneNumber.isNotBlank()
    }
}