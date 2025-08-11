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
import org.jspecify.annotations.NullMarked;

/**
 * Builder for configuring a dynamic proxy.
 *
 * @param <I> the service interface type
 * @param <B> the builder type
 */
@NullMarked
public interface DoburokuBuilder<I, B extends DoburokuBuilder<I, B>> {

    /**
     * Sets the resolver used to determine translation keys for method invocations.
     *
     * @param keyResolver the key resolver implementation
     * @return this builder
     */
    B key(TranslationKeyResolver keyResolver);

    /**
     * Sets the resolver that renders method arguments into translation components.
     *
     * @param argumentResolver the argument resolver implementation
     * @return this builder
     */
    B argument(TranslationArgumentResolver argumentResolver);

    /**
     * Sets the resolver that produces the final translation result.
     *
     * @param resultResolver the result resolver implementation
     * @return this builder
     */
    B result(TranslationResultResolver resultResolver);

    /**
     * Builds and returns a dynamic proxy that implements the configured service interface.
     *
     * <p>Unset options fall back to defaults.</p>
     *
     * @return a proxy implementing the service interface
     */
    I brew();
}
