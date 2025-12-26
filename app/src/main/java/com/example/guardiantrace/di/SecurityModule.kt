package com.example.guardiantrace.di

import android.content.Context
import com.example.guardiantrace.data.security.IntegrityChecker
import com.example.guardiantrace.data.security.SecurityPolicyEnforcer
import com.example.guardiantrace.data.security.ScreenRecordingDetector
import com.example.guardiantrace.data.security.SecureSharedPreferencesWrapper
import com.example.guardiantrace.data.security.EncryptionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dependency Injection module for security-related components.
 *
 * Provides singleton instances of encryption, integrity checking,
 * and secure storage utilities following the OWASP Mobile Security
 * Top 10 and Google Android Security & Privacy guidelines.
 */
@Module
@InstallIn(SingletonComponent::class)
object SecurityModule {

    @Provides
    @Singleton
    fun provideEncryptionManager(): EncryptionManager {
        return EncryptionManager()
    }

    @Provides
    @Singleton
    fun provideSecureSharedPreferencesWrapper(
        @ApplicationContext context: Context
    ): SecureSharedPreferencesWrapper {
        return SecureSharedPreferencesWrapper(context)
    }

    @Provides
    @Singleton
    fun provideIntegrityChecker(
        @ApplicationContext context: Context
    ): IntegrityChecker {
        return IntegrityChecker(context)
    }

    @Provides
    @Singleton
    fun provideScreenRecordingDetector(
        @ApplicationContext context: Context
    ): ScreenRecordingDetector {
        return ScreenRecordingDetector(context)
    }

    @Provides
    @Singleton
    fun provideSecurityPolicyEnforcer(
        @ApplicationContext context: Context,
        integrityChecker: IntegrityChecker,
        screenRecordingDetector: ScreenRecordingDetector
    ): SecurityPolicyEnforcer {
        return SecurityPolicyEnforcer(context, integrityChecker, screenRecordingDetector)
    }
}


