package com.example.guardiantrace.data.repository

import com.example.guardiantrace.data.encryption.EncryptionManager
import com.example.guardiantrace.data.encryption.HashingManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object EncryptionModule {

    @Provides
    @Singleton
    fun provideEncryptionManager(): EncryptionManager {
        return EncryptionManager()
    }

    @Provides
    @Singleton
    fun provideHashingManager(): HashingManager {
        return HashingManager()
    }
}