import java.util.Properties

plugins {
    kotlin("android")
    alias(libs.plugins.android.application)
    alias(libs.plugins.ksp)
    id("com.google.firebase.crashlytics")
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kover)
    alias(libs.plugins.google.services)
    alias(libs.plugins.appversionning)
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

android {
    compileSdk = AndroidConfig.COMPILE_SDK

    defaultConfig {
        minSdk = AndroidConfig.MIN_SDK
        targetSdk = AndroidConfig.TARGET_SDK
        applicationId = "fr.boitakub.bogadex"

        testApplicationId = "fr.boitakub.bogadex.tests"
        testInstrumentationRunner = "fr.boitakub.bogadex.tests.InstrumentationTestRunner"
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
            configure<com.google.firebase.crashlytics.buildtools.gradle.CrashlyticsExtension> {
                mappingFileUploadEnabled = false
            }
        }
        getByName("release") {
            isDebuggable = false
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            signingConfig = signingConfigs.getByName("release")
        }
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
    packagingOptions {
        resources {
            excludes += "META-INF/LICENSE.md"
            excludes += "META-INF/LICENSE-notice.md" // si besoin
        }
    }
    sourceSets {
        getByName("androidTest").assets.srcDirs("src/androidTest/assets")
    }
    lint {
        xmlReport = true
        htmlReport = true
        htmlOutput = file("${project.rootDir}/build/reports/android-lint.html")
    }
    namespace = "fr.boitakub.bogadex"
}

dependencies {

    //region - Merge coverage reports
    kover(project(":common"))
    kover(project(":shared:architecture"))
    kover(project(":shared:bgg_api_client"))
    kover(project(":feature:boardgame"))
    kover(project(":feature:boardgame_list"))
    kover(project(":feature:preferences"))
    //endregion

    implementation(project(":common"))
    implementation(project(":shared:architecture"))
    implementation(project(":shared:bgg_api_client"))
    implementation(project(":feature:boardgame"))
    implementation(project(":feature:boardgame_list"))
    implementation(project(":feature:preferences"))

    implementation(libs.datastore)
    implementation(libs.okhttp)
    implementation(libs.retrofit)

    //region Dependency Injection

    val koinBom = platform(libs.koin.bom)
    implementation(koinBom)
    implementation(libs.koin.android)
    implementation(libs.koin.android.workmanager)

    //endregion

    //region UI

    val composeBom = platform(libs.compose.bom)
    implementation(composeBom)
    implementation(libs.compose.material3)
    implementation(libs.compose.ui.tooling.preview)
    debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.compose.ui.test.manifest)
    implementation(libs.material)

    implementation(libs.navigation.compose)
    implementation(libs.coil)
    implementation(libs.coil.network)

    //endregion

    //region Networking

    implementation(libs.okhttp)
    implementation(libs.retrofit)

    //endregion

    //region Service & Worker

    implementation(libs.work.runtime)

    //endregion

    //region Database

    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    //endregion

    //region Monitoring

    val firebaseBom = platform(libs.firebase.bom)
    implementation(firebaseBom)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)

    //endregion

    //region Test

    testImplementation(libs.test.junit)
    testImplementation(libs.test.mockk)

    //endregion

    //region AndroidTest

    androidTestImplementation(libs.test.espresso)
    androidTestImplementation(composeBom)
    androidTestImplementation(libs.test.compose.ui)
    androidTestImplementation(libs.test.mockk.android)
    androidTestImplementation(libs.test.mockwebserver)
    androidTestImplementation(libs.test.koin)
    androidTestImplementation(libs.test.koin.android)
    androidTestImplementation(libs.test.coil)

    //endregion
}
