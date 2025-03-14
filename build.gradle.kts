// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {
        classpath (libs.dagger.hilt.android.gradle.plugin)
        classpath (libs.navigation.safe.args.gradle.plugin) // Safe Args eklendi
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    id("com.google.dagger.hilt.android") version "2.44" apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
}