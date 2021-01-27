pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }

    plugins {
        kotlin("multiplatform") version "1.4.21"
        kotlin("plugin.serialization") version "1.4.21"
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        jcenter()
    }
}

rootProject.name = "rsocket-kotlin-multiplatform-example"
