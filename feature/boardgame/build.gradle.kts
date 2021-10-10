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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.get()
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)

    implementation(libs.androidx.fragment.ktx)

    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.viewModel)
    implementation(libs.androidx.lifecycle.liveData)

    //endregion

    //region Dependency Injection

    implementation(libs.hilt.android)
    kapt(libs.hilt.androidCompiler)
    implementation(libs.hilt.work)
    kapt(libs.hilt.compiler)

    //endregion

    //region UI

    implementation(libs.androidx.composeUi)
    implementation(libs.androidx.composeUiTooling)
    implementation(libs.androidx.composeMaterial)
    implementation(libs.androidx.foundation)

    //endregion

    //region UI

    implementation(libs.coil.compose)

    //endregion

    //region Service & Worker

    implementation(libs.work.runtime)
    implementation(libs.work.ktx)

    //endregion

    //region Database

    implementation(libs.room.runtime)

    //endregion

    //region Test

    testImplementation(libs.testing.junit)
    testImplementation(libs.testing.core.ktx)

    //endregion

    //region AndroidTest

    androidTestImplementation(libs.testing.androidx.junit)
    androidTestImplementation(libs.testing.espresso.core)
    androidTestImplementation(libs.testing.mockk.android)
    androidTestImplementation(libs.testing.work)

    //endregion
}
