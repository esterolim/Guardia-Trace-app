package com.example.guardiantrace.domain.repository

import com.example.guardiantrace.domain.module.Attachment
import kotlinx.coroutines.flow.Flow
import java.io.File

interface AttachmentRepository {

    suspend fun addAttachment(
        incidentId: Long,
        file: File,
        fileName: String
    ): Result<Attachment>

    suspend fun getAttachmentByIncidentId(incidentId: Long): Flow<List<Attachment>>

    suspend fun getAttachmentById(attachmentId: Long): Result<Attachment?>

    suspend fun decryptAttachment(attachment: Attachment): Result<File>

    suspend fun deleteAttachment(id: Long): Result<Unit>

    suspend fun verifyAttachmentIntegrity(attachment: Attachment): Result<Boolean>
}