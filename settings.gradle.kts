pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
        maven { url = java.net.URI("https://devrepo.kakao.com/nexus/content/groups/public/") }
    }
}

//enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

//includeBuild("build-logic")

rootProject.name = "DocuSnap"
include(":app")
include(":data")
include(":domain")
include(":presentation")
