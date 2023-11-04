@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        mavenCentral()
        mavenLocal()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositories {
        mavenCentral()
        mavenLocal()
        maven("https://repo.opencollab.dev/maven-releases")
        maven("https://repo.opencollab.dev/maven-snapshots")
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "protocol"

include("bedrock-codec", "bedrock-connection", "common")