package com.example.guardiantrace.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.guardiantrace.data.local.entity.IncidentEntity
import com.example.guardiantrace.data.local.relation.IncidentWithAttachments
import kotlinx.coroutines.flow.Flow


@Dao
interface IncidentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(incident: IncidentEntity): Long

    @Update
    suspend fun update(incident: IncidentEntity)

    @Delete
    suspend fun delete(incident: IncidentEntity)

    @Query("UPDATE incidents SET is_deleted = 1, updated_at = :timestamp WHERE id = :incidentId")
    suspend fun softDelete(incidentId: Long, timestamp: Long)

    @Query("SELECT * FROM incidents WHERE is_deleted = 0 ORDER BY timestamp DESC")
    suspend fun getAllActiveIncidents(): Flow<List<IncidentEntity>>

    @Query("SELECT * FROM incidents WHERE id = :incidentId AND is_deleted = 0")
    fun getIncidentByIdFlow(incidentId: Long): Flow<IncidentEntity?>

    @Transaction
    @Query("SELECT * FROM incidents WHERE id = :incidentId AND is_deleted = 0")
    suspend fun getIncidentWithAttachments(incidentId: Long): IncidentWithAttachments?

    @Transaction
    @Query("SELECT * FROM incidents WHERE is_deleted = 0 ORDER BY timestamp DESC")
    fun getAllIncidentsWithAttachments(): Flow<List<IncidentWithAttachments>>

    @Query(
        """
        SELECT * FROM incidents 
        WHERE is_deleted = 0 
        AND (title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%')
        ORDER BY timestamp DESC
    """
    )
    fun searchIncidents(query: String): Flow<List<IncidentEntity>>

    @Query(
        """
        SELECT * FROM incidents 
        WHERE is_deleted = 0 
        AND timestamp BETWEEN :startDate AND :endDate
        ORDER BY timestamp DESC
    """
    )
    fun getIncidentsByDateRange(startDate: Long, endDate: Long): Flow<List<IncidentEntity>>


    @Query(
        """
        SELECT * FROM incidents 
        WHERE is_deleted = 0 
        AND latitude IS NOT NULL 
        AND longitude IS NOT NULL
        ORDER BY timestamp DESC
    """
    )
    fun getIncidentsWithLocation(): Flow<List<IncidentEntity>>

    @Query("SELECT COUNT(*) FROM incidents WHERE is_deleted = 0")
    fun getIncidentCount(): Flow<Int>

    @Query(
        """
        SELECT COUNT(*) FROM incidents 
        WHERE is_deleted = 0 
        AND timestamp >= :monthStartTimestamp
    """
    )
    fun getIncidentCountThisMonth(monthStartTimestamp: Long): Flow<Int>

    @Query("DELETE FROM incidents")
    suspend fun deleteAll()

}