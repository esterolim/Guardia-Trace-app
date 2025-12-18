package com.example.guardiantrace.data.local.mapper

import com.example.guardiantrace.data.local.entity.IncidentEntity
import com.example.guardiantrace.domain.module.Incident
import java.time.LocalDateTime
import java.time.ZoneOffset

/**
 * Mapper between IncidentEntity (data layer) and Incident (domain layer)
 * Follows clean architecture separation of concerns
 */
object IncidentMapper {

    /**
     * Converts domain model to entity
     */
    fun toEntity(incident: Incident): IncidentEntity {
        return IncidentEntity(
            id = incident.id,
            title = incident.title,
            description = incident.description,
            timestamp = incident.timeStamp.toEpochSecond(ZoneOffset.UTC),
            latitude = incident.latitude,
            longitude = incident.longitude,
            address = incident.address,
            createdAt = incident.createdAt.toEpochSecond(ZoneOffset.UTC),
            updatedAt = incident.updatedAt.toEpochSecond(ZoneOffset.UTC),
            isDeleted = incident.isDeleted
        )
    }

    /**
     * Converts entity to domain model
     */
    fun toDomain(entity: IncidentEntity): Incident {
        return Incident(
            id = entity.id,
            title = entity.title,
            description = entity.description,
            timeStamp = entity.timestamp.let { LocalDateTime.ofEpochSecond(it, 0, ZoneOffset.UTC) },
            latitude = entity.latitude,
            longitude = entity.longitude,
            address = entity.address,
            createdAt = entity.createdAt.let { LocalDateTime.ofEpochSecond(it, 0, ZoneOffset.UTC) },
            updatedAt = entity.updatedAt.let { LocalDateTime.ofEpochSecond(it, 0, ZoneOffset.UTC) },
            isDeleted = entity.isDeleted
        )
    }

    /**
     * Converts list of entities to domain models
     */
    fun toDomainList(entities: List<IncidentEntity>): List<Incident> {
        return entities.map { toDomain(it) }
    }
}