plugins {
    id("java")
    `maven-publish`
    id("io.papermc.paperweight.userdev") version "1.7.1"
}

group = "dev.kokiriglade"
version = "3.0.1"

paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.REOBF_PRODUCTION

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    paperweight.paperDevBundle("${properties["mcVersion"]}-R0.1-SNAPSHOT")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

configure<JavaPluginExtension> {
    withJavadocJar()
    withSourcesJar()
}

tasks.javadoc {
    (options as StandardJavadocDocletOptions).links(
            "https://jd.papermc.io/paper/${properties["mcVersion"]}/",
            "https://jd.advntr.dev/api/4.17.0/",
            "https://guava.dev/releases/32.1.2-jre/api/docs/"
    )
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
        /*maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/celerry/popcorn")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
                password = project.findProperty("gpr.token") as String? ?: System.getenv("GITHUB_TOKEN")
            }
        }*/
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
