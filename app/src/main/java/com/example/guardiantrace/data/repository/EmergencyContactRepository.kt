package com.example.guardiantrace.data.repository

import com.example.guardiantrace.domain.module.EmergencyContact
import kotlinx.coroutines.flow.Flow

interface EmergencyContactRepository {
    suspend fun createContact(contact: EmergencyContact): Result<Long>
    fun getAllContacts(): Flow<List<EmergencyContact>>
    fun getActiveContacts(): Flow<List<EmergencyContact>>
    suspend fun getContactById(contactId: Long): Result<EmergencyContact?>
    suspend fun updateContact(contact: EmergencyContact): Result<Unit>
    suspend fun deleteContact(contactId: Long): Result<Unit>
    suspend fun updateContactPriority(contactId: Long, priority: Int): Result<Unit>
    suspend fun toggleContactActive(contactId: Long, isActive: Boolean): Result<Unit>
    fun getActiveContactCount(): Flow<Int>
}