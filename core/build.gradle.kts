plugins {
    id("java")
    `maven-publish`
}

group = "dev.kokiriglade"
version = "1.2.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21-R0.1-SNAPSHOT")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

configure<JavaPluginExtension> {
    withJavadocJar()
    withSourcesJar()
}

configure<PublishingExtension> {
    publications {
        register<MavenPublication>("maven") {
            artifactId = "corn"
            from(components["java"])
        }
    }
    repositories {
        mavenLocal()
        maven {
            name = "OSSRH"
            url = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = System.getenv("MAVEN_USERNAME")
                password = System.getenv("MAVEN_PASSWORD")
            }
        }
    }
}