package com.example.guardiantrace.data.local.mapper

import com.example.guardiantrace.data.local.entity.AttachmentEntity
import com.example.guardiantrace.domain.module.Attachment
import com.example.guardiantrace.domain.module.AttachmentType
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.ZoneOffset


object AttachmentMapper {

    fun toEntity(attachment: Attachment, incidentId: Long): AttachmentEntity {
        return AttachmentEntity(
            id = attachment.id,
            incidentId = incidentId,
            fileName = attachment.fileName,
            fileType = attachment.fileType.name,
            fileSize = attachment.fileSize,
            filePath = attachment.encryptedFilePath,
            sha256Hash = attachment.sha256Hash,
            createdAt = attachment.createdAt.toEpochSecond(ZoneOffset.UTC),
            metadata = attachment.metadata.let { Gson().toJson(it) },
            mimeType = attachment.mimeType
        )

    }

    fun toDomain(entity: AttachmentEntity): Attachment {
        return Attachment(
            id = entity.id,
            fileName = entity.fileName,
            encryptedFilePath = entity.filePath,
            fileType = AttachmentType.valueOf(entity.fileType),
            fileSize = entity.fileSize,
            sha256Hash = entity.sha256Hash,
            createdAt = entity.createdAt.let {
                java.time.LocalDateTime.ofEpochSecond(
                    it,
                    0,
                    ZoneOffset.UTC
                )
            },
            metadata = entity.metadata.let {
                val type = TypeToken.getParameterized(
                    Map::class.java,
                    String::class.java,
                    String::class.java
                ).type
                Gson().fromJson(it, type)
            },
            mimeType = entity.mimeType
        )
    }

    fun toDomainList(entities: List<AttachmentEntity>): List<Attachment> {
        return entities.map { toDomain(it) }
    }
}