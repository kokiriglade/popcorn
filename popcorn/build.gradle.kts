plugins {
    idea
    `java-library`
    `maven-publish`
    checkstyle
    id("io.papermc.paperweight.userdev") version "1.7.1"
    id("io.github.goooler.shadow") version "8.1.7"
    id("io.papermc.hangar-publish-plugin") version "0.1.2"
}

group = "dev.kokiriglade"
version = "3.3.1"

paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.REOBF_PRODUCTION

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    checkstyle("ca.stellardrift:stylecheck:0.2.1")
    api("dev.dejvokep:boosted-yaml:1.3.5")
    paperweight.paperDevBundle("${properties["mcVersion"]}-R0.1-SNAPSHOT")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

configure<JavaPluginExtension> {
    withJavadocJar()
    withSourcesJar()
}

tasks.withType<Checkstyle>().configureEach {
    configFile = file("config/checkstyle/checkstyle.xml")
}

tasks {
    build {
        dependsOn(shadowJar)
    }
    javadoc {
        dependsOn(check)

        (options as StandardJavadocDocletOptions).apply {
            encoding = Charsets.UTF_8.name()
            addStringOption("tag", "implNote:a:Implementation Note")
            addStringOption("tag", "apiNote:a:API Note")
            links(
                "https://jd.papermc.io/paper/${rootProject.properties["mcVersion"]}/",
                "https://jd.advntr.dev/api/4.17.0/",
                "https://guava.dev/releases/32.1.2-jre/api/docs/",
                "https://javadoc.io/static/dev.dejvokep/boosted-yaml/1.3/",
                "https://repo.celerry.com/javadoc/mirror/com/mojang/brigadier/1.2.9/raw/"
            )
        }
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
        dependsOn(check)
        archiveFileName.set("${project.name}-${project.version}.jar")
        relocate("dev.dejvokep.boostedyaml", "dev.kokiriglade.popcorn.lib.boostedyaml")
    }

    val copyToRunTaskPluginsDirectory by creating(Copy::class) {
        val pluginsDir = file("../test-plugin/run/plugins")
        dependsOn(shadowJar)

        from(shadowJar.get().archiveFile)
        into(pluginsDir)

        doFirst {
            file(pluginsDir).mkdirs()
            pluginsDir.listFiles()?.forEach { file ->
                if (file.name.startsWith(project.name) && file.name.endsWith(".jar")) {
                    file.delete()
                }
            }
        }
    }
}

hangarPublish {
    publications.register("plugin") {
        version = project.version as String // use project version as publication version
        id = "popcorn"
        channel = "Release"

        apiKey = project.findProperty("hangarAPIKey") as String? ?: System.getenv("HANGAR_KEY")

        // register platforms
        platforms {
            paper {
                jar = tasks.shadowJar.flatMap { it.archiveFile }
                platformVersions = listOf(rootProject.properties["mcVersion"].toString())
                dependencies {
                }
            }
        }
    }
}

configure<PublishingExtension> {
    publications {
        register<MavenPublication>("maven") {
            artifact(tasks.shadowJar) {
                artifactId = "popcorn"
                classifier = ""
            }
            artifact(tasks["sourcesJar"])
            artifact(tasks["javadocJar"])
        }
    }
    repositories {
        mavenLocal()
        maven {
            name = "celerry"
            url = uri("https://repo.celerry.com/releases")
            credentials {
                username = project.findProperty("celerryUsername") as String? ?: System.getenv("CELERRY_NAME")
                password = project.findProperty("celerryPassword") as String? ?: System.getenv("CELERRY_PASS")
            }
        }
    }
}
