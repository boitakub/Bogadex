pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
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
include(":feature:preferences")
