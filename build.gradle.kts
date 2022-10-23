plugins {
    id("io.gitlab.arturbosch.detekt") version "1.19.0"
    id("com.diffplug.spotless") version "6.3.0"
    id("com.autonomousapps.dependency-analysis") version "1.13.1"
    id("nl.neotech.plugin.rootcoverage") version "1.6.0"
}

// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    val kotlinVersion: String by project
    val daggerVersion: String by project
    repositories {
        google()
        mavenCentral()
        maven(url = "https://plugins.gradle.org/m2/")
    }
    dependencies {
        // -- Core plugins
        classpath("com.android.tools.build:gradle:7.3.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")

        // -- Application plugins
        classpath("com.google.dagger:hilt-android-gradle-plugin:$daggerVersion")

        // -- Services & Monitoring
        classpath("com.google.gms:google-services:4.3.14")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.2")
        classpath("com.google.firebase:perf-plugin:1.4.2")
    }
}

allprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")
    apply(plugin = "com.diffplug.spotless")

    detekt {
        config = files("$rootDir/detekt.yml")
        parallel = true
        buildUponDefaultConfig = true

        // By default detekt does not check test source set and gradle specific files, so hey have to be added manually
        input = files(
            "$rootDir/build.gradle.kts",
            "$rootDir/settings.gradle.kts",
            "src/build.gradle.kts",
            "src/main",
        )
    }

    spotless {
        ratchetFrom = "origin/main"

        format("misc") {
            target("*.gradle", "*.md", ".gitignore")

            trimTrailingWhitespace()
            indentWithTabs()
            endWithNewline()
        }
        kotlin {
            target("**/*.kt")
            ktlint()
            licenseHeaderFile(file("${project.rootDir}/copyright.txt"))
        }
        kotlinGradle {
            target("*.gradle.kts")
            ktlint()
        }
    }
}
