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
package io.github.namiuni.doburoku.internal.key;

import io.github.namiuni.doburoku.api.annotation.Key;
import io.github.namiuni.doburoku.api.exception.MissingTranslationKeyException;
import io.github.namiuni.doburoku.api.invocation.InvocationContext;
import io.github.namiuni.doburoku.api.key.TranslationKeyResolver;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.jspecify.annotations.NullMarked;

/**
 * Resolves translation keys from the {@link Key} annotation on target methods.
 *
 * <p>Keys are cached per {@link Method} to avoid repeated lookups. This implementation is thread-safe.</p>
 */
@NullMarked
public final class AnnotationKeyResolver implements TranslationKeyResolver {

    private final Map<Method, String> cache = new ConcurrentHashMap<>();

    /**
     * Creates a new key resolver.
     */
    public AnnotationKeyResolver() {
    }

    /**
     * Resolves the translation key by reading the {@link Key} annotation on the
     * method represented by the provided context, caching the result.
     *
     * @param context the method invocation context
     * @return the translation key declared on the method
     * @throws MissingTranslationKeyException if the method is not annotated with {@link Key}
     */
    @Override
    public String resolve(final InvocationContext context) throws MissingTranslationKeyException {
        final Method method = context.method();
        return this.cache.computeIfAbsent(method, this::extractKey);
    }

    private String extractKey(final Method method) {
        final Key annotation = method.getAnnotation(Key.class);
        if (annotation == null) {
            throw new MissingTranslationKeyException("Missing @Key on method: " + method);
        }
        return annotation.value();
    }
}
