package com.example.guardiantrace.domain.repository

import com.example.guardiantrace.domain.module.Incident
import kotlinx.coroutines.flow.Flow


interface IncidentRepository {

    suspend fun createIncident(incident: Incident): Result<Long>

    fun getAllIncidents(): Result<List<Incident>>

    suspend fun updateIncident(incident: Incident): Result<Unit>

    suspend fun deleteIncident(incidentId: Long): Result<Unit>

    fun getIncidentByDateRange(
        startDate: Long,
        endDate: Long
    ): Flow<List<Incident>>
}