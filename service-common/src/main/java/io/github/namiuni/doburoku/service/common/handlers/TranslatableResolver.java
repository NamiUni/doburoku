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

import java.lang.reflect.Method;
import net.kyori.adventure.translation.Translatable;
import org.jspecify.annotations.NullMarked;

/**
 * A functional interface for resolving a {@link Translatable} object from a {@link Method}.
 * <p>
 * This is intended for reflection-based systems where a method itself dictates the
 * translation key, for example, by using an annotation on the method.
 */
@NullMarked
@FunctionalInterface
public interface TranslatableResolver {

    /**
     * Resolves a {@link Translatable} key from the given method.
     *
     * @param method the method to resolve the key from
     * @return the resolved {@link Translatable} key
     */
    Translatable resolve(Method method);
}
