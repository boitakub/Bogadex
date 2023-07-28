plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

android {
    compileSdk = 33

    defaultConfig {
        minSdk = 23
        targetSdk = 31

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
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    namespace = "fr.boitakub.api.client"
}

dependencies {
    val okhttpVersion: String by project
    val retrofitVersion: String by project
    val junitVersion: String by project
    val espressoVersion: String by project

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

    //region Networking

    implementation("com.squareup.okhttp3:okhttp:$okhttpVersion")
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")

    //endregion

    //region Test

    testImplementation("junit:junit:$junitVersion")

    //endregion

    //region AndroidTest

    androidTestImplementation("androidx.test.espresso:espresso-core:$espressoVersion")

    //endregion
}
