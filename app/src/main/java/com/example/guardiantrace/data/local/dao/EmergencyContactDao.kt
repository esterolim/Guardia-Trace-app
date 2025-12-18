package com.example.guardiantrace.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.guardiantrace.data.local.entity.EmergencyContactEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EmergencyContactDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmergencyContact(contact: EmergencyContactEntity): Long

    @Update
    suspend fun updateEmergencyContact(contact: EmergencyContactEntity)

    @Delete
    suspend fun deleteEmergencyContact(contact: EmergencyContactEntity)

    @Query("SELECT * FROM emergency_contacts WHERE is_active = 1 ORDER BY priority ASC")
    fun getActiveContacts(): Flow<List<EmergencyContactEntity>>

    @Query("SELECT * FROM emergency_contacts WHERE id = :contactId")
    suspend fun getContactById(contactId: Long): EmergencyContactEntity?

    @Query("SELECT * FROM emergency_contacts WHERE phone_number = :phoneNumber")
    suspend fun getContactByPhoneNumber(phoneNumber: String): EmergencyContactEntity?

    @Query("SELECT COUNT(*) FROM emergency_contacts WHERE is_active = 1")
    fun getActiveContactCount(): Flow<Int>

    @Query("UPDATE emergency_contacts SET priority = :priority, updated_at = :timestamp WHERE id = :contactId")
    suspend fun updateContactPriority(contactId: Long, priority: Int, timestamp: Long)

    @Query("UPDATE emergency_contacts SET is_active = :isActive, updated_at = :timestamp WHERE id = :contactId")
    suspend fun toggleActive(contactId: Long, isActive: Boolean, timestamp: Long)

    @Query("DELETE FROM emergency_contacts")
    suspend fun deleteAll()
}