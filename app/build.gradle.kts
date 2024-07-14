import java.util.Properties

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("io.github.reactivecircus.app-versioning") version "1.2.0"
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("com.google.firebase.firebase-perf")
}

android {
    compileSdk = 34

    defaultConfig {
        applicationId = "fr.boitakub.bogadex"
        minSdk = 23
        targetSdk = 33

        testApplicationId = "fr.boitakub.bogadex.tests"
        testInstrumentationRunner = "fr.boitakub.bogadex.tests.InstrumentHiltTestRunner"
        kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas")
            }
        }
    }
    signingConfigs {
        create("release") {
            val props = Properties()
            val propFile = file(System.getProperty("user.home") + "/CloudStation/Informatique/Keys/bogadex.properties")
            if (propFile.exists()) {
                props.load(propFile.inputStream())
            }

            if (!props.isEmpty) {
                storeFile = file(System.getProperty("user.home") + props.getProperty("keystore"))
                storePassword = props.getProperty("keystore.password")
                keyAlias = props.getProperty("keyAlias")
                keyPassword = props.getProperty("keyPassword")
            } else {
                storeFile = file(System.getenv("RUNNER_TEMP") + "/keystore/keystore_file.jks")
                storePassword = System.getenv("SIGNING_STORE_PASSWORD")
                keyAlias = System.getenv("SIGNING_KEY_ALIAS")
                keyPassword = System.getenv("SIGNING_KEY_PASSWORD")
            }
        }
    }
    buildTypes {
        getByName("debug") {
            isDebuggable = true
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
            configure<com.google.firebase.perf.plugin.FirebasePerfExtension> {
                // Set this flag to 'false' to disable @AddTrace annotation processing and
                // automatic monitoring of HTTP/S network requests
                // for a specific build variant at compile time.
                setInstrumentationEnabled(false)
            }
            configure<com.google.firebase.crashlytics.buildtools.gradle.CrashlyticsExtension> {
                mappingFileUploadEnabled = false
            }
        }
        getByName("release") {
            isDebuggable = false
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
    packagingOptions {
        resources.excludes.add("META-INF/*")
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
    sourceSets {
        getByName("androidTest").assets.srcDirs("src/androidTest/assets")
    }
    lint {
        xmlReport = true
        htmlReport = true
        htmlOutput = file("${project.rootDir}/build/reports/android-lint.html")
    }
    testOptions {
        packagingOptions {
            jniLibs {
                useLegacyPackaging = true
            }
        }
    }
    namespace = "fr.boitakub.bogadex"
}

dependencies {
    val androidCoreVersion: String by project
    val lifecycleVersion: String by project
    val composeBomVersion: String by project
    val coilVersion: String by project
    val navigationVersion: String by project
    val daggerVersion: String by project
    val hiltVersion: String by project
    val okhttpVersion: String by project
    val retrofitVersion: String by project
    val dataStoreVersion: String by project
    val roomVersion: String by project
    val workVersion: String by project
    val firebaseVersion: String by project
    val junitVersion: String by project
    val testCoreVersion: String by project
    val espressoVersion: String by project
    val mockkVersion: String by project

    implementation(project(":common"))
    implementation(project(":shared:architecture"))
    implementation(project(":shared:bgg_api_client"))
    implementation(project(":feature:boardgame"))
    implementation(project(":feature:boardgame_list"))
    implementation(project(":feature:preferences"))

    //region Core & Lifecycle

    implementation("androidx.core:core-ktx:$androidCoreVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:$lifecycleVersion")

    //endregion

    //region Dependency Injection

    implementation("com.google.dagger:hilt-android:$daggerVersion")
    kapt("com.google.dagger:hilt-compiler:$daggerVersion")
    implementation("androidx.hilt:hilt-work:$hiltVersion")
    kapt("androidx.hilt:hilt-compiler:$hiltVersion")

    //endregion

    //region UI

    implementation("androidx.navigation:navigation-compose:$navigationVersion")
    implementation("io.coil-kt:coil-compose:$coilVersion")

    // UI - Tooling
    val composeBom = platform("androidx.compose:compose-bom:$composeBomVersion")
    implementation(composeBom)
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    //endregion

    //region Networking

    implementation("com.squareup.okhttp3:okhttp:$okhttpVersion")
    implementation("com.squareup.okhttp3:logging-interceptor:$okhttpVersion")
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")

    //endregion

    //region Service & Worker

    implementation("androidx.work:work-runtime:$workVersion")
    implementation("androidx.work:work-runtime-ktx:$workVersion")

    //endregion

    // DataStore (SharedPreferences)

    implementation("androidx.datastore:datastore-preferences:$dataStoreVersion")

    //endregion

    //region Database

    implementation("androidx.room:room-runtime:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")

    //endregion

    //region Monitoring

    implementation(platform("com.google.firebase:firebase-bom:$firebaseVersion"))
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-perf-ktx")

    //endregion

    //region Test

    testImplementation("junit:junit:$junitVersion")
    testImplementation("io.mockk:mockk-android:$mockkVersion")
    testImplementation("io.mockk:mockk-agent:$mockkVersion")
    testImplementation("io.coil-kt:coil-test:$coilVersion")

    //endregion

    //region AndroidTest

    androidTestImplementation(project(":shared:tests_tools"))

    androidTestImplementation("androidx.test:core-ktx:$testCoreVersion")
    androidTestImplementation("androidx.test.espresso:espresso-core:$espressoVersion")
    androidTestImplementation("io.mockk:mockk-android:$mockkVersion")
    androidTestImplementation("io.mockk:mockk-agent:$mockkVersion")
    androidTestImplementation("com.google.dagger:hilt-android-testing:$daggerVersion")
    kaptAndroidTest("com.google.dagger:hilt-compiler:$daggerVersion")
    androidTestImplementation("androidx.work:work-testing:$workVersion")
    androidTestImplementation("io.coil-kt:coil-test:$coilVersion")
    androidTestImplementation("com.squareup.okhttp3:mockwebserver:$okhttpVersion")

    //endregion
}
