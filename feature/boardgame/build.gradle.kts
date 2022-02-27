plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

apply(rootProject.file("./gradle/jacoco.gradle"))

android {
    compileSdk = 31

    defaultConfig {
        minSdk = 23
        targetSdk = 31

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        debug {
            isTestCoverageEnabled = true
        }
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    packagingOptions {
        resources {
            excludes += setOf("META-INF/LGPL2.1", "META-INF/AL2.0")
        }
    }
    buildFeatures {
        compose = true
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.1.1"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    sourceSets {
        getByName("androidTest").assets.srcDirs("src/androidTest/assets")
    }
}

dependencies {
    implementation(project(":common"))
    implementation(project(":shared:architecture"))
    implementation(project(":shared:bgg_api_client"))

    //region Core & Lifecycle

    implementation(AndroidX.core.ktx)
    implementation(AndroidX.appCompat)
    implementation(AndroidX.fragment.ktx)

    implementation(AndroidX.lifecycle.runtimeKtx)
    implementation(AndroidX.lifecycle.viewModelKtx)

    //endregion

    //region Dependency Injection

    implementation(Google.dagger.hilt.android)
    kapt(Google.dagger.hilt.compiler)
    implementation(AndroidX.hilt.work)
    kapt(AndroidX.hilt.compiler)

    //endregion

    //region UI

    implementation(AndroidX.compose.ui)
    implementation(AndroidX.compose.ui.tooling)
    implementation(AndroidX.compose.material)
    implementation(AndroidX.compose.foundation)

    //endregion

    //region UI

    implementation(COIL.compose)

    //endregion

    //region Service & Worker

    implementation(AndroidX.work.runtime)
    implementation(AndroidX.work.runtimeKtx)

    //endregion

    //region Database

    implementation(AndroidX.room.runtime)

    //endregion

    //region Test

    testImplementation(Testing.junit4)
    testImplementation(AndroidX.test.coreKtx)

    //endregion

    //region AndroidTest

    androidTestImplementation(project(":shared:tests_tools"))
    androidTestImplementation(AndroidX.test.ext.junit)
    androidTestImplementation(AndroidX.test.espresso.core)
    androidTestImplementation(Testing.mockK.android)
    androidTestImplementation(AndroidX.work.testing)

    // -- Compose Tests
    androidTestImplementation(AndroidX.compose.ui.testJunit4)
    debugImplementation(AndroidX.compose.ui.testManifest)

    //endregion
}
