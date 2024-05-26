import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.kotlin.serialization)
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("kotlin-parcelize")
}

android {
    namespace = "dev.bittim.valolink"
    compileSdk = 34

    // Determine version
    applicationVariants.all {
        val variant = this
        variant.outputs.map { it as com.android.build.gradle.internal.api.BaseVariantOutputImpl }
            .forEach { output ->
                val outputFileName = "Valolink-${variant.baseName}-${variant.versionName}.apk"
                println("Output file name: $outputFileName")
                output.outputFileName = outputFileName
            }
    }

    val majorVersion: Int
    val minorVersion: Int
    val patchVersion: Int
    val buildVersion: Int

    val versionPropertiesFile = file("version.properties")
    if (versionPropertiesFile.canRead()) {
        val versionProperties = Properties()
        versionProperties.load(FileInputStream(versionPropertiesFile))

        majorVersion = versionProperties.getProperty("MAJOR")
            .toInt()
        minorVersion = versionProperties.getProperty("MINOR")
            .toInt()
        patchVersion = versionProperties.getProperty("PATCH")
            .toInt()
        buildVersion = versionProperties.getProperty("BUILD")
            .toInt() + 1

        versionProperties.setProperty(
            "BUILD",
            buildVersion.toString()
        )
        versionProperties.store(
            versionPropertiesFile.writer(),
            null
        )
    } else {
        throw GradleException("Could not read version.properties file")
    }

    val versionName = "v$majorVersion.$minorVersion.$patchVersion-$buildVersion"

    defaultConfig {
        applicationId = "dev.bittim.valolink"
        minSdk = 33
        targetSdk = 34
        this.versionCode = buildVersion
        this.versionName = versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        ksp {
            arg(
                "room.schemaLocation",
                "$projectDir/schemas"
            )
        }
    }

    // Retrieve signing information
    val storePassword: String
    val keyAlias: String
    val keyPassword: String

    val keystorePropertiesFile = file("keystore.properties")
    if (keystorePropertiesFile.canRead()) {
        val keystoreProperties = Properties()
        keystoreProperties.load(FileInputStream(keystorePropertiesFile))

        storePassword = keystoreProperties.getProperty("STORE_PASSWORD")
        keyAlias = keystoreProperties.getProperty("KEY_ALIAS")
        keyPassword = keystoreProperties.getProperty("KEY_PASSWORD")
    } else {
        println("Could not read keystore.properties file, using environment variables instead")
        storePassword = System.getenv("STORE_PASSWORD") ?: ""
        keyAlias = System.getenv("KEY_ALIAS") ?: ""
        keyPassword = System.getenv("KEY_PASSWORD") ?: ""
    }

    signingConfigs {
        create("release") {
            storeFile = file("../keystore.jks")
            this.storePassword = storePassword
            this.keyAlias = keyAlias
            this.keyPassword = keyPassword
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs["release"]
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.material.icons.extended.android)
    implementation(libs.material)
    implementation(libs.play.services.auth)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.material3.adaptive.navigation.suite)
    implementation(libs.androidx.adaptive)
    implementation(libs.androidx.adaptive.layout)
    implementation(libs.androidx.adaptive.navigation)
    implementation(libs.appwrite.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Dagger - Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    ksp(libs.androidx.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.moshi)

    // Moshi
    implementation(libs.moshi.kotlin)

    // Room
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Coil
    implementation(libs.coil.kt.coil.compose)
    implementation(libs.coil.gif)
    implementation(libs.apng)
}