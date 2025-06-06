plugins {
    kotlin("android")
    alias(libs.plugins.android.library)
    alias(libs.plugins.ksp)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kover)
}

android {
    compileSdk = AndroidConfig.COMPILE_SDK

    defaultConfig {
        minSdk = AndroidConfig.MIN_SDK
        targetSdk = AndroidConfig.TARGET_SDK

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        debug {
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
        }
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    buildFeatures {
        compose = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
    packagingOptions {
        resources {
            excludes += "META-INF/LICENSE.md"
            excludes += "META-INF/LICENSE-notice.md" // si besoin
        }
    }
    namespace = "fr.boitakub.boardgame_list"
}

dependencies {
    implementation(project(":common"))
    implementation(project(":shared:architecture"))
    implementation(project(":shared:bgg_api_client"))
    implementation(project(":feature:boardgame"))

    //region Core

    implementation(libs.android.core)

    //endregion

    //region Dependency Injection

    val koinBom = platform(libs.koin.bom)
    implementation(koinBom)
    implementation(libs.koin.android)
    implementation(libs.koin.android.compose)

    //endregion

    //region UI

    val composeBom = platform(libs.compose.bom)
    implementation(composeBom)
    implementation(libs.compose.material3)
    implementation(libs.compose.foundation)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    debugImplementation(libs.compose.ui.tooling)

    // UI third-party
    implementation(libs.navigation.compose)
    implementation(libs.coil)

    //endregion

    //region Service & Worker

    implementation(libs.work.runtime)

    //endregion

    //region Database

    implementation(libs.room.runtime)

    //endregion

    //region Test

    testImplementation(libs.test.junit)

    //endregion

    //region AndroidTest

    androidTestImplementation(libs.test.espresso)
    androidTestImplementation(libs.test.android)
    androidTestImplementation("io.github.pdvrieze.xmlutil:core:0.91.0")
    androidTestImplementation("io.github.pdvrieze.xmlutil:serialization:0.91.0")
    androidTestImplementation(libs.test.work)
    androidTestImplementation(libs.okhttp)
    androidTestImplementation(libs.test.mockk.android)

    //endregion
}
