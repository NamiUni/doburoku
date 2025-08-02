/*
 * This file is part of doburoku, licensed under the MIT License.
 *
 * Copyright (c) 2025 Namiu (Unitarou)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.github.namiuni.doburoku.reflect.core;

import io.github.namiuni.doburoku.api.Doburoku;
import io.github.namiuni.doburoku.reflect.api.collection.ArgumentResolvers;
import io.github.namiuni.doburoku.reflect.api.collection.ComponentHandlers;
import io.github.namiuni.doburoku.reflect.api.handlers.ArgumentResolver;
import io.github.namiuni.doburoku.reflect.api.handlers.ComponentHandler;
import io.github.namiuni.doburoku.reflect.api.handlers.TranslatableResolver;
import io.github.namiuni.doburoku.reflect.internal.DoburokuMethod;
import io.github.namiuni.doburoku.reflect.internal.DoburokuProxyFactory;
import io.github.namiuni.doburoku.reflect.internal.collection.ArgumentResolversImpl;
import io.github.namiuni.doburoku.reflect.internal.collection.ComponentHandlersImpl;
import io.github.namiuni.doburoku.reflect.internal.registry.ArgumentResolverRegistry;
import io.github.namiuni.doburoku.reflect.internal.registry.ComponentHandlerRegistry;
import io.github.namiuni.doburoku.reflect.internal.registry.TranslatableResolverRegistry;
import io.github.namiuni.doburoku.reflect.internal.registry.TypedProviderRegistry;
import java.lang.reflect.Parameter;
import java.util.Objects;
import java.util.function.Consumer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.renderer.ComponentRenderer;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * A factory for creating a dynamic proxy of a service interface that uses the Doburoku
 * translation framework.
 * <p>
 * This class provides a builder-style interface to configure how method calls on a
 * target interface are translated into rich-text {@link Component} messages. It allows
 * specifying resolvers for translation keys, handlers for method arguments, and processors
 * for the final result.
 *
 * @param <I> the service interface type for which a proxy will be created
 */
@NullMarked
public final class DoburokuProxy<I> {

    private final Class<I> serviceInterface;
    private @Nullable TranslatableResolver translatableResolver;
    private final ArgumentResolversImpl argumentResolvers = new ArgumentResolversImpl();
    private @Nullable ComponentRenderer<Parameter> argumentTransformer;
    private final ComponentHandlersImpl resultHandlers = new ComponentHandlersImpl();

    private DoburokuProxy(final Class<I> serviceInterface) {
        this.serviceInterface = serviceInterface;
    }

    /**
     * Creates a new {@link DoburokuProxy} builder for the given service interface.
     *
     * @param serviceInterface the interface class to be proxied
     * @param <I>              the type of the service interface
     * @return a new {@link DoburokuProxy} instance
     */
    public static <I> DoburokuProxy<I> from(final Class<I> serviceInterface) {
        return new DoburokuProxy<>(serviceInterface);
    }

    /**
     * Sets the resolver that determines the translation key for each method call.
     *
     * @param resolver the {@link TranslatableResolver} to use
     * @return this builder instance for chaining
     */
    public DoburokuProxy<I> translatable(final TranslatableResolver resolver) {
        this.translatableResolver = resolver;
        return this;
    }

    /**
     * Configures the argument resolvers.
     *
     * @param resolvers a consumer that provides access to the {@link ArgumentResolvers} collection
     * @return this builder instance for chaining
     */
    public DoburokuProxy<I> argument(final Consumer<ArgumentResolvers> resolvers) {
        this.argument(resolvers, null);
        return this;
    }

    /**
     * Configures the argument resolvers and an optional final transformer.
     * <p>
     * The transformer can be used to wrap resolved arguments, for example, by turning them
     * into named placeholders for a templating engine like MiniMessage.
     *
     * @param resolvers   a consumer that provides access to the {@link ArgumentResolvers} collection
     * @param transformer an optional {@link ComponentRenderer} to transform the final resolved argument
     * @return this builder instance for chaining
     */
    public DoburokuProxy<I> argument(final Consumer<ArgumentResolvers> resolvers, final @Nullable ComponentRenderer<Parameter> transformer) {
        resolvers.accept(this.argumentResolvers);
        this.argumentTransformer = transformer;
        return this;
    }

    /**
     * Sets the final transformer for resolved arguments.
     *
     * @param transformer the {@link ComponentRenderer} to use for transforming arguments
     * @return this builder instance for chaining
     */
    public DoburokuProxy<I> argument(final @Nullable ComponentRenderer<Parameter> transformer) {
        this.argumentTransformer = transformer;
        return this;
    }

    /**
     * Configures the result handlers which process the final translated component.
     *
     * @param handlers a consumer that provides access to the {@link ComponentHandlers} collection
     * @return this builder instance for chaining
     */
    public DoburokuProxy<I> result(final Consumer<ComponentHandlers> handlers) {
        handlers.accept(this.resultHandlers);
        return this;
    }

    /**
     * "Brews" the final proxy instance based on the provided configuration.
     * <p>
     * This method assembles all the configured resolvers and handlers, creates a
     * {@link Doburoku} instance to manage the translation logic, and constructs
     * the proxy.
     *
     * @return a new proxy instance of the service interface {@code I}
     * @throws NullPointerException if a {@link TranslatableResolver} has not been set
     */
    public I brew() {
        Objects.requireNonNull(this.translatableResolver);

        final TypedProviderRegistry<ArgumentResolver<?>> argumentProvider = new TypedProviderRegistry<>(this.argumentResolvers.get(), value -> Component.empty());
        final TypedProviderRegistry<ComponentHandler<?>> resultProvider = new TypedProviderRegistry<>(this.resultHandlers.get(), component -> null);

        final Doburoku<DoburokuMethod> doburoku = Doburoku.brew(
                new TranslatableResolverRegistry(this.translatableResolver),
                new ArgumentResolverRegistry(argumentProvider, this.argumentTransformer),
                new ComponentHandlerRegistry(resultProvider)
        );

        return DoburokuProxyFactory.of(doburoku).create(this.serviceInterface);
    }
}
