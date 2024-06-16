plugins {
    id("java")
    id("io.github.goooler.shadow") version "8.1.7"
}

group = "dev.kokiriglade"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21-R0.1-SNAPSHOT")
    implementation(project(":popcorn"))
}

tasks {
    build {
        dependsOn(shadowJar)
    }
    javadoc {
        enabled = false
    }
    processResources {
        val apiVersion = rootProject.providers.gradleProperty("mcVersion").get()
        val props = mapOf(
                "version" to project.version,
                "apiversion" to "\"$apiVersion\"",
        )
        inputs.properties(props)
        filesMatching("paper-plugin.yml") {
            expand(props)
        }
    }
    shadowJar {
        relocate("dev.kokiriglade.popcorn", "dev.kokiriglade.testplugin.corn")
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}