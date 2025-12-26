# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# ============================================
# SECURITY & REVERSE ENGINEERING PROTECTION
# ============================================

# Keep security-related classes
-keep class com.example.guardiantrace.data.security.** { *; }
-keep class com.example.guardiantrace.data.encryption.** { *; }
-keepclassmembers class com.example.guardiantrace.data.security.** {
    public <methods>;
}

# Remove debug info but keep line numbers for crash reporting
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Aggressive obfuscation
-dontoptimize
-dontpreverify
-repackageclasses com.example.guardiantrace.obf
-allowaccessmodification

# Prevent removal of specific fields and methods
-keepclasseswithmembernames class * {
    native <methods>;
}

# Keep enums
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Keep Parcelables
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# Keep serializable classes
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# ============================================
# CRYPTOGRAPHY & HILT DEPENDENCIES
# ============================================

# Keep Hilt-related classes
-keep class ** extends dagger.hilt.android.lifecycle.HiltViewModel { *; }
-keep class dagger.hilt.** { *; }
-keepclasseswithmembernames class * {
    @dagger.hilt.** <fields>;
    @javax.inject.** <fields>;
}

# Keep encryption and security classes
-keep class androidx.security.crypto.** { *; }
-keep class javax.crypto.** { *; }
-keep class android.security.keystore.** { *; }

# Keep Room database classes
-keep class androidx.room.** { *; }
-keep class * extends androidx.room.RoomDatabase { *; }

# ============================================
# ANDROIDX & COMPOSE DEPENDENCIES
# ============================================

# Keep Compose classes
-keep class androidx.compose.** { *; }
-keep class androidx.lifecycle.** { *; }

# Keep custom Application classes
-keep class com.example.guardiantrace.GuardianTraceApp { *; }
-keep class com.example.guardiantrace.MainActivity { *; }

# ============================================
# DEBUGGING PROTECTION
# ============================================

# Remove Log calls in release builds
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
}

# Prevent tampering detection code from being removed
-keepclassmembers class com.example.guardiantrace.data.security.IntegrityChecker {
    public boolean isDeviceRooted();
    public boolean isDebuggerAttached();
    public boolean isFridaDetected();
}

