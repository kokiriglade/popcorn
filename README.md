# popcorn

[![Javadoc](https://img.shields.io/badge/JavaDoc-Online-green)](https://repo.celerry.com/javadoc/releases/dev/kokiriglade/popcorn/latest)
![GitHub Release](https://img.shields.io/github/v/release/celerry/popcorn)
![GitHub License](https://img.shields.io/github/license/celerry/popcorn)

started off as a fork of [corn](https://github.com/broccolai/corn), but just their `ItemBuilder` component. now aiming
to write general utils & builders for the [paper](https://github.com/PaperMC/paper) API.

## usage

to use with gradle, follow these steps:

1. add the maven repository
2. include the dependency
3. relocate the dependency to avoid conflicts

### step 1: add the maven repo

Configure your `build.gradle.kts` to include the GitHub Packages repository with authentication.

```kotlin
plugins {
    id("java")
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://repo.celerry.com/releases")
    }
}
```

### step 2: include the dependency

```kotlin
dependencies { 
    implementation("dev.kokiriglade:popcorn:2.2.2")
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