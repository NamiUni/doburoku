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
package io.github.namiuni.doburoku.reflect.internal.registry;

import io.github.namiuni.doburoku.api.providers.TranslatableProvider;
import io.github.namiuni.doburoku.reflect.api.handlers.TranslatableResolver;
import io.github.namiuni.doburoku.reflect.internal.DoburokuMethod;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.kyori.adventure.translation.Translatable;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class TranslatableResolverRegistry implements TranslatableProvider<DoburokuMethod> {

    private final TranslatableResolver translatableResolver;

    private final Map<Method, Translatable> translatableCache = new ConcurrentHashMap<>();

    public TranslatableResolverRegistry(final TranslatableResolver translatableResolver) {
        this.translatableResolver = translatableResolver;
    }

    @Override
    public Translatable get(final DoburokuMethod context) {
        return this.translatableCache.computeIfAbsent(context.method(), this.translatableResolver::resolve);
    }
}
