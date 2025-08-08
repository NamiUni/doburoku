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
package io.github.namiuni.doburoku.core;

import io.github.namiuni.doburoku.common.argument.TranslationArgumentResolver;
import io.github.namiuni.doburoku.common.key.TranslationKeyResolver;
import io.github.namiuni.doburoku.common.result.TranslationResultResolver;
import java.util.Objects;
import net.kyori.adventure.text.TranslatableComponent;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * A "brewery" for creating dynamic proxies of service interfaces, much like
 * how Doburoku (Japanese moonshine) is brewed.
 * <p>
 * This class provides a step-builder interface to configure how method calls on a
 * target interface are processed to produce {@link TranslatableComponent} instances.
 * It enforces the configuration order: key -> argument -> result -> brew.
 */
@NullMarked
public final class Doburoku {

    /**
     * Utility class; use {@link #from(Class)} to begin the step builder.
     */
    private Doburoku() {
    }

    /**
     * Starts the step builder for the given service interface.
     *
     * @param <I>              the service interface type
     * @param serviceInterface the interface class to proxy
     * @return the first step that requires a TranslationKeyResolver
     */
    public static <I> KeyStep<I> from(final Class<I> serviceInterface) {
        return new Builder<>(serviceInterface);
    }

    /**
     * First step: set the key resolver, then proceed to the argument step.
     *
     * @param <I> the service interface type
     */
    public sealed interface KeyStep<I> permits Builder {

        /**
         * Sets the resolver used to determine translation keys for method invocations.
         *
         * @param keyResolver the key resolver implementation
         * @return the next step to configure the argument resolver
         */
        ArgumentStep<I> key(TranslationKeyResolver keyResolver);
    }

    /**
     * Second step: set the argument resolver, then proceed to the result step.
     *
     * @param <I> the service interface type
     */
    public sealed interface ArgumentStep<I> permits Builder {

        /**
         * Sets the resolver that renders method arguments into translation components.
         *
         * @param argumentResolver the argument resolver implementation
         * @return the next step to configure the result resolver
         */
        ResultStep<I> argument(TranslationArgumentResolver argumentResolver);
    }

    /**
     * Third step: set the result resolver, then proceed to the build step.
     *
     * @param <I> the service interface type
     */
    public sealed interface ResultStep<I> permits Builder {

        /**
         * Sets the resolver that produces the final translation result.
         *
         * @param resultResolver the result resolver implementation
         * @return the final step to build the proxy instance
         */
        BuildStep<I> result(TranslationResultResolver resultResolver);
    }

    /**
     * Final step: create the dynamic proxy.
     *
     * @param <I> the service interface type
     */
    public sealed interface BuildStep<I> permits Builder {

        /**
         * Builds and returns a dynamic proxy that implements the configured service interface.
         *
         * @return a proxy implementing the service interface
         */
        I brew();
    }

    /**
     * Internal step builder implementation that enforces the build order via types.
     *
     * @param <I> the service interface type
     */
    private static final class Builder<I> implements KeyStep<I>, ArgumentStep<I>, ResultStep<I>, BuildStep<I> {

        private final Class<I> serviceInterface;
        private @Nullable TranslationKeyResolver translatableKeyResolver;
        private @Nullable TranslationArgumentResolver translationArgumentRenderers;
        private @Nullable TranslationResultResolver componentHandlers;

        private Builder(final Class<I> serviceInterface) {
            this.serviceInterface = serviceInterface;
        }

        @Override
        public ArgumentStep<I> key(final TranslationKeyResolver keyResolver) {
            this.translatableKeyResolver = keyResolver;
            return this;
        }

        @Override
        public ResultStep<I> argument(final TranslationArgumentResolver argumentResolver) {
            this.translationArgumentRenderers = argumentResolver;
            return this;
        }

        @Override
        public BuildStep<I> result(final TranslationResultResolver resultResolver) {
            this.componentHandlers = resultResolver;
            return this;
        }

        @Override
        public I brew() {
            Objects.requireNonNull(this.translatableKeyResolver);
            Objects.requireNonNull(this.translationArgumentRenderers);
            Objects.requireNonNull(this.componentHandlers);

            final DoburokuDrunkard doburokuDrunkard = new DoburokuDrunkard(
                    this.translatableKeyResolver,
                    this.translationArgumentRenderers,
                    this.componentHandlers
            );

            return DoburokuProxyFactory.of(doburokuDrunkard).create(this.serviceInterface);
        }
    }
}
