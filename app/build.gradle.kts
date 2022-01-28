import java.util.Properties

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("io.github.reactivecircus.app-versioning")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("com.google.firebase.firebase-perf")
}

apply(rootProject.file("./gradle/jacoco.gradle"))

android {
    compileSdk = 31

    defaultConfig {
        applicationId = "fr.boitakub.bogadex"
        minSdk = 23
        targetSdk = 31

        testApplicationId = "fr.boitakub.bogadex.tests"
        testInstrumentationRunner = "fr.boitakub.bogadex.tests.InstrumentHiltTestRunner"
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
            isTestCoverageEnabled = true
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
    buildFeatures {
        compose = true
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.0.5"
    }
    sourceSets {
        getByName("androidTest").assets.srcDirs("src/androidTest/assets")
    }
    lint {
        xmlReport = true
        htmlReport = true
        htmlOutput = file("${project.rootDir}/build/reports/android-lint.html")
    }
}

dependencies {
    implementation(project(":common"))
    implementation(project(":shared:architecture"))
    implementation(project(":shared:bgg_api_client"))
    implementation(project(":feature:boardgame"))
    implementation(project(":feature:boardgame_list"))

    //region Core & Lifecycle

    implementation(AndroidX.core.ktx)
    implementation(AndroidX.appCompat)

    //endregion

    //region Dependency Injection

    implementation(Google.dagger.hilt.android)
    kapt(Google.dagger.hilt.compiler)
    implementation(AndroidX.hilt.work)
    kapt(AndroidX.hilt.compiler)

    //endregion

    //region UI

    implementation(Google.android.material)
    implementation(AndroidX.navigation.uiKtx)
    implementation(AndroidX.navigation.fragmentKtx)
    implementation("androidx.compose.runtime:runtime:1.0.0")

    //endregion

    //region Networking

    implementation(Square.okHttp3)
    implementation(Square.okHttp3.loggingInterceptor)
    implementation(Square.retrofit2)
    implementation(COIL)

    //endregion

    //region Service & Worker

    implementation(AndroidX.work.runtime)
    implementation(AndroidX.work.runtimeKtx)

    //endregion

    //region Database

    implementation(AndroidX.room.runtime)
    kapt(AndroidX.room.compiler)
    implementation(AndroidX.room.ktx)

    //endregion

    //region Monitoring

    implementation(platform(Firebase.bom))
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-perf-ktx")

    //endregion

    //region Test

    testImplementation(Testing.junit4)

    //endregion

    //region AndroidTest

    androidTestImplementation(project(":shared:tests_tools"))
    androidTestImplementation(AndroidX.test.ext.junit)
    androidTestImplementation(AndroidX.test.espresso.core)
    androidTestImplementation("androidx.test.espresso:espresso-contrib:_") {
        exclude(module = "protobuf-lite")
    }
    androidTestImplementation(AndroidX.test.coreKtx)
    androidTestImplementation(Google.dagger.hilt.android.testing)
    kaptAndroidTest(Google.dagger.hilt.compiler)
    androidTestImplementation(AndroidX.work.testing)
    androidTestImplementation(Square.okHttp3.mockWebServer)

    //endregion
}
