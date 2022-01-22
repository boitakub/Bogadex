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
}

dependencies {

    //region Parsing
    api("com.tickaroo.tikxml:annotation:_") {
        exclude(group = "com.squareup.okio")
    }
    implementation("com.tickaroo.tikxml:core:_") {
        exclude(group = "com.squareup.okio")
    }
    implementation("com.tickaroo.tikxml:retrofit-converter:_") {
        exclude(group = "com.squareup.okhttp3")
        exclude(group = "com.squareup.retrofit2")
    }
    implementation("com.tickaroo.tikxml:converter-htmlescape:_")
    kapt("com.tickaroo.tikxml:processor:_")
    //endregion

    //region Networking

    implementation(Square.okHttp3)
    implementation(Square.okHttp3.loggingInterceptor)
    implementation(Square.retrofit2)

    //endregion

    //region Test

    testImplementation(Testing.junit4)

    //endregion

    //region AndroidTest

    androidTestImplementation(AndroidX.test.ext.junit)
    androidTestImplementation(AndroidX.test.espresso.core)

    //endregion
}
