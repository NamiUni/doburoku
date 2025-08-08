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
 * This class provides a builder-style interface to configure how method calls on a
 * target interface are processed to produce {@link TranslatableComponent} instances.
 * It allows specifying resolvers for translation keys, handlers for method arguments, and processors
 * for the result.
 *
 * @param <I> the service interface type for which a proxy will be "brewed"
 */
@NullMarked
public final class DoburokuService<I> {

    private final Class<I> serviceInterface;
    private @Nullable TranslationKeyResolver translatableKeyResolver;
    private @Nullable TranslationArgumentResolver translationArgumentRenderers;
    private @Nullable TranslationResultResolver componentHandlers;

    private DoburokuService(final Class<I> serviceInterface) {
        this.serviceInterface = serviceInterface;
    }

    /**
     * Creates a new builder for the given service interface.
     *
     * @param <I> the service interface type
     * @param serviceInterface the interface class to proxy
     * @return a new {@code DoburokuService} instance
     */
    public static <I> DoburokuService<I> from(final Class<I> serviceInterface) {
        return new DoburokuService<>(serviceInterface);
    }

    /**
     * Sets the resolver used to determine translation keys for method invocations.
     *
     * @param keyResolver the key resolver
     * @return this instance for chaining
     */
    public DoburokuService<I> key(final TranslationKeyResolver keyResolver) {
        this.translatableKeyResolver = keyResolver;
        return this;
    }

    /**
     * Sets the resolver that renders method arguments into translation components.
     *
     * @param argumentResolver the argument resolver
     * @return this instance for chaining
     */
    public DoburokuService<I> argument(final TranslationArgumentResolver argumentResolver) {
        this.translationArgumentRenderers = argumentResolver;
        return this;
    }

    /**
     * Sets the resolver that produces the final translation result.
     *
     * @param resultResolver the result resolver
     * @return this instance for chaining
     */
    public DoburokuService<I> result(final TranslationResultResolver resultResolver) {
        this.componentHandlers = resultResolver;
        return this;
    }

    /**
     * Builds and returns a dynamic proxy that implements the configured service interface.
     *
     * @return a proxy implementing the service interface
     * @throws NullPointerException if any required resolver has not been configured
     */
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
