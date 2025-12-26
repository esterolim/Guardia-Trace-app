// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.ksp) apply false
    id("org.owasp.dependencycheck") version "12.1.9" apply false
    id("io.gitlab.arturbosch.detekt") version "1.23.8" apply false
}

// Configure security scanning for all subprojects
subprojects {
    apply(plugin = "org.owasp.dependencycheck")
    apply(plugin = "io.gitlab.arturbosch.detekt")

    // Detekt configuration
    configure<io.gitlab.arturbosch.detekt.extensions.DetektExtension> {
        config.setFrom(files("${rootDir}/detekt.yml"))
        parallel = true
        ignoreFailures = false
    }
}