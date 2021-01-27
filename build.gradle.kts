import org.jetbrains.kotlin.konan.target.*

buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.21")
        classpath("org.jetbrains.kotlinx:atomicfu-gradle-plugin:0.14.4")
    }
}

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

apply(plugin = "kotlinx-atomicfu")

kotlin {
    jvm("serverJvm")
    jvm("clientJvm")
    js("clientJs", IR) {
        browser {
            binaries.executable()
        }
        nodejs {
            binaries.executable()
        }
    }
    when {
        HostManager.hostIsLinux -> linuxX64("clientNative")
        HostManager.hostIsMingw -> null //no native support for TCP mingwX64("clientNative")
        HostManager.hostIsMac   -> macosX64("clientNative")
        else                    -> null
    }?.binaries {
        executable {
            entryPoint = "main"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("io.rsocket.kotlin:rsocket-core:0.12.0")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-protobuf:1.0.1")
            }
        }

        val clientMain by creating {
            dependsOn(commonMain)
            dependencies {
                implementation("io.rsocket.kotlin:rsocket-transport-ktor-client:0.12.0")
            }
        }

        val serverJvmMain by getting {
            dependencies {
                implementation("io.rsocket.kotlin:rsocket-transport-ktor-server:0.12.0")
                implementation("io.ktor:ktor-server-cio:1.5.0")
            }
        }

        val clientJvmMain by getting {
            dependsOn(clientMain)
            dependencies {
                implementation("io.ktor:ktor-client-cio:1.5.0")
            }
        }

        val clientJsMain by getting {
            dependsOn(clientMain)
            dependencies {
                implementation("io.ktor:ktor-client-js:1.5.0")
            }
        }

        if (!HostManager.hostIsMingw) {
            val clientNativeMain by getting {
                dependsOn(clientMain)
            }
        }
    }
}
