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
package io.github.namiuni.doburoku.api.argument;

import io.leangen.geantyref.TypeToken;
import java.lang.reflect.Type;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Registry of {@link TranslationArgumentRenderer} mappings by Java type.
 *
 * <p>Implementations may be mutable; callers can register renderers for raw or generic types.</p>
 */
@NullMarked
@ApiStatus.Internal
public interface ArgumentResolverRegistry {

    /**
     * Registers a renderer for the given type.
     *
     * @param <T> the type to render
     * @param type the type to associate with the renderer
     * @param renderer the renderer implementation
     * @return this registry for chaining
     */
    <T> ArgumentResolverRegistry plus(Type type, TranslationArgumentRenderer<T> renderer);

    /**
     * Registers a renderer for the given raw type.
     *
     * @param <T>      the type to render
     * @param type     the raw class to associate with the renderer
     * @param renderer the renderer implementation
     * @return this registry for chaining
     */
    default <T> ArgumentResolverRegistry plus(final Class<T> type, final TranslationArgumentRenderer<T> renderer) {
        return this.plus((Type) type, renderer);
    }

    /**
     * Registers a renderer for the given generic type.
     *
     * @param <T>      the type to render
     * @param type     the generic type token
     * @param renderer the renderer implementation
     * @return this registry for chaining
     */
    default <T> ArgumentResolverRegistry plus(final TypeToken<T> type, final TranslationArgumentRenderer<T> renderer) {
        return this.plus(type.getType(), renderer);
    }
}
