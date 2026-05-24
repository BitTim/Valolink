/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       build.gradle.kts
 * Module:     Valolink.androidApp
 * Author:     Tim Anhalt (BitTim)
 * Modified:   24.05.26, 18:11
 */

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

android {
    namespace = "dev.bittim.valolink"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "dev.bittim.valolink"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(projects.shared)
    implementation(libs.androidx.activity.compose)
    implementation(libs.koin.android)
    debugImplementation(libs.compose.uiTooling)

    constraints {
        implementation("androidx.compose.material3.adaptive:adaptive") {
            version { strictly("1.3.0-alpha07") }
        }
        implementation("androidx.compose.material3.adaptive:adaptive-layout") {
            version { strictly("1.3.0-alpha07") }
        }
        implementation("androidx.compose.material3.adaptive:adaptive-navigation") {
            version { strictly("1.3.0-alpha07") }
        }
        implementation("androidx.compose.material3.adaptive:adaptive-navigation3") {
            version { strictly("1.3.0-alpha07") }
        }
    }
}
