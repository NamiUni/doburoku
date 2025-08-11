# Doburoku (濁酒, 濁醪, どぶろく)

A tiny Java library for working with Kyori Adventure’s TranslatableComponent in a DRY, safe, and extensible way.  
Inspired by Moonshine, focusing on simplicity and just-enough power.

- Simple: low learning curve with a step builder and a small set of concepts
- Minimalist: delegates translation resource management to Adventure
- Effective: covers common cases with a compact API

## Modules

- doburoku-api  
  Public API: annotations, SPIs, and interfaces.
- doburoku-runtime  
  Default implementations: step builder and standard resolvers/transformers.
- doburoku-internal  
  Internal details (subject to change; don’t depend on this directly).

In most cases, depending on doburoku-runtime is sufficient (it brings doburoku-api transitively).

## Requirements

- Java 21+
- adventure-api 4.24.0+
- Currently SNAPSHOT: API may change without notice (add the snapshot repository)

## Installation


Maven (add SNAPSHOT repository):

```xml
<repositories>
  <repository>
    <id>sonatype-snapshots</id>
    <url>https://central.sonatype.com/repository/maven-snapshots/</url>
  </repository>
</repositories>

<dependencies>
  <dependency>
    <groupId>io.github.namiuni</groupId>
    <artifactId>doburoku-MODULE</artifactId>
    <version>1.0.0-SNAPSHOT</version>
  </dependency>
</dependencies>
```


Gradle (Kotlin DSL):

```kotlin
repositories {
    maven("https://central.sonatype.com/repository/maven-snapshots/")
}

dependencies {
    implementation("io.github.namiuni:doburoku-MODULE:1.0.0-SNAPSHOT")
}
```


## Quick Start

1) Prepare Adventure localization  
   Register a TranslationStore in the GlobalTranslator (see https://docs.advntr.dev/localization.html).

Example bundle (`messages_en_US.properties`):
```properties
hello.world=Hello, world!
```


2) Define a service interface
```java
public interface SimpleService {
    @Key("hello.world")
    Component helloWorld();
}
```


3) “Brew” the proxy and call it
```java
SimpleService messages = Doburoku
        .builder(SimpleService.class)
        .brew();

Component component = messages.helloWorld();
audience.sendMessage(component);
```


## Advanced Usage

- Custom argument rendering
```java
...
.argument(registry -> registry.plus(
    Player.class,
    (parameter, player) -> player.displayName()
))
```


- Result transformation (TranslatableComponent → any type)
```java
...
.result(registry -> registry.plus(
    new TypeToken<Consumer<Audience>>() {},
    (method, component) -> audience -> audience.sendMessage(component)
))
```

## Summary

The advanced usage might seem a bit complex at first, but mastering it can significantly boost your development efficiency and keep your code clean. I hope this has conveyed some of its appeal\!

We've also created a dedicated example plugin using Paper API and MiniPlaceholders. You can find it here, which might be helpful until the official wiki is ready:
[https://github.com/NamiUni/doburoku-example](https://github.com/NamiUni/doburoku-example)

Let's brew some doburoku\! (But in reality, brewing your own doburoku is illegal in Japan, so please don't do it\!)

## FAQ

**Q. Uh oh, someone saw me moonshining doburoku! What do I do now?**

A. Just take a deep breath, have a small drink of your doburoku, and relax.

## Support & Community

If you have any questions, bug reports, or feature suggestions, feel free to join our Discord server!

* **Support:** [Discord](https://discord.gg/X9s7q9ps33)
* **Bug Reports & Feature Requests:** [GitHub Issues](https://github.com/NamiUni/doburoku/issues)
