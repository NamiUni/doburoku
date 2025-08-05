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
package io.github.namiuni.doburoku.reflect;

import io.github.namiuni.doburoku.service.common.collection.ArgumentResolvers;
import io.github.namiuni.doburoku.service.common.collection.ComponentHandlers;
import io.github.namiuni.doburoku.service.common.handlers.ArgumentResolver;
import io.github.namiuni.doburoku.service.common.handlers.ComponentHandler;
import io.github.namiuni.doburoku.service.common.handlers.TranslatableResolver;
import io.github.namiuni.doburoku.service.internal.DoburokuDrunkard;
import io.github.namiuni.doburoku.service.internal.DoburokuMethod;
import io.github.namiuni.doburoku.service.internal.DoburokuProxyFactory;
import io.github.namiuni.doburoku.service.internal.collection.ArgumentResolversImpl;
import io.github.namiuni.doburoku.service.internal.collection.ComponentHandlersImpl;
import io.github.namiuni.doburoku.service.internal.registry.ArgumentResolverRegistry;
import io.github.namiuni.doburoku.service.internal.registry.ComponentHandlerRegistry;
import io.github.namiuni.doburoku.service.internal.registry.TranslatableResolverRegistry;
import io.github.namiuni.doburoku.service.internal.registry.TypedProviderRegistry;
import java.lang.reflect.Parameter;
import java.util.Objects;
import java.util.function.Consumer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.renderer.ComponentRenderer;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * A "brewery" for creating dynamic proxies of service interfaces, much like
 * how Doburoku (Japanese moonshine) is brewed.
 * <p>
 * This class provides a builder-style interface to configure how method calls on a
 * target interface are processed to produce {@link TranslatableComponent} instances.
 * It allows specifying resolvers for translation keys, handlers for method arguments, and processors
 * for the result.
 *
 * @param <I> the service interface type for which a proxy will be "brewed"
 */
@NullMarked
public final class DoburokuBrewery<I> {

    private final Class<I> serviceInterface;
    private @Nullable TranslatableResolver translatableResolver = DefaultTranslatableResolver.create("");
    private final ArgumentResolversImpl argumentResolvers = new ArgumentResolversImpl();
    private @Nullable ComponentRenderer<Parameter> argumentTransformer;
    private final ComponentHandlersImpl resultHandlers = new ComponentHandlersImpl();

    private DoburokuBrewery(final Class<I> serviceInterface) {
        this.serviceInterface = serviceInterface;
    }

    /**
     * Creates a new {@link DoburokuBrewery} instance for the given service interface.
     *
     * @param serviceInterface the interface class to be proxied
     * @param <I>              the type of the service interface
     * @return a new {@link DoburokuBrewery} instance
     */
    public static <I> DoburokuBrewery<I> from(final Class<I> serviceInterface) {
        return new DoburokuBrewery<>(serviceInterface);
    }

    /**
     * Sets the resolver that determines the translation key for each method call.
     *
     * @param resolver the {@link TranslatableResolver} to use
     * @return this brewery instance for chaining
     */
    public DoburokuBrewery<I> translatable(final TranslatableResolver resolver) {
        this.translatableResolver = resolver;
        return this;
    }

    /**
     * Configures the argument resolvers.
     *
     * @param resolvers a consumer that provides access to the {@link ArgumentResolvers} collection
     * @return this brewery instance for chaining
     */
    public DoburokuBrewery<I> argument(final Consumer<ArgumentResolvers> resolvers) {
        this.argument(resolvers, null);
        return this;
    }

    /**
     * Configures the argument resolvers and an optional transformer.
     * <p>
     * The transformer can be used to wrap resolved arguments, for example, by turning them
     * into named placeholders for a templating engine like {@code MiniMessage}.
     *
     * @param resolvers   a consumer that provides access to the {@link ArgumentResolvers} collection
     * @param transformer an optional {@link ComponentRenderer} to transform the resolved argument
     * @return this brewery instance for chaining
     */
    public DoburokuBrewery<I> argument(final Consumer<ArgumentResolvers> resolvers, final @Nullable ComponentRenderer<Parameter> transformer) {
        resolvers.accept(this.argumentResolvers);
        this.argumentTransformer = transformer;
        return this;
    }

    /**
     * Sets the transformer for resolved arguments.
     *
     * @param transformer the {@link ComponentRenderer} to use for transforming arguments
     * @return this brewery instance for chaining
     */
    public DoburokuBrewery<I> argument(final @Nullable ComponentRenderer<Parameter> transformer) {
        this.argumentTransformer = transformer;
        return this;
    }

    /**
     * Configures the fermenting process, which process the {@link TranslatableComponent} produced by the brewery.
     *
     * @param handlers a consumer that provides access to the {@link ComponentHandlers} collection
     * @return this brewery instance for chaining
     */
    public DoburokuBrewery<I> ferment(final Consumer<ComponentHandlers> handlers) {
        handlers.accept(this.resultHandlers);
        return this;
    }

    /**
     * "Brews" the proxy instance based on the provided configuration.
     *
     * @return a new proxy instance of the service interface {@code I}
     * @throws NullPointerException if a {@link TranslatableResolver} has not been set
     */
    public I brew() {
        Objects.requireNonNull(this.translatableResolver);

        final TypedProviderRegistry<ArgumentResolver<?>> argumentProvider = new TypedProviderRegistry<>(this.argumentResolvers.get(), value -> Component.empty());
        final TypedProviderRegistry<ComponentHandler<?>> resultProvider = new TypedProviderRegistry<>(this.resultHandlers.get(), component -> null);

        final DoburokuDrunkard<DoburokuMethod> doburokuDrunkard = DoburokuDrunkard.brew(
                new TranslatableResolverRegistry(this.translatableResolver),
                new ArgumentResolverRegistry(argumentProvider, this.argumentTransformer),
                new ComponentHandlerRegistry(resultProvider)
        );

        return DoburokuProxyFactory.of(doburokuDrunkard).create(this.serviceInterface);
    }
}
