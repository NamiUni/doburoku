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
package io.github.namiuni.doburoku.api.providers;

import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.translation.Translatable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * A provider that processes a translation key and its arguments to produce a final result.
 * <p>
 * This interface is responsible for taking the constituent parts of a translation
 * (the key and arguments) and performing the final action, such as creating a
 * {@link net.kyori.adventure.text.TranslatableComponent} and returning it.
 *
 * @param <T> the type of the context object.
 */
@NullMarked
public interface ResultProvider<T> {

    /**
     * Generates a result from the given translation components.
     *
     * @param context      the context object.
     * @param translatable the translation key.
     * @param arguments    the resolved translation arguments.
     * @param <R>          the type of the result.
     * @return the final product of the translation, which may be {@code null}.
     */
    <R> @Nullable R get(T context, Translatable translatable, ComponentLike[] arguments);
}
