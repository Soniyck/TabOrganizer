plugins {
    // Kotlin plugin to support Kotlin DSL
    kotlin("jvm") version "1.8.0"
    id("org.jetbrains.intellij") version "1.17.4"
}

group = "com.soniyck"
version = "0.9"

repositories {
    mavenCentral()
}

intellij {
    version.set("2023.3")
    type.set("IC")
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
        version.set("2023.3")
        sinceBuild.set("233")
        untilBuild.set("241.*")
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