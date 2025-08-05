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
package io.github.namiuni.doburoku.service.common.handlers;

import net.kyori.adventure.text.ComponentLike;
import org.jspecify.annotations.NullMarked;

/**
 * Resolves an object of type {@code T} into a {@link ComponentLike} representation.
 * <p>
 * This is used to convert arbitrary objects into components that can be inserted as
 * arguments into a {@link net.kyori.adventure.text.TranslatableComponent}.
 *
 * @param <T> the type of the value to resolve
 */
@NullMarked
public interface ArgumentResolver<T> {

    /**
     * Resolves the given value into a {@link ComponentLike}.
     *
     * @param value the value to resolve
     * @return the resolved {@link ComponentLike}
     */
    ComponentLike resolve(T value);
}
