/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       build.gradle.kts
 * Module:     Valolink.shared
 * Author:     Tim Anhalt (BitTim)
 * Modified:   24.05.26, 15:48
 */

import com.codingfeline.buildkonfig.compiler.FieldSpec
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.io.FileInputStream
import java.util.*

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)

    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.androidx.room)

    id("com.codingfeline.buildkonfig") version "+"
}

room {
    schemaDirectory("$projectDir/schemas")
}

kotlin {
    android {
        namespace = "dev.bittim.valolink.shared"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }

        androidResources {
            enable = true
        }
    }
    
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    sourceSets {
        all {
            languageSettings.optIn("kotlin.time.ExperimentalTime")
            languageSettings.optIn("kotlin.uuid.ExperimentalUuidApi")
        }

        androidMain.dependencies {
            implementation(libs.koin.android)
            implementation(libs.compose.uiToolingPreview)
        }
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.compose.material.icons.extended)

            // Koin
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.koin.compose.viewmodel.navigation)
            implementation(libs.koin.compose.navigation3)

            // Navigation 3
            implementation(libs.jetbrains.navigation3.ui)
            implementation(libs.jetbrains.material3.adaptiveNavigation3)
            implementation(libs.jetbrains.lifecycle.viewmodelNavigation3)

            // Kotlin Extensions
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.coroutines.core)

            // Ktor
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.cio)

            // Coil
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor3)

            // Room
            implementation(libs.androidx.room.runtime)
            implementation(libs.androidx.sqlite.bundled)

            // Supabase
            implementation(project.dependencies.platform(libs.supabase.bom))
            implementation(libs.auth.kt)
            implementation(libs.postgrest.kt)
            implementation(libs.storage.kt)
            implementation(libs.realtime.kt)
            implementation(libs.coil3.integration)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)

    add("kspAndroid", libs.androidx.room.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room.compiler)
    add("kspIosArm64", libs.androidx.room.compiler)
}

buildkonfig {
    packageName = "dev.bittim.valolink"

    defaultConfigs {
        // Extract Supabase credentials from .env file or environment variables
        val supabaseUrl: String
        val supabaseKey: String

        val propertiesFile = file("../../.env")
        if (propertiesFile.canRead()) {
            val properties = Properties()
            properties.load(FileInputStream(propertiesFile))

            supabaseUrl = properties.getProperty("SUPABASE_URL")
            supabaseKey = properties.getProperty("SUPABASE_KEY")
        } else {
            println("Could not read .env file, using environment variables instead")
            supabaseUrl = System.getenv("SUPABASE_URL") ?: ""
            supabaseKey = System.getenv("SUPABASE_KEY") ?: ""
        }

        require(supabaseUrl.isNotEmpty()) {
            "Supabase Url not set. Please set add SUPABASE_URL to .env file at repo root or set the SUPABASE_URL environment variable. You can find your project URL in your project's dashboard."
        }

        require(supabaseKey.isNotEmpty()) {
            "Supabase Key not set. Please set add SUPABASE_KEY to .env file at repo root or set the SUPABASE_KEY environment variable. You can find your sharable key in your project's dashboard."
        }

        buildConfigField(FieldSpec.Type.STRING, "SUPABASE_URL", supabaseUrl)
        buildConfigField(FieldSpec.Type.STRING, "SUPABASE_KEY", supabaseKey)
    }
}