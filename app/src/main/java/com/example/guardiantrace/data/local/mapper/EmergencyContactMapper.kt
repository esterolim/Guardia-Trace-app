package com.example.guardiantrace.data.local.mapper

import com.example.guardiantrace.data.local.entity.EmergencyContactEntity
import com.example.guardiantrace.domain.module.EmergencyContact

object EmergencyContactMapper {

    fun toEntity(contact: EmergencyContact): EmergencyContactEntity {
        return EmergencyContactEntity(
            id = contact.id,
            name = contact.name,
            phoneNumber = contact.phoneNumber,
            email = contact.email.orEmpty(),
            relationship = contact.relationship,
            priority = contact.priority,
            isActive = contact.isActive,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )

    }

    fun toDomain(entity: EmergencyContactEntity): EmergencyContact {
        return EmergencyContact(
            id = entity.id,
            name = entity.name,
            phoneNumber = entity.phoneNumber,
            email = entity.email,
            relationship = entity.relationship,
            priority = entity.priority,
            isActive = entity.isActive
        )
    }

    fun toDomainList(entities: List<EmergencyContactEntity>): List<EmergencyContact> {
        return entities.map { toDomain(it) }
    }
}