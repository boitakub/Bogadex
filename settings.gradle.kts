plugins {
    // See https://jmfayard.github.io/refreshVersions
    id("de.fayard.refreshVersions") version "0.30.1"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "Bogadex"
include(":app")
include(":feature:boardgame")
include(":feature:boardgame_list")
include(":common")
include(":shared:architecture")
include(":shared:bgg_api_client")
include(":shared:tests_tools")

enableFeaturePreview("VERSION_CATALOGS") // Gradle 7 new dependency management tool
