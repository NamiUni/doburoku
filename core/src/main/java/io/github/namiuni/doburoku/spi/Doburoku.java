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
package io.github.namiuni.doburoku.spi;

import io.github.namiuni.doburoku.api.argument.TranslationArgumentResolver;
import io.github.namiuni.doburoku.api.key.TranslationKeyResolver;
import io.github.namiuni.doburoku.api.result.TranslationResultResolver;
import io.github.namiuni.doburoku.internal.DoburokuDrunkard;
import io.github.namiuni.doburoku.internal.DoburokuProxyFactory;
import java.util.Objects;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Builder for configuring a dynamic proxy.
 *
 * @param <B> the builder type
 * @param <I> the service interface type
 */
@NullMarked
public class Doburoku<B extends Doburoku<B, I>, I> {

    /** The service interface. */
    protected final Class<I> serviceInterface;
    /** The key resolver. */
    protected @Nullable TranslationKeyResolver keyResolver;
    /** The argument resolver. */
    protected @Nullable TranslationArgumentResolver argumentResolver;
    /** The result resolver. */
    protected @Nullable TranslationResultResolver resultResolver;

    /**
     * Creates a new instance.
     *
     * @param serviceInterface the service interface
     * @throws IllegalArgumentException if the given class is not an interface
     */
    protected Doburoku(final Class<I> serviceInterface) throws IllegalArgumentException {
        Objects.requireNonNull(serviceInterface, "serviceInterface must not be null");
        if (!serviceInterface.isInterface()) {
            throw new IllegalArgumentException("serviceInterface must be an interface: " + serviceInterface.getName());
        }
        this.serviceInterface = serviceInterface;
    }

    /**
     * Starts the builder for the given service interface.
     *
     * @param <I>              the service interface type
     * @param serviceInterface the interface class to proxy
     * @return a new builder instance
     * @throws IllegalArgumentException if the given class is not an interface
     */
    public static <I> Doburoku<?, I> of(final Class<I> serviceInterface) {
        return new Doburoku<>(serviceInterface);
    }

    /**
     * Sets the resolver used to determine translation keys for method invocations.
     *
     * @param keyResolver the key resolver implementation
     * @return this builder
     */
    @Contract(mutates = "this")
    @SuppressWarnings("unchecked")
    public final B key(final TranslationKeyResolver keyResolver) {
        Objects.requireNonNull(keyResolver, "keyResolver");
        this.keyResolver = keyResolver;
        return (B) Doburoku.this;
    }

    /**
     * Sets the resolver that renders method arguments into translation components.
     *
     * @param argumentResolver the argument resolver implementation
     * @return this builder
     */
    @Contract(mutates = "this")
    @SuppressWarnings("unchecked")
    public final B argument(final TranslationArgumentResolver argumentResolver) {
        Objects.requireNonNull(argumentResolver, "argumentResolver");
        this.argumentResolver = argumentResolver;
        return (B) Doburoku.this;
    }

    /**
     * Sets the resolver that produces the final translation result.
     *
     * @param resultResolver the result resolver implementation
     * @return this builder
     */
    @Contract(mutates = "this")
    @SuppressWarnings("unchecked")
    public final B result(final TranslationResultResolver resultResolver) {
        Objects.requireNonNull(resultResolver, "resultResolver");
        this.resultResolver = resultResolver;
        return (B) Doburoku.this;
    }

    /**
     * Builds and returns a dynamic proxy that implements the configured service interface.
     *
     * <p>Unset options fall back to defaults.</p>
     *
     * @return a proxy implementing the service interface
     */
    public I brew() {
        final Class<I> service = Objects.requireNonNull(this.serviceInterface);
        final TranslationKeyResolver key = Objects.requireNonNull(this.keyResolver);
        final TranslationArgumentResolver argument = Objects.requireNonNull(this.argumentResolver);
        final TranslationResultResolver result = Objects.requireNonNull(this.resultResolver);

        final DoburokuDrunkard drunkard = new DoburokuDrunkard(key, argument, result);
        return DoburokuProxyFactory.of(drunkard).create(service);
    }
}
