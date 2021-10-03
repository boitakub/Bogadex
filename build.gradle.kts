import io.gitlab.arturbosch.detekt.detekt

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
        classpath(libs.detektPlugin)
        classpath(libs.ktlintPlugin)
        classpath(libs.jacocoPlugin)

        // -- Application plugins
        classpath(libs.hiltAndroidPlugin)
    }
}

apply(plugin = "org.jlleitschuh.gradle.ktlint")

subprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    // Optionally configure plugin
    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        debug.set(true)
    }

    detekt {
        config = files("$rootDir/detekt.yml")

        parallel = true

        // By default detekt does not check test source set and gradle specific files, so hey have to be added manually
        input = files(
            "$rootDir/build.gradle.kts",
            "$rootDir/settings.gradle.kts",
            "src/build.gradle.kts",
            "src/main",
        )
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

val installGitHook = tasks.register("installGitHook", Copy::class) {
    from("${rootProject.rootDir}/pre-commit")
    into("${rootProject.rootDir}/.git/hooks")
    fileMode = 777
}
tasks.getByPath(":app:preBuild").dependsOn(installGitHook)

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
