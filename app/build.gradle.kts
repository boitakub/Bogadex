plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("io.github.reactivecircus.app-versioning") version "1.0.0"
}

android {
    compileSdk = 30

    defaultConfig {
        applicationId = "fr.boitakub.bogadex"
        minSdk = 23
        targetSdk = 30

        testApplicationId = "fr.boitakub.bogadex.tests"
        testInstrumentationRunner = "fr.boitakub.bogadex.tests.InstrumentHiltTestRunner"
    }

    buildTypes {
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
    sourceSets {
        getByName("androidTest").assets.srcDirs("src/androidTest/assets")
    }
}

dependencies {
    implementation(project(":common"))
    implementation(project(":shared:clean_architecture"))
    implementation(project(":shared:bgg_api_client"))
    implementation(project(":feature:boardgame"))
    implementation(project(":feature:boardgame_list"))

    implementation("androidx.core:core-ktx:1.6.0")
    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("com.google.android.material:material:1.4.0")

    implementation("com.google.dagger:hilt-android:2.38.1")
    kapt("com.google.dagger:hilt-android-compiler:2.38.1")
    implementation("androidx.hilt:hilt-work:1.0.0")
    kapt("androidx.hilt:hilt-compiler:1.0.0")

    implementation("androidx.work:work-runtime:2.6.0")
    implementation("androidx.work:work-runtime-ktx:2.6.0")

    implementation("androidx.navigation:navigation-fragment-ktx:2.3.5")
    implementation("androidx.navigation:navigation-ui-ktx:2.3.5")

    implementation("com.squareup.okhttp3:okhttp:4.9.1")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    implementation("androidx.room:room-runtime:2.3.0")
    kapt("androidx.room:room-compiler:2.3.0")
    implementation("androidx.room:room-ktx:2.3.0")

    // DI in AndroidTest
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.38.1")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.38.1")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test:core-ktx:1.4.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("com.adevinta.android:barista:4.1.0") {
        exclude("org.jetbrains.kotlin")
    }
    androidTestImplementation("androidx.work:work-testing:2.6.0")

    // Mock
    androidTestImplementation("com.squareup.okhttp3:mockwebserver:4.9.1")
}
