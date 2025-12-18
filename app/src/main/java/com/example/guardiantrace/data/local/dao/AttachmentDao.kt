package com.example.guardiantrace.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.guardiantrace.data.local.entity.AttachmentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AttachmentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttachment(attachment: AttachmentEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllAttachments(attachments: List<AttachmentEntity>)

    @Update
    suspend fun updateAttachment(attachment: AttachmentEntity)

    @Delete
    suspend fun deleteAttachment(attachment: AttachmentEntity)

    @Query("SELECT * FROM attachments WHERE incident_id = :incidentId ORDER BY created_at DESC")
    suspend fun getAttachmentsByIncidentId(incidentId: Long): Flow<List<AttachmentEntity>>

    @Query("SELECT * FROM attachments WHERE incident_id = :incidentId AND file_type = :fileType")
    suspend fun getAttachmentsByType(incidentId: Long, fileType: String): Flow<List<AttachmentEntity>>

    @Query("SELECT COUNT(*) FROM attachments WHERE incident_id = :incidentId")
    fun getAttachmentCount(incidentId: Long): Flow<Int>

    @Query("SELECT * FROM attachments WHERE id = :attachmentId")
    suspend fun getAttachmentById(attachmentId: Long): AttachmentEntity?

    @Query("SELECT COUNT(*) FROM attachments")
    fun getTotalAttachmentCount(): Flow<Int>

    @Query("SELECT SUM(file_size) FROM attachments")
    fun getTotalAttachmentSize(): Flow<Long?>

    @Query("DELETE FROM attachments WHERE incident_id = :incidentId")
    suspend fun deleteAttachmentsByIncidentId(incidentId: Long)

    @Query("DELETE FROM attachments")
    suspend fun deleteAllAttachments()
}