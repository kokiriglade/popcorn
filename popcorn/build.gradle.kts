plugins {
    idea
    `java-library`
    `maven-publish`
    checkstyle
    id("io.papermc.paperweight.userdev") version "1.7.1"
    id("io.github.goooler.shadow") version "8.1.7"
}

group = "dev.kokiriglade"
version = "3.2.0"

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
        relocate("dev.dejvokep.boostedyaml", "dev.kokiriglade.popcorn.lib.boostedyaml")
        archiveFileName.set("${project.name}-${project.version}.jar")
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

configure<PublishingExtension> {
    publications {
        register<MavenPublication>("maven") {
            artifactId = "popcorn"
            from(components["java"])
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
