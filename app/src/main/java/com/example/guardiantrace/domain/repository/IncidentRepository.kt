package com.example.guardiantrace.domain.repository

import com.example.guardiantrace.domain.module.Incident
import kotlinx.coroutines.flow.Flow


interface IncidentRepository {

    suspend fun createIncident(incident: Incident): Result<Long>

    fun getAllIncidents(): Flow<List<Incident>>

    suspend fun getIncidentById(id: Long): Result<Incident?>

    suspend fun updateIncident(incident: Incident): Result<Unit>

    suspend fun deleteIncident(incidentId: Long): Result<Unit>

    fun searchIncidents(query: String): Flow<List<Incident>>

    fun getIncidentByDateRange(
        startDate: Long,
        endDate: Long
    ): Flow<List<Incident>>

    fun getIncidentCount(): Flow<Int>

    fun getIncidentsWithLocation(): Flow<List<Incident>>
}