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
package io.github.namiuni.doburoku.service.internal.registry;

import io.github.namiuni.doburoku.service.internal.common.TypedProvider;
import io.leangen.geantyref.GenericTypeReflector;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class TypedProviderRegistry<T> {

    private final List<TypedProvider<T>> providers;
    private final TypedProvider<T> sentinel;

    private final Map<Type, TypedProvider<T>> cache = new ConcurrentHashMap<>();

    public TypedProviderRegistry(final List<TypedProvider<T>> providers, final T emptyProvider) {
        this.providers = providers.stream().sorted().toList();
        this.sentinel = new TypedProvider<>(Object.class, emptyProvider, Integer.MIN_VALUE);
    }

    public @Nullable T find(final Type target) {
        final TypedProvider<T> provider = this.cache.computeIfAbsent(target, type -> {
            for (final TypedProvider<T> typedProvider : this.providers) {
                if (GenericTypeReflector.isSuperType(typedProvider.type(), type)) {
                    return typedProvider;
                }
            }
            return this.sentinel;
        });
        return provider == this.sentinel ? null : provider.provider();
    }
}
