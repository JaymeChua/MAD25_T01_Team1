// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false

    id("com.google.devtools.ksp") version "2.0.21-1.0.27" apply false
}



buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        // 2) Expose the Google Services Gradle plugin to submodules
        //    (This is required so that app module can apply `id("com.google.gms.google-services")`)
        classpath("com.google.gms:google-services:4.4.4")
    }
}
