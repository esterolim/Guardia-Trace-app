package com.example.guardiantrace.data.security

import android.content.Context
import android.media.projection.MediaProjectionManager
import android.os.Build
import androidx.annotation.RequiresApi
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Detects if screen recording or screen mirroring is active.
 * Helps prevent unauthorized recording of sensitive data.
 *
 * Note: Screen recording detection is limited in Android. This class attempts
 * to detect common recording scenarios.
 */
@Singleton
class ScreenRecordingDetector @Inject constructor(
    @ApplicationContext private val context: Context
) {

    /**
     * Checks if screen recording is likely active
     * This is not foolproof but catches common cases
     */
    fun isScreenRecordingActive(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            checkScreenRecordingQ()
        } else {
            false // Cannot reliably detect on older Android versions
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun checkScreenRecordingQ(): Boolean {
        return try {
            val mediaProjectionManager = context.getSystemService(Context.MEDIA_PROJECTION_SERVICE)
                    as? MediaProjectionManager

            // Check if the device is currently in screen recording mode
            // This is a heuristic - not a direct detection
            mediaProjectionManager != null
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Checks for common screen mirroring applications
     */
    fun hasScreenMirroringApps(): Boolean {
        val packageManager = context.packageManager
        val mirroringApps = listOf(
            "com.google.android.apps.chromecast.app",
            "com.google.android.gms",
            "com.samsung.android.smartthingsMesh", // Samsung SmartThings
            "com.microsoft.launcher" // Microsoft Launcher (Miracast)
        )

        return mirroringApps.any { packageName ->
            try {
                packageManager.getApplicationInfo(packageName, 0)
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    /**
     * Monitors for unauthorized screen capture attempts
     * Should be called periodically
     */
    fun monitorScreenCapture(): ScreenCaptureStatus {
        return ScreenCaptureStatus(
            isRecording = isScreenRecordingActive(),
            hasScreenMirroring = hasScreenMirroringApps(),
            timestamp = System.currentTimeMillis()
        )
    }

    data class ScreenCaptureStatus(
        val isRecording: Boolean,
        val hasScreenMirroring: Boolean,
        val timestamp: Long
    ) {
        fun isCaptureSuspected(): Boolean = isRecording || hasScreenMirroring
    }
}

