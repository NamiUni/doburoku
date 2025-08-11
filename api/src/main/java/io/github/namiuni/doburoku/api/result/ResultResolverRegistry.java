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
package io.github.namiuni.doburoku.api.result;

import io.leangen.geantyref.TypeToken;
import java.lang.reflect.Type;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Registry of {@link TranslatableComponentTransformer} mappings by Java type.
 *
 * <p>Implementations may be mutable; callers can register transformers for raw or generic types.</p>
 */
@NullMarked
@ApiStatus.Internal
public interface ResultResolverRegistry {

    /**
     * Registers a transformer for a return type.
     *
     * @param <T> the result type
     * @param type the raw class to associate with the transformer
     * @param transformer the transformer to register
     * @return this registry for chaining
     */
    <T> ResultResolverRegistry plus(Type type, TranslatableComponentTransformer<T> transformer);

    /**
     * Registers a transformer for a raw return type.
     *
     * @param <T>         the result type
     * @param type        the raw class to associate with the transformer
     * @param transformer the transformer to register
     * @return this registry for chaining
     */
    default <T> ResultResolverRegistry plus(final Class<T> type, final TranslatableComponentTransformer<T> transformer) {
        return this.plus((Type) type, transformer);
    }

    /**
     * Registers a transformer for a generic return type.
     *
     * @param <T>         the result type
     * @param type        the generic type token
     * @param transformer the transformer to register
     * @return this registry for chaining
     */
    default <T> ResultResolverRegistry plus(final TypeToken<T> type, final TranslatableComponentTransformer<T> transformer) {
        return this.plus(type.getType(), transformer);
    }
}
