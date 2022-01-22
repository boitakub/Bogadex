plugins {
    id("io.gitlab.arturbosch.detekt")
    id("com.diffplug.spotless")
}

// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://plugins.gradle.org/m2/")
    }
    dependencies {
        // -- Core plugins
        classpath(Android.tools.build.gradlePlugin)
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:_")

        // -- Utility plugins
        classpath("org.jacoco:org.jacoco.core:_")

        // -- Application plugins
        classpath(Google.dagger.hilt.android.gradlePlugin)

        // -- Services & Monitoring
        classpath(Google.playServicesGradlePlugin)
        classpath(Firebase.crashlyticsGradlePlugin)
        classpath(Firebase.performanceMonitoringGradlePlugin)
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

subprojects {
    configurations.all {
        resolutionStrategy {
            eachDependency {
                when (requested.group) {
                    "org.jacoco" -> useVersion("0.8.7")
                }
            }
        }
    }
}
