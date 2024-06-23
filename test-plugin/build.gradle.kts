plugins {
    id("java")
    id("io.github.goooler.shadow") version "8.1.7"
    id("xyz.jpenilla.run-paper") version "2.3.0"
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
    runServer {
        minecraftVersion(rootProject.providers.gradleProperty("mcVersion").get())
        downloadPlugins {
            url("https://github.com/PaperMC/Debuggery/releases/download/v1.5.1/debuggery-bukkit-1.5.1.jar")
        }
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}