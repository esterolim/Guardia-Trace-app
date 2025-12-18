package com.example.guardiantrace.di

import android.content.Context
import androidx.room.Room
import com.example.guardiantrace.data.local.dao.AttachmentDao
import com.example.guardiantrace.data.local.dao.EmergencyContactDao
import com.example.guardiantrace.data.local.dao.IncidentDao
import com.example.guardiantrace.data.local.database.GuardianTraceDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): GuardianTraceDatabase {
        val passphrase = SQLiteDatabase.getBytes("guardian_trace_secure_key_2024".toCharArray())
        val factory = SupportFactory(passphrase)

        return Room.databaseBuilder(
            context,
            GuardianTraceDatabase::class.java,
            GuardianTraceDatabase.DATABASE_NAME
        )
            .openHelperFactory(factory)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideIncidentDao(database: GuardianTraceDatabase): IncidentDao {
        return database.incidentDao()
    }

    @Provides
    @Singleton
    fun provideAttachmentDao(database: GuardianTraceDatabase): AttachmentDao {
        return database.attachmentDao()
    }

    @Provides
    @Singleton
    fun provideEmergencyContactDao(database: GuardianTraceDatabase): EmergencyContactDao {
        return database.emergencyContactDao()
    }


}