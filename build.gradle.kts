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
    type.set("IC") // Target IDE Platform: IC = IntelliJ IDEA Community, IU = IntelliJ IDEA Ultimate, etc.
    plugins.set(listOf("java"))
}

dependencies {
    implementation(kotlin("stdlib"))
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
        sinceBuild.set("231")
    }
    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}