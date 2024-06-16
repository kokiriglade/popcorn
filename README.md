# popcorn

[![Javadoc](https://img.shields.io/badge/JavaDoc-Online-green)](https://celerry.github.io/popcorn/javadoc/)
![GitHub Release](https://img.shields.io/github/v/release/celerry/popcorn)
![GitHub License](https://img.shields.io/github/license/celerry/popcorn)


started off as a fork of [corn](https://github.com/broccolai/corn), but just their `ItemBuilder` component. now aiming to write general utils & builders for the [paper](https://github.com/PaperMC/paper) API.

## usage

to use with gradle, follow these steps:

1. add the GitHub Packages repository
2. include the dependency
3. relocate the dependency to avoid conflicts

### step 1: add the GitHub Packages Repository

Configure your `build.gradle.kts` to include the GitHub Packages repository with authentication.

```kotlin
plugins {
    id("java")
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://maven.pkg.github.com/celerry/popcorn")
        credentials {
            username = project.findProperty("gpr.user") ?: System.getenv("GITHUB_ACTOR")
            password = project.findProperty("gpr.token") ?: System.getenv("GITHUB_TOKEN")
        }
    }
}
```

### step 2: include the dependency
```kotlin
dependencies {
    implementation("dev.kokiriglade:popcorn:VERSION")
}
```

### step 3: relocate the dependency
1. apply the `shadow` plugin:
    ```kotlin
    plugins {
        id("io.github.goooler.shadow") version "8.1.7"
    }
    ```
2. configure `shadowJar` to relocate `popcorn`
    ```kotlin
    tasks {
        shadowJar {
            relocate("dev.kokiriglade.popcorn", "your.package.popcorn")
        }
    }
    ```

### authenticate with GitHub Packages
ensure you provide your GitHub username and a personal access token.
store these in `~/.gradle/gradle.properties` for  convenience.

```properties
gpr.user=your_github_username
gpr.token=your_github_token
```