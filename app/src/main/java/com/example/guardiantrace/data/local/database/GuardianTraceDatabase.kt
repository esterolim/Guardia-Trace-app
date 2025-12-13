package com.example.guardiantrace.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.guardiantrace.data.local.dao.AttachmentDao
import com.example.guardiantrace.data.local.dao.EmergencyContactDao
import com.example.guardiantrace.data.local.dao.IncidentDao
import com.example.guardiantrace.data.local.entity.AttachmentEntity
import com.example.guardiantrace.data.local.entity.EmergencyContactEntity
import com.example.guardiantrace.data.local.entity.IncidentEntity


@Database(
    entities = [
        IncidentEntity::class,
        AttachmentEntity::class,
        EmergencyContactEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class GuardianTraceDatabase : RoomDatabase() {

    abstract fun incidentDao(): IncidentDao
    abstract fun attachmentDao(): AttachmentDao
    abstract fun emergencyContactDao(): EmergencyContactDao

    companion object {
        const val DATABASE_NAME = "guardian_trace_encrypted.db"

    }
}