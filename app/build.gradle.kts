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
        debug {
            isDebuggable = true
            isTestCoverageEnabled = true
            configure<com.google.firebase.perf.plugin.FirebasePerfExtension> {
                // Set this flag to 'false' to disable @AddTrace annotation processing and
                // automatic monitoring of HTTP/S network requests
                // for a specific build variant at compile time.
                setInstrumentationEnabled(false)
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
    lint {
        isCheckDependencies = true
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)

    //endregion

    //region Dependency Injection

    implementation(libs.hilt.android)
    kapt(libs.hilt.androidCompiler)
    implementation(libs.hilt.work)
    kapt(libs.hilt.compiler)

    //endregion

    //region UI

    implementation(libs.material)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)

    //endregion

    //region Networking

    implementation(libs.okhttp.core)
    implementation(libs.okhttp.loggingInterceptor)
    implementation(libs.retrofit.core)

    //endregion

    //region Service & Worker

    implementation(libs.work.runtime)
    implementation(libs.work.ktx)

    //endregion

    //region Database

    implementation(libs.room.runtime)
    kapt(libs.room.compiler)
    implementation(libs.room.ktx)

    //endregion

    //region Test

    testImplementation(libs.testing.junit)

    //endregion

    //region Monitoring

    implementation(platform("com.google.firebase:firebase-bom:29.0.3"))
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-perf-ktx")

    //endregion

    //region AndroidTest

    androidTestImplementation(project(":shared:tests_tools"))
    androidTestImplementation(libs.testing.androidx.junit)
    androidTestImplementation(libs.testing.espresso.core)
    androidTestImplementation(libs.testing.espresso.contrib) {
        exclude(module = "protobuf-lite")
    }
    androidTestImplementation(libs.testing.core.ktx)
    androidTestImplementation(libs.testing.hilt.android)
    kaptAndroidTest(libs.hilt.androidCompiler)
    androidTestImplementation(libs.testing.work)
    androidTestImplementation(libs.testing.okhttp.mockwebserver)

    //endregion
}
