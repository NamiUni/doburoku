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
package io.github.namiuni.doburoku.core;

import io.github.namiuni.doburoku.core.key.KeyResolver;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
record CachedMethod(
        Method method,
        String key,
        TranslationServiceFactory factory,
        KeyResolver keyResolver,
        @Nullable Supplier<Object> childServiceSupplier) {

    static CachedMethod create(
            final TranslationServiceFactory factory,
            final KeyResolver keyResolver,
            final Method method,
            final String parentKey
    ) {

        return new CachedMethod(
                method,
                keyResolver.resolve(parentKey, method),
                factory,
                keyResolver,
                CachedMethod.createChildService(method, factory)
        );
    }

    private static @Nullable Supplier<Object> createChildService(
            final Method method,
            final TranslationServiceFactory factory
    ) {
        if (method.getDeclaringClass() != method.getReturnType().getDeclaringClass()) {
            return null;
        }

        return new Supplier<>() {
            private final AtomicReference<@Nullable Object> childServiceRef = new AtomicReference<>();

            @Override
            public Object get() {
                final Object currentChildService = this.childServiceRef.get();
                if (currentChildService != null) {
                    return currentChildService;
                }

                final Object newService = factory.create(method.getReturnType());
                if (this.childServiceRef.compareAndSet(null, newService)) {
                    return newService;
                } else {
                    return Objects.requireNonNull(this.childServiceRef.get());
                }
            }
        };
    }
}
