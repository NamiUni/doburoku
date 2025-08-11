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
package io.github.namiuni.doburoku.api.key;

import io.github.namiuni.doburoku.api.exception.MissingTranslationKeyException;
import io.github.namiuni.doburoku.api.invocation.InvocationContext;
import org.jspecify.annotations.NullMarked;

/**
 * Resolves a translation key for a given reflective method invocation.
 * <p>
 * Implementations may rely on naming conventions, annotations, or other
 * context-dependent strategies.
 * </p>
 */
@NullMarked
@FunctionalInterface
public interface TranslationKeyResolver {

    /**
     * Resolves the translation key for the provided invocation context.
     *
     * @param context the method invocation context
     * @return the translation key to be used for the invocation
     * @throws MissingTranslationKeyException if the key cannot be resolved
     */
    String resolve(InvocationContext context) throws MissingTranslationKeyException;
}
