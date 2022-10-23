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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
    namespace = "fr.boitakub.boardgame_list"
}

dependencies {
    val androidCoreVersion: String by project
    val appcompatVersion: String by project
    val fragmentVersion: String by project
    val recyclerviewVersion: String by project
    val navigationVersion: String by project
    val lifecycleVersion: String by project
    val daggerVersion: String by project
    val hiltVersion: String by project
    val coilVersion: String by project
    val materialVersion: String by project
    val composeVersion: String by project
    val roomVersion: String by project
    val workVersion: String by project
    val junitVersion: String by project
    val espressoVersion: String by project

    implementation(project(":common"))
    implementation(project(":shared:architecture"))
    implementation(project(":shared:bgg_api_client"))
    implementation(project(":feature:boardgame"))

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
    implementation("com.google.android.material:material:1.7.0")
    implementation("androidx.fragment:fragment-ktx:$fragmentVersion")
    implementation("androidx.navigation:navigation-fragment-ktx:$navigationVersion")

    implementation("io.coil-kt:coil:$coilVersion")
    implementation("androidx.recyclerview:recyclerview:$recyclerviewVersion")
    implementation("io.github.florent37:shapeofview:1.4.7")

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

    androidTestImplementation("androidx.test.espresso:espresso-core:$espressoVersion")

    //endregion
}
