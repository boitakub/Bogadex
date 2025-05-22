plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.google.services) apply false
    id("com.google.firebase.crashlytics") version "3.0.3" apply false
    alias(libs.plugins.compose.compiler) apply false
    id("com.diffplug.spotless") version "7.0.1"
    id("org.sonarqube") version "6.2.0.5505"
}

sonarqube {
    properties {
        property("sonar.projectKey", "boitakub_Bogadex")
        property("sonar.organization", "boitakub")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}

allprojects {
    apply(plugin = "com.diffplug.spotless")

    spotless {
        ratchetFrom = "origin/main"

        format("misc") {
            target("*.gradle", ".gitignore")

            trimTrailingWhitespace()
            leadingSpacesToTabs()
            endWithNewline()
        }
        kotlin {
            target("**/*.kt")
            targetExclude("**/AndroidConfig.kt")
            ktlint().editorConfigOverride(
                mapOf(
                    "ktlint_function_naming_ignore_when_annotated_with" to "Composable",
                ),
            )
            licenseHeaderFile(file("${project.rootDir}/copyright.txt"))
        }
        kotlinGradle {
            target("*.gradle.kts")
            ktlint()
        }
    }
}
