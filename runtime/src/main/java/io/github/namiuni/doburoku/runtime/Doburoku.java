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
package io.github.namiuni.doburoku.runtime;

import io.github.namiuni.doburoku.api.argument.ArgumentResolverRegistry;
import io.github.namiuni.doburoku.api.argument.TranslationArgumentResolver;
import io.github.namiuni.doburoku.api.argument.TranslationArgumentTransformer;
import io.github.namiuni.doburoku.api.key.TranslationKeyResolver;
import io.github.namiuni.doburoku.api.result.ResultResolverRegistry;
import io.github.namiuni.doburoku.api.result.TranslationResultResolver;
import io.github.namiuni.doburoku.internal.DoburokuDrunkard;
import io.github.namiuni.doburoku.internal.DoburokuProxyFactory;
import io.github.namiuni.doburoku.internal.argument.ArgumentResolverRegistryImpl;
import io.github.namiuni.doburoku.internal.key.AnnotationKeyResolver;
import io.github.namiuni.doburoku.internal.result.ResultResolverRegistryImpl;
import java.util.Objects;
import java.util.function.Consumer;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TranslatableComponent;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Factory for creating dynamic proxies of service interfaces.
 *
 * <p>Provides a regular builder to configure how method calls on a target interface are processed
 * into {@link TranslatableComponent} instances. Unset options fall back to sensible defaults.</p>
 */
@NullMarked
public final class Doburoku {

    private Doburoku() {
        throw new UnsupportedOperationException();
    }

    /**
     * Starts the builder for the given service interface.
     *
     * @param <I>              the service interface type
     * @param serviceInterface the interface class to proxy
     * @return a new builder instance
     * @throws IllegalArgumentException if the given class is not an interface
     */
    public static <I> Builder<I> builder(final Class<I> serviceInterface) throws IllegalArgumentException {
        return new Builder<>(serviceInterface);
    }

    /**
     * Fluent builder for configuring and creating a proxy.
     *
     * <p>Defaults:
     * <ul>
     *   <li>Key resolver: {@link AnnotationKeyResolver}</li>
     *   <li>Argument resolver: {@link ArgumentResolverRegistryImpl}</li>
     *   <li>Result resolver: {@link ResultResolverRegistryImpl}</li>
     * </ul>
     * </p>
     *
     * @param <I> the service interface type
     */
    public static final class Builder<I> {

        private final Class<I> serviceInterface;
        private @Nullable TranslationKeyResolver keyResolver;
        private @Nullable TranslationArgumentResolver argumentResolver;
        private @Nullable TranslationResultResolver resultResolver;

        private Builder(final Class<I> serviceInterface) {
            Objects.requireNonNull(serviceInterface, "serviceInterface must not be null");
            if (!serviceInterface.isInterface()) {
                throw new IllegalArgumentException("serviceInterface must be an interface: " + serviceInterface.getName());
            }
            this.serviceInterface = serviceInterface;
        }

        /**
         * Sets the resolver used to determine translation keys for method invocations.
         *
         * @param keyResolver the key resolver implementation
         * @return this builder
         */
        public Builder<I> key(final TranslationKeyResolver keyResolver) {
            this.keyResolver = keyResolver;
            return this;
        }

        /**
         * Sets the resolver that renders method arguments into translation components.
         *
         * @param argumentResolver the argument resolver implementation
         * @return this builder
         */
        public Builder<I> argument(final TranslationArgumentResolver argumentResolver) {
            this.argumentResolver = argumentResolver;
            return this;
        }

        /**
         * Sets the resolver and applies a transformer to all rendered components.
         *
         * @param argumentResolver the base argument resolver implementation
         * @param transformer      the transformer to apply to all rendered components
         * @return this builder
         */
        public Builder<I> argument(
                final TranslationArgumentResolver argumentResolver,
                final TranslationArgumentTransformer transformer
        ) {
            return this.argument(context -> {
                final ComponentLike[] components = argumentResolver.resolve(context);
                for (int i = 0; i < components.length; i++) {
                    components[i] = transformer.transform(context.arguments()[i].parameter(), components[i]);
                }
                return components;
            });
        }

        /**
         * Configures an {@link ArgumentResolverRegistry} using a consumer function.
         *
         * @param configurator a consumer that configures the registry
         * @return this builder
         */
        public Builder<I> argument(final Consumer<ArgumentResolverRegistry> configurator) {
            final ArgumentResolverRegistryImpl registry = new ArgumentResolverRegistryImpl();
            configurator.accept(registry);
            return this.argument(registry);
        }

        /**
         * Configures an {@link ArgumentResolverRegistry} with a transformer and additional configuration.
         *
         * @param configurator a consumer that configures the registry
         * @param transformer  the argument transformer to apply to all rendered components
         * @return this builder
         */
        public Builder<I> argument(
                final Consumer<ArgumentResolverRegistry> configurator,
                final TranslationArgumentTransformer transformer
        ) {
            final ArgumentResolverRegistryImpl registry = new ArgumentResolverRegistryImpl();
            configurator.accept(registry);
            return this.argument(registry, transformer);
        }

        /**
         * Sets the resolver that produces the final translation result.
         *
         * @param resultResolver the result resolver implementation
         * @return this builder
         */
        public Builder<I> result(final TranslationResultResolver resultResolver) {
            this.resultResolver = resultResolver;
            return this;
        }

        /**
         * Configures a {@link ResultResolverRegistry} using a consumer function.
         *
         * @param configurator a consumer that configures the registry
         * @return this builder
         */
        public Builder<I> result(final Consumer<ResultResolverRegistry> configurator) {
            final ResultResolverRegistryImpl registry = new ResultResolverRegistryImpl();
            configurator.accept(registry);
            return this.result(registry);
        }

        /**
         * Builds and returns a dynamic proxy that implements the configured service interface.
         *
         * <p>Unset options fall back to defaults.</p>
         *
         * @return a proxy implementing the service interface
         */
        public I brew() {
            final TranslationKeyResolver key = Objects.requireNonNullElseGet(
                    this.keyResolver,
                    AnnotationKeyResolver::new
            );
            final TranslationArgumentResolver args = Objects.requireNonNullElseGet(
                    this.argumentResolver,
                    ArgumentResolverRegistryImpl::new
            );
            final TranslationResultResolver result = Objects.requireNonNullElseGet(
                    this.resultResolver,
                    ResultResolverRegistryImpl::new
            );

            final DoburokuDrunkard drunkard = new DoburokuDrunkard(key, args, result);
            return DoburokuProxyFactory.of(drunkard).create(this.serviceInterface);
        }
    }
}
