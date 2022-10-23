plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = 32

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
        kotlinCompilerExtensionVersion = "1.3.2"
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
    namespace = "fr.boitakub.bogadex.boardgame"
}

dependencies {
    val androidCoreVersion: String by project
    val appcompatVersion: String by project
    val fragmentVersion: String by project
    val coilVersion: String by project
    val lifecycleVersion: String by project
    val daggerVersion: String by project
    val hiltVersion: String by project
    val materialVersion: String by project
    val composeVersion: String by project
    val roomVersion: String by project
    val workVersion: String by project
    val junitVersion: String by project
    val junitExtVersion: String by project
    val testCoreVersion: String by project
    val espressoVersion: String by project

    implementation(project(":common"))
    implementation(project(":shared:architecture"))
    implementation(project(":shared:bgg_api_client"))

    //region Core & Lifecycle

    implementation("androidx.core:core-ktx:$androidCoreVersion")
    implementation("androidx.appcompat:appcompat:$appcompatVersion")
    implementation("androidx.fragment:fragment-ktx:$fragmentVersion")

    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")

    //endregion

    //region Dependency Injection

    implementation("com.google.dagger:hilt-android:$daggerVersion")
    kapt("com.google.dagger:hilt-compiler:$daggerVersion")
    implementation("androidx.hilt:hilt-work:$hiltVersion")
    kapt("androidx.hilt:hilt-compiler:$hiltVersion")

    //endregion

    //region UI
    implementation("androidx.compose.material:material:$materialVersion")

    //region Compose

    implementation("androidx.compose.foundation:foundation:$composeVersion")
    implementation("androidx.compose.ui:ui:$composeVersion")

    // Android Studio Preview support
    implementation("androidx.compose.ui:ui-tooling-preview:$composeVersion")
    debugImplementation("androidx.compose.ui:ui-tooling:$composeVersion")

    //endregion

    //region UI

    implementation("io.coil-kt:coil-compose:$coilVersion")

    //endregion

    //region Service & Worker

    implementation("androidx.work:work-runtime:$workVersion")
    implementation("androidx.work:work-runtime-ktx:$workVersion")

    //endregion

    //region Database

    implementation("androidx.room:room-runtime:$roomVersion")

    //endregion

    //region Test

    testImplementation("junit:junit:$junitVersion")

    //endregion

    //region AndroidTest

    androidTestImplementation(project(":shared:tests_tools"))

    androidTestImplementation("androidx.test:core-ktx:$testCoreVersion")
    androidTestImplementation("androidx.test.espresso:espresso-core:$espressoVersion")
    androidTestImplementation("androidx.test.ext:junit:$junitExtVersion")

    androidTestImplementation("androidx.compose.ui:ui-test-junit4:$composeVersion")
    debugImplementation("androidx.compose.ui:ui-test-manifest:$composeVersion")

    androidTestImplementation("io.mockk:mockk-android:1.12.3")
    androidTestImplementation("androidx.work:work-testing:$workVersion")

    //endregion
}
