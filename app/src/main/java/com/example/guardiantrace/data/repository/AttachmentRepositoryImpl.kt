package com.example.guardiantrace.data.repository

import com.example.guardiantrace.data.encryption.EncryptionManager
import com.example.guardiantrace.data.encryption.HashingManager
import com.example.guardiantrace.data.local.dao.AttachmentDao
import com.example.guardiantrace.data.local.mapper.AttachmentMapper
import com.example.guardiantrace.domain.module.Attachment
import com.example.guardiantrace.domain.repository.AttachmentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AttachmentRepositoryImpl @Inject constructor(
    private val attachmentDao: AttachmentDao,
    private val encryptionManager: EncryptionManager,
    private val hashingManager: HashingManager
) : AttachmentRepository {


    override suspend fun addAttachment(
        incidentId: Long,
        file: File,
        fileName: String
    ): Result<Attachment> {
        return try {
            // Read file bytes
            val fileBytes = file.readBytes()

            // Encrypt file
            val encryptedData = encryptionManager.encryptFile(fileBytes)

            // Hash file
            val sha256Hash = hashingManager.hashFile(fileBytes)

            // Save encrypted file to disk
            val encryptedFilePath = "attachments/$incidentId/$fileName.enc"
            val encryptedFile = File(encryptedFilePath)
            encryptedFile.parentFile?.mkdirs()
            encryptedFile.writeBytes(encryptedData.cipherText)

            // Save IV to disk
            val ivFilePath = "$encryptedFilePath.iv"
            val ivFile = File(ivFilePath)
            ivFile.writeBytes(encryptedData.iv)

            // Create AttachmentEntity
            val attachmentEntity = com.example.guardiantrace.data.local.entity.AttachmentEntity(
                incidentId = incidentId,
                fileName = fileName,
                filePath = encryptedFilePath,
                fileSize = fileBytes.size.toLong(),
                sha256Hash = sha256Hash,
                createdAt = System.currentTimeMillis(),
                fileType = file.extension,
                mimeType =  java.nio.file.Files.probeContentType(file.toPath()) ?: "application/octet-stream"
            )

            // Insert into database
            val attachmentId = attachmentDao.insertAttachment(attachmentEntity)

            // Map to domain model
            val attachment = AttachmentMapper.toDomain(
                attachmentEntity.copy(id = attachmentId)
            )

            Result.success(attachment)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun getAttachmentByIncidentId(incidentId: Long): Flow<List<Attachment>> {
        return attachmentDao.getAttachmentsByIncidentId(incidentId).map { entities ->
            entities.map { AttachmentMapper.toDomain(it) }
        }
    }

    override suspend fun getAttachmentById(attachmentId: Long): Result<Attachment?> {
        return try {
            val entity = attachmentDao.getAttachmentById(attachmentId)
            val attachment = entity?.let { AttachmentMapper.toDomain(it) }
            Result.success(attachment)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun decryptAttachment(attachment: Attachment): Result<File> {
        return try {
            val entity = attachmentDao.getAttachmentById(attachment.id)
                ?: return Result.failure(Exception("Attachment not found"))

            // Read encrypted file
            val encryptedFile = File(entity.filePath)
            if (!encryptedFile.exists()) {
                return Result.failure(Exception("Encrypted file not found"))
            }

            val ciphertext = encryptedFile.readBytes()

            // Read IV
            val ivFile = File("${entity.filePath}.iv")
            if (!ivFile.exists()) {
                return Result.failure(Exception("IV file not found"))
            }

            val iv = ivFile.readBytes()

            // Decrypt
            val encryptedData = com.example.guardiantrace.data.encryption.EncryptedData(
                cipherText = ciphertext,
                iv = iv
            )

            val decryptedData = encryptionManager.decryptFile(encryptedData)

            val decryptedFile = File(attachment.encryptedFilePath)
            decryptedFile.writeBytes(decryptedData)

            Result.success(decryptedFile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteAttachment(id: Long): Result<Unit> {
        return try {
            val entity = attachmentDao.getAttachmentById(id)
                ?: return Result.failure(Exception("Attachment not found"))

            // Delete files from disk
            val encryptedFile = File(entity.filePath)
            if (encryptedFile.exists()) {
                encryptedFile.delete()
            }

            val ivFile = File("${entity.filePath}.iv")
            if (ivFile.exists()) {
                ivFile.delete()
            }

            // Delete from database
            attachmentDao.deleteAttachment(entity)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun verifyAttachmentIntegrity(attachment: Attachment): Result<Boolean> {
        return try {
            val entity = attachmentDao.getAttachmentById(attachment.id)
                ?: return Result.failure(Exception("Attachment not found"))

            // Decrypt file
            val decryptResult = decryptAttachment(attachment)
            if (decryptResult.isFailure) {
                return Result.failure(decryptResult.exceptionOrNull()!!)
            }
            val decryptedFile = decryptResult.getOrNull()!!

            val decryptedData = decryptedFile.readBytes()
            val isValid = hashingManager.verifyHash(decryptedData, entity.sha256Hash)

            // Verify hash
            Result.success(isValid)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}