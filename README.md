# 🍿 popcorn <a href="https://repo.celerry.com/javadoc/releases/dev/kokiriglade/popcorn/latest"><img align="right" src="https://img.shields.io/badge/JavaDoc-Online-green"></a> <a href="https://github.com/kokiriglade/popcorn/releases/latest"><img src="https://img.shields.io/github/v/release/kokiriglade/popcorn" align="right"></a> <a href="https://github.com/kokiriglade/popcorn/blob/main/LICENSE"><img src="https://img.shields.io/github/license/kokiriglade/popcorn" align="right"></a>

A very opinionated utility library for Paper. Started off as a  fork of [corn](https://github.com/broccolai/corn)'s ItemBuilder utilities. Popcorn also contains a slightly modified version of [IF](https://github.com/stefvanschie/IF) that only supports [Adventure components](https://docs.advntr.dev/text.html), and has no XML functionality.

You can find builds to put inside your server's `plugins` folder on [Hangar](https://hangar.papermc.io/kokiriglade/popcorn).

> [!NOTE]
> Popcorn **only** ever supports the **latest** version of Minecraft, and thus Paper.

## Usage

### Add to gradle build script

To add this project as a dependency for your Gradle project, make sure your dependencies section of your `build.gradle.kts` looks like the following:

```kotlin
dependencies {
    compileOnly("dev.kokiriglade:popcorn:[VERSION]")
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

### Dependency via paper-plugin.yml

```yaml
# ...
dependencies:
    server:
        popcorn:
            load: BEFORE
            required: true
            join-classpath: true
```
