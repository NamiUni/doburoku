# Doburoku (濁酒, 濁醪, どぶろく)

A tiny Java library for working with Kyori Adventure’s TranslatableComponent in a DRY, safe, and extensible way.  
Inspired by Moonshine, focusing on simplicity and just-enough power.

- Simple: low learning curve with a step builder and a small set of concepts
- Minimalist: delegates translation resource management to Adventure
- Effective: covers common cases with a compact API

## Modules

- doburoku-api  
  Public API: annotations and interfaces.

- doburoku-spi  
  Public SPI: extension points for custom resolvers and integrations.

- doburoku-internal  
  Internal details (subject to change; don’t depend on this directly).

- doburoku-standard  
  Default implementations: step builder and standard resolvers/transformers.

Tip: For most users, start with doburoku-standard.

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
    <artifactId>ARTIFACT_ID</artifactId>
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
    implementation("io.github.namiuni:ARTIFACT_ID:1.0.0-SNAPSHOT")
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
.argument((ArgumentResolverRegistry registry) -> registry.plus(
    Player.class,
    (parameter, player) -> player.displayName()
))
```


- Result transformation (TranslatableComponent → any type)
```java
...
.result((ResultResolverRegistry registry) -> registry.plus(
    new TypeToken<Consumer<Audience>>() {},
    (method, component) -> audience -> audience.sendMessage(component)
))
```


## FAQ

**Q. Uh oh, someone saw me moonshining doburoku! What do I do now?**

A. Just take a deep breath, have a small drink of your doburoku, and relax.

## Support & Community

If you have any questions, bug reports, or feature suggestions, feel free to join our Discord server!

* **Support:** [Discord](https://discord.gg/X9s7q9ps33)
* **Bug Reports & Feature Requests:** [GitHub Issues](https://github.com/NamiUni/doburoku/issues)

## License

MIT License. See the LICENSE file for details.
