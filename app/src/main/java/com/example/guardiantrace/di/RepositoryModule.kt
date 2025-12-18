package com.example.guardiantrace.di

import com.example.guardiantrace.data.repository.AttachmentRepositoryImpl
import com.example.guardiantrace.data.repository.EmergencyContactRepository
import com.example.guardiantrace.data.repository.IncidentRepositoryImpl
import com.example.guardiantrace.data.repository.SecurityRepositoryImpl
import com.example.guardiantrace.domain.repository.AttachmentRepository
import com.example.guardiantrace.domain.repository.EmergencyContactRepositoryImpl
import com.example.guardiantrace.domain.repository.IncidentRepository
import com.example.guardiantrace.domain.repository.SecurityRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule  {

    @Binds
    @Singleton
    abstract fun binIncidentRepository(
        impl: IncidentRepositoryImpl
    ): IncidentRepository

    @Binds
    @Singleton
    abstract fun bindAttachmentRepository(
        impl: AttachmentRepositoryImpl
    ): AttachmentRepository

    @Binds
    @Singleton
    abstract fun bindEmergencyContactRepository(
        impl: EmergencyContactRepositoryImpl
    ): EmergencyContactRepository

    @Binds
    @Singleton
    abstract fun bindSecurityRepository(
        impl: SecurityRepositoryImpl
    ): SecurityRepository
}