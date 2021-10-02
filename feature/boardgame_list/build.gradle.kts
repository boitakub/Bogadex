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
    }
}

dependencies {
    implementation(project(":common"))
    implementation(project(":shared:clean_architecture"))
    implementation(project(":shared:bgg_api_client"))
    implementation(project(":feature:boardgame"))

    //region Core & Lifecycle

    implementation(libs.androidx.fragment.ktx)

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

    implementation(libs.material)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.coil.core)

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

    //endregion

    //region AndroidTest

    androidTestImplementation(libs.testing.androidx.junit)
    androidTestImplementation(libs.testing.espresso.core)

    //endregion
}
