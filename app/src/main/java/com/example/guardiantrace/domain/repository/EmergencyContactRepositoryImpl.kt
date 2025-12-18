package com.example.guardiantrace.domain.repository

import com.example.guardiantrace.data.local.dao.EmergencyContactDao
import com.example.guardiantrace.data.local.mapper.EmergencyContactMapper
import com.example.guardiantrace.data.repository.EmergencyContactRepository
import com.example.guardiantrace.domain.module.EmergencyContact
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EmergencyContactRepositoryImpl @Inject constructor(
    private val contactDao: EmergencyContactDao
) : EmergencyContactRepository {

    /**
     * Creates a new emergency contact
     */
    override suspend fun createContact(contact: EmergencyContact): Result<Long> {
        return try {
            val entity = EmergencyContactMapper.toEntity(contact)
            val id = contactDao.insertEmergencyContact(entity)
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Gets all emergency contacts
     */
    override fun getAllContacts(): Flow<List<EmergencyContact>> {
        return contactDao.getActiveContacts().map { entities ->
            EmergencyContactMapper.toDomainList(entities)
        }
    }

    /**
     * Gets active emergency contacts only
     */
    override fun getActiveContacts(): Flow<List<EmergencyContact>> {
        return contactDao.getActiveContacts().map { entities ->
            EmergencyContactMapper.toDomainList(entities)
        }
    }

    /**
     * Gets contact by ID
     */
    override suspend fun getContactById(contactId: Long): Result<EmergencyContact?> {
        return try {
            val entity = contactDao.getContactById(contactId)
            val contact = entity?.let { EmergencyContactMapper.toDomain(it) }
            Result.success(contact)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Updates an emergency contact
     */
    override suspend fun updateContact(contact: EmergencyContact): Result<Unit> {
        return try {
            val entity = EmergencyContactMapper.toEntity(contact)
            contactDao.updateEmergencyContact(entity)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Deletes an emergency contact
     */
    override suspend fun deleteContact(contactId: Long): Result<Unit> {
        return try {
            val entity = contactDao.getContactById(contactId)
            if (entity != null) {
                contactDao.deleteEmergencyContact(entity)
                Result.success(Unit)
            } else {
                Result.failure(Exception("Contact not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Updates contact priority
     */
    override suspend fun updateContactPriority(contactId: Long, priority: Int): Result<Unit> {
        return try {
            contactDao.updateContactPriority(contactId, priority, System.currentTimeMillis())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Toggles contact active status
     */
    override suspend fun toggleContactActive(contactId: Long, isActive: Boolean): Result<Unit> {
        return try {
            contactDao.toggleActive(contactId, isActive, System.currentTimeMillis())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    /**
     * Gets count of active contacts
     */
    override fun getActiveContactCount(): Flow<Int> {
        return contactDao.getActiveContactCount()
    }
}