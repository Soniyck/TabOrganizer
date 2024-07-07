plugins {
    // Kotlin plugin to support Kotlin DSL
    kotlin("jvm") version "1.8.0"
    id("org.jetbrains.intellij") version "1.17.4"
}

group = "com.soniyck"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

intellij {
    // Define the version of IntelliJ IDEA you are targeting
    version.set("2024.1")
    type.set("RD")
    // plugins.set(listOf("com.jetbrains.rider"))
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains:marketplace-zip-signer:0.1.24")
    // Add any additional dependencies you need here
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "17"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "17"
    }

    patchPluginXml {
    }

    signPlugin {
        certificateChainFile.set(file("certificate/chain.crt"))
        privateKeyFile.set(file("certificate/private.pem"))
        password.set("xxxx")
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}