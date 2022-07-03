plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

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
            // FIXME: isTestCoverageEnabled not compatible with espresso
            // isTestCoverageEnabled = true
        }
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
    namespace = "fr.boitakub.bogadex.common"
}

dependencies {
    implementation(project(":shared:architecture"))

    //region UI

    implementation(Google.android.material)
    implementation(AndroidX.compose.ui)
    implementation(AndroidX.compose.ui.tooling)
    implementation(AndroidX.compose.foundation)
    implementation(AndroidX.preference.ktx)
    implementation(AndroidX.recyclerView)

    //endregion

    //region Dependency Injection

    implementation(Google.dagger.hilt.android)
    kapt(Google.dagger.hilt.compiler)
    implementation(AndroidX.hilt.work)
    kapt(AndroidX.hilt.compiler)

    //endregion

    //region Test

    testImplementation(Testing.junit4)

    //endregion

    //region AndroidTest

    androidTestImplementation(AndroidX.test.ext.junit)
    androidTestImplementation(AndroidX.test.espresso.core)

    //endregion
}
