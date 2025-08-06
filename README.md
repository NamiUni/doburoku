# Doburoku (濁酒, 濁醪, どぶろく) WIP

A Java library to manage [Kyori/Adventure](https://github.com/KyoriPowered/adventure)'s `TranslatableComponent` in a [DRY](https://en.wikipedia.org/wiki/Don%27t_repeat_yourself) way. 

Inspired by [Kyori/Moonshine](https://github.com/KyoriPowered/moonshine), Doburoku is a type of Japanese moonshine.

## Features

* **Simple**: Simpler than Moonshine.
* **Minimalist**: Does not handle the management of translation resources.
* **Effective**: While simple, it meets most of the needs that Moonshine addresses.

## Installation

This library requires Kyori's adventure-api version 4.24.0. It is currently a SNAPSHOT version and its API may change. It is available in the Maven Central snapshot repository.

**`pom.xml`**

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
        <artifactId>doburoku-reflect-core</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

**`build.gradle.kts`**

```kotlin
repositories {
    maven("https://central.sonatype.com/repository/maven-snapshots/")
}

dependencies {
    implementation("io.github.namiuni:doburoku-reflect-core:1.0.0-SNAPSHOT")
}
```

## Basic Usage

First, you need to prepare a `Translator` or `TranslationStore`. It's easy to register a `TranslationStore` with the `GlobalTranslator`. For more details, refer to the official Adventure documentation: [https://docs.advntr.dev/localization.html](https://docs.advntr.dev/localization.html).

Let's assume you have a resource bundle like this, and you have added its source to `GlobalTranslator`:

**`simple_en_US.properties`**

```properties
hello.world=Hello, world!
```

Create a service interface with a return type of `Component`.

```java
public interface SimpleService {
    Component helloWorld();
}
```

`DoburokuBrewery` is the entry point. By passing your service interface to `DoburokuBrewery.from(Class<?>)` and calling `brew()`, a dynamic proxy instance is 'brewed' for you.

```java
SimpleService simpleService = DoburokuBrewery
    .from(SimpleService.class)
    .brew();
```

Now, when you call the `helloWorld()` method on the created proxy object, it will return a `TranslatableComponent` with the translation key `hello.world`. The method name is converted from camel case to a dot-separated key by default.

```java
Component message = simpleService.helloWorld();
audience.sendMessage(message); // result: "Hello, world!"
```

## Advanced Usage

To demonstrate the full potential of this library, we'll use a more complex example with the Paper API. The following assumes a resource bundle with these patterns, already added to the `GlobalTranslator`.

**`example_en_US.properties`**

```properties
example.join.message={0} came to brew doburoku! Total {1} brewers!!
example.welcome.message=Welcome! {0}!!
```

Define a service interface:

```java
public interface ExampleService {
    void joinMessage(Player player, int Count);
    Consumer<Player> welcomeMessage(Player player);
}
```

Now, let's brew the proxy with a more advanced configuration:

```java
ExampleService exampleService = DoburokuService
    .from(ExampleService.class)
    .translatable(DefaultTranslatableResolver.create("example"))
    .argument(resolvers -> resolvers
            .add(Player.class, player -> player.displayName())
    )
    .result(handlers -> handlers
            .add(new TypeToken<Consumer<Audience>>() { }, component -> {
                return audience -> audience.sendMessage(component);
            })
    )
    .brew();
```

### `DoburokuBrewery#translatable(TranslatableResolver)`

This method configures how the translation key for a `TranslatableComponent` is resolved from a method. The default implementation, `DefaultTranslatableResolver`, converts the method name from camel case to a dot-separated key. You can use `DefaultTranslatableResolver.create("example")` to prepend a prefix, so `joinMessage` becomes `example.join.message`. You can also provide your own custom `TranslatableResolver` implementation.

### `DoburokuBrewery#argument(Consumer<ArgumentResolvers>)`

This method customizes how method arguments are resolved into `ComponentLike` objects for the `TranslatableComponent`'s arguments. You can add `ArgumentResolver`s for specific types. For types that are already `ComponentLike`, no resolver is needed. For other types, the library defaults to `String.valueOf()` and then wraps it in `Component.text()`. Here, we add a resolver to handle `Player` objects by converting them to their display name.

### `DoburokuBrewery#result(Consumer<ComponentHandlers>)`

This method defines how the generated `TranslatableComponent` is transformed into the method's return type. You can add `ComponentHandler`s for specific return types. If a handler for a return type is not provided, calling the method will throw an `IllegalStateException`. In this example, we've added a handler for Consumer<Audience> to return a message consumer.
### Final Invocation

Now, you can use the `ExampleService` object as follows:

```java
// example result: Welcome! Unitarou!!
// perform: brewer.sendMessage(ComponentLike);
Consumer<Audience> welcomeSender = exampleService.welcomeMessage(brewer);
welcomeSender.accept(brewer);
```

## Summary

The advanced usage might seem a bit complex at first, but mastering it can significantly boost your development efficiency and keep your code clean. I hope this has conveyed some of its appeal\!

We've also created a dedicated example plugin using Paper API and MiniPlaceholders. You can find it here, which might be helpful until the official wiki is ready:
[https://github.com/NamiUni/doburoku-example](https://github.com/NamiUni/doburoku-example)

Let's brew some doburoku\! (But in reality, brewing your own doburoku is illegal in Japan, so please don't do it\!)

## Support & Community

If you have any questions, bug reports, or feature suggestions, feel free to join our Discord server!

* **Support:** [Discord](https://discord.gg/X9s7q9ps33)
* **Bug Reports & Feature Requests:** [GitHub Issues](https://github.com/NamiUni/doburoku/issues)

## FAQ

**Q. Uh oh, someone saw me moonshining doburoku! What do I do now?**

A. Just take a deep breath, have a small drink of your doburoku, and relax.
