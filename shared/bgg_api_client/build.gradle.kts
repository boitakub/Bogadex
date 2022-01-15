plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
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
}

dependencies {

    //region Parsing
    api(libs.tikxml.annotation) {
        exclude(group = "com.squareup.okio")
    }
    implementation(libs.tikxml.core) {
        exclude(group = "com.squareup.okio")
    }
    implementation(libs.tikxml.retrofitConverter) {
        exclude(group = "com.squareup.okhttp3")
        exclude(group = "com.squareup.retrofit2")
    }
    implementation(libs.tikxml.htmlescape)
    kapt(libs.tikxml.processor)
    //endregion

    //region Networking

    implementation(libs.okhttp.core)
    implementation(libs.okhttp.loggingInterceptor)
    implementation(libs.retrofit.core)

    //endregion

    //region Test

    testImplementation(libs.testing.junit)

    //endregion

    //region AndroidTest

    androidTestImplementation(libs.testing.androidx.junit)
    androidTestImplementation(libs.testing.espresso.core)

    //endregion
}
