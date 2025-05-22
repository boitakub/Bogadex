plugins {
    kotlin("android")
    alias(libs.plugins.android.library)
    id("kotlin-kapt")
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
    namespace = "fr.boitakub.api.client"
}

dependencies {
    implementation(libs.okhttp)
    implementation(libs.retrofit)

    //region Parsing
    api("com.tickaroo.tikxml:annotation:0.8.13") {
        exclude(group = "com.squareup.okio")
    }
    implementation("com.tickaroo.tikxml:core:0.8.13") {
        exclude(group = "com.squareup.okio")
    }
    implementation("com.tickaroo.tikxml:retrofit-converter:0.8.13") {
        exclude(group = "com.squareup.okhttp3")
        exclude(group = "com.squareup.retrofit2")
    }
    implementation("com.tickaroo.tikxml:converter-htmlescape:0.8.13")
    kapt("com.tickaroo.tikxml:processor:0.8.13")
    //endregion

    //region Tests

    testImplementation(libs.test.junit)
    androidTestImplementation(libs.test.espresso)

    //endregion
}
