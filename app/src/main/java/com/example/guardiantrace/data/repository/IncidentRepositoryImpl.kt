package com.example.guardiantrace.data.repository

import com.example.guardiantrace.data.local.dao.AttachmentDao
import com.example.guardiantrace.data.local.dao.IncidentDao
import com.example.guardiantrace.data.local.mapper.AttachmentMapper
import com.example.guardiantrace.data.local.mapper.IncidentMapper
import com.example.guardiantrace.domain.module.Incident
import com.example.guardiantrace.domain.repository.IncidentRepository
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Singleton
class IncidentRepositoryImpl @Inject constructor(
    private val incidentDao: IncidentDao,
    private val attachmentDao: AttachmentDao
) : IncidentRepository {

    override suspend fun createIncident(incident: Incident): Result<Long> {
        return try {
            val entity = IncidentMapper.toEntity(incident)
            val id = incidentDao.insert(entity)
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getAllIncidents(): Flow<List<Incident>> {
        return incidentDao.getAllIncidents().map { entities ->
            entities.map { entity ->
                val incident = IncidentMapper.toDomain(entity)
                // Load attachments for each incident
                val attachments = attachmentDao.getAttachmentsByIncidentId(entity.id)
                    .map { AttachmentMapper.toDomainList(it) }

                incident
            }
        }
    }

    override suspend fun getIncidentById(id: Long): Result<Incident?> {
        return try {
            val incidentWithAttachments = incidentDao.getIncidentWithAttachments(id)

            if (incidentWithAttachments == null) {
                Result.success(null)
            } else {
                val incident = IncidentMapper.toDomain(incidentWithAttachments.incident)
                AttachmentMapper.toDomainList(incidentWithAttachments.attachmentIds)

                Result.success(incident.copy())
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateIncident(incident: Incident): Result<Unit> {
        return try {
            val entity = IncidentMapper.toEntity(incident)
                .copy(updatedAt = System.currentTimeMillis())
            incidentDao.update(entity)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteIncident(incidentId: Long): Result<Unit> {
        return try {
            incidentDao.softDelete(incidentId, System.currentTimeMillis())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun searchIncidents(query: String): Flow<List<Incident>> {
        return incidentDao.searchIncidents(query).map { entities ->
            IncidentMapper.toDomainList(entities)
        }
    }

    override fun getIncidentByDateRange(startDate: Long, endDate: Long): Flow<List<Incident>> {
        return incidentDao.getIncidentsByDateRange(startDate, endDate).map { entities ->
            IncidentMapper.toDomainList(entities)
        }
    }

    override fun getIncidentCount(): Flow<Int> {
        return incidentDao.getIncidentCount()
    }

    override fun getIncidentsWithLocation(): Flow<List<Incident>> {
        return incidentDao.getIncidentsWithLocation().map { entities ->
            IncidentMapper.toDomainList(entities)
        }
    }

}