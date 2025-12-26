package com.example.guardiantrace.data.security

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import com.example.guardiantrace.BuildConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IntegrityChecker @Inject constructor(
    @ApplicationContext private val context: Context
) {

    /**
     * Checks if device appears to be rooted (Android only)
     * This is not foolproof but catches common cases
     */
    fun isDeviceRooted(): Boolean {
        return checkRootMethod1() || checkRootMethod2() || checkRootMethod3()
    }

    private fun checkRootMethod1(): Boolean {
        // Check for common rooting paths
        val paths = arrayOf(
            "/system/app/Superuser.apk",
            "/sbin/su",
            "/system/bin/su",
            "/system/xbin/su",
            "/data/local/xbin/su",
            "/data/local/bin/su",
            "/system/sd/xbin/su",
            "/system/bin/failsafe/su",
            "/data/local/su",
            "/su/bin/su"
        )
        return paths.any { java.io.File(it).exists() }
    }

    private fun checkRootMethod2(): Boolean {
        return try {
            val file = java.io.File("/system/app/Superuser.apk")
            file.exists()
        } catch (e: Exception) {
            false
        }
    }

    private fun checkRootMethod3(): Boolean {
        var process: Process? = null
        return try {
            process = Runtime.getRuntime().exec("which su")
            process.inputStream.available() > 0
        } catch (e: Exception) {
            false
        } finally {
            process?.destroy()
        }
    }

    fun isDebuggerAttached(): Boolean {
        return try {
            android.os.Debug.isDebuggerConnected() || android.os.Debug.waitingForDebugger()
        } catch (e: Exception) {
            false
        }
    }

    fun isFridaDetected(): Boolean {
        val fridaLibs = arrayOf(
            "frida-agent.so",
            "frida-gadget.so",
            "frida.so"
        )

        return try {
            for (lib in fridaLibs) {
                if (Runtime.getRuntime().exec("find /system -name $lib 2>/dev/null").waitFor() == 0) {
                    return true
                }
            }
            false
        } catch (e: Exception) {
            false
        }
    }

    fun verifyPackageSignature(): Boolean {
        return try {
            val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                context.packageManager.getPackageInfo(
                    context.packageName,
                    PackageManager.GET_SIGNING_CERTIFICATES
                )
            } else {
                @Suppress("DEPRECATION")
                context.packageManager.getPackageInfo(
                    context.packageName,
                    PackageManager.GET_SIGNATURES
                )
            }

            // For now, just verify that signature exists
            // In production, should verify against a known good hash
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageInfo.signingInfo != null && !packageInfo.signingInfo!!.signingCertificateHistory.isEmpty()
            } else {
                @Suppress("DEPRECATION")
                packageInfo.signatures != null && packageInfo.signatures!!.isNotEmpty()
            }
        } catch (e: Exception) {
            SecureLogger.e("Error verifying package signature", e)
            false
        }
    }

    fun performIntegrityCheck(): Boolean {
        val checks = listOf(
            Pair("Debugger Detection", isDebuggerAttached()),
            Pair("Root Detection", isDeviceRooted()),
            Pair("Frida Detection", isFridaDetected()),
            Pair("Signature Verification", verifyPackageSignature())
        )

        var allChecksPassed = true
        for ((checkName, isFailed) in checks) {
            if (isFailed) {
                SecureLogger.securityEvent(
                    "Integrity Check Failed",
                    "$checkName detected potential tampering"
                )
                allChecksPassed = false
            }
        }

        return allChecksPassed
    }

    fun getIntegrityReport(): IntegrityReport {
        return IntegrityReport(
            isDebuggerAttached = isDebuggerAttached(),
            isDeviceRooted = isDeviceRooted(),
            isFridaDetected = isFridaDetected(),
            isSignatureValid = verifyPackageSignature(),
            timestamp = System.currentTimeMillis()
        )
    }

    data class IntegrityReport(
        val isDebuggerAttached: Boolean,
        val isDeviceRooted: Boolean,
        val isFridaDetected: Boolean,
        val isSignatureValid: Boolean,
        val timestamp: Long
    ) {
        fun isIntact(): Boolean = !isDebuggerAttached && !isDeviceRooted && !isFridaDetected && isSignatureValid
    }
}

