# 🍿 popcorn <a href="https://repo.celerry.com/javadoc/releases/dev/kokiriglade/popcorn/latest"><img align="right" src="https://img.shields.io/badge/JavaDoc-Online-green"></a> <a href="https://github.com/kokiriglade/popcorn/releases/latest"><img src="https://img.shields.io/github/v/release/kokiriglade/popcorn" align="right"></a> <a href="https://github.com/kokiriglade/popcorn/blob/main/LICENSE"><img src="https://img.shields.io/github/license/kokiriglade/popcorn" align="right"></a>

Started off as a  fork of [corn](https://github.com/broccolai/corn), but just their `ItemBuilder` component. Now aiming to write general utilities for the [Paper](https://github.com/PaperMC/paper) API. As of `3.0.0`, popcorn also contains a slightly modified version of [IF](https://github.com/stefvanschie/IF) that only supports [Adventure components](https://docs.advntr.dev/text.html), and has no XML functionality.

Popcorn **only** ever supports the **latest** version of Minecraft, and thus Paper.

## Usage

[//]: # (Gradle dependency instructions)
<details>
<summary><strong>Gradle dependency instructions</strong></summary>

To add this project as a dependency for your Gradle project, make sure your dependencies section of your `build.gradle.kts` looks like the following:

```kotlin
dependencies {
    implementation("dev.kokiriglade:popcorn:3.0.0")
    // ...
}
```

You also need to add my Maven repository:

```kotlin
repositories {
    maven("https://repo.celerry.com/releases")
    // ...
}
```

In order to include the project in your own project, you will need to use the `shadowJar` plugin. If you don't have it already, add the following to the top of your file:

```kotlin
plugins {
    // ...
    id("io.github.goooler.shadow") version "8.1.7"
}
```

To relocate the project's classes to your own namespace, add the following, with `[YOUR PACKAGE]` being the top-level package of your project:
```kotlin
tasks {
    // ...
    shadowJar {
        relocate("dev.kokiriglade.popcorn", "[YOUR PACKAGE].popcorn")
    }
}
```
</details>

### Dependency via plugin.yml

popcorn does not support declaring the dependency via the libraries section in the plugin.yml. Please make use of a build tool as described above to use popcorn as a dependency.
