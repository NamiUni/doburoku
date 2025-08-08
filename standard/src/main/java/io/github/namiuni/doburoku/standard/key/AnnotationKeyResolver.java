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
package io.github.namiuni.doburoku.standard.key;

import io.github.namiuni.doburoku.standard.annotation.Key;
import io.github.namiuni.doburoku.common.DoburokuMethod;
import io.github.namiuni.doburoku.common.key.TranslationKeyResolver;
import java.util.HashMap;
import java.util.Map;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class AnnotationKeyResolver implements TranslationKeyResolver {

    private final Map<DoburokuMethod, String> cache = new HashMap<>();

    @Override
    public String resolve(final DoburokuMethod context) {
        return this.cache.computeIfAbsent(context, it -> it.method().getAnnotation(Key.class).value());
    }
}
