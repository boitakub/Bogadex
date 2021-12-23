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
        val libs = project.extensions.getByType<VersionCatalogsExtension>()
            .named("libs") as org.gradle.accessors.dm.LibrariesForLibs

        // -- Core plugins
        classpath(libs.android.gradlePlugin)
        classpath(libs.kotlin.gradlePlugin)

        // -- Utility plugins
        classpath(libs.jacocoPlugin)

        // -- Application plugins
        classpath(libs.hiltAndroidPlugin)
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
                    "org.jacoco" -> useVersion(libs.versions.jacoco.get())
                }
            }
        }
    }
}
