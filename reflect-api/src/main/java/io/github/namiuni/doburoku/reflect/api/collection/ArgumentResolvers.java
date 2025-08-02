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
package io.github.namiuni.doburoku.reflect.api.collection;

import io.github.namiuni.doburoku.reflect.api.handlers.ArgumentResolver;
import io.leangen.geantyref.TypeToken;
import org.jspecify.annotations.NullMarked;

/**
 * A collection for registering and managing {@link ArgumentResolver} instances.
 * <p>
 * This interface provides a builder-style API for adding resolvers for different
 * data types, allowing for custom serialization of objects into {@link net.kyori.adventure.text.ComponentLike}
 * arguments for translation.
 */
@NullMarked
public interface ArgumentResolvers {

    /**
     * Adds an {@link ArgumentResolver} for a specific type with a given priority.
     * <p>
     * Resolvers with higher priority values are checked before resolvers with lower values.
     *
     * @param type     the {@link Class} of the type to handle
     * @param resolver the resolver for the specified type
     * @param priority the priority of the resolver
     * @param <T>      the type
     * @return this {@link ArgumentResolvers} instance for chaining
     */
    <T> ArgumentResolvers add(Class<T> type, ArgumentResolver<T> resolver, int priority);

    /**
     * Adds an {@link ArgumentResolver} for a specific generic type with a given priority.
     * <p>
     * Resolvers with higher priority values are checked before resolvers with lower values.
     *
     * @param type     the {@link TypeToken} representing the type to handle
     * @param resolver the resolver for the specified type
     * @param priority the priority of the resolver
     * @param <T>      the type
     * @return this {@link ArgumentResolvers} instance for chaining
     */
    <T> ArgumentResolvers add(TypeToken<T> type, ArgumentResolver<T> resolver, int priority);

    /**
     * Adds an {@link ArgumentResolver} for a specific type with default priority (0).
     *
     * @param type     the {@link Class} of the type to handle
     * @param resolver the resolver for the specified type
     * @param <T>      the type
     * @return this {@link ArgumentResolvers} instance for chaining
     */
    <T> ArgumentResolvers add(Class<T> type, ArgumentResolver<T> resolver);

    /**
     * Adds an {@link ArgumentResolver} for a specific generic type with default priority (0).
     *
     * @param type     the {@link TypeToken} representing the type to handle
     * @param resolver the resolver for the specified type
     * @param <T>      the type
     * @return this {@link ArgumentResolvers} instance for chaining
     */
    <T> ArgumentResolvers add(TypeToken<T> type, ArgumentResolver<T> resolver);
}
