package com.example.guardiantrace.domain.module

import java.time.LocalDateTime

data class Attachment(
    val id: Long = 0,
    val fileName: String,
    val encryptedFilePath: String,
    val fileType: AttachmentType,
    val fileSize: Long,
    val sha256Hash: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val metadata: Map<String, String> = emptyMap(),
    val mimeType: String,
) {
    fun isValid(): Boolean {
        return fileName.isNotBlank() &&
                encryptedFilePath.isNotBlank() &&
                sha256Hash.isNotBlank() &&
                fileSize > 0
    }
}

enum class AttachmentType {
    IMAGE,
    VIDEO,
    DOCUMENT,
    AUDIO,
    OTHER
}