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
package io.github.namiuni.doburoku.reflect.internal.collection;

import io.github.namiuni.doburoku.reflect.api.collection.ArgumentResolvers;
import io.github.namiuni.doburoku.reflect.api.handlers.ArgumentResolver;
import io.github.namiuni.doburoku.reflect.internal.common.TypedProvider;
import io.leangen.geantyref.TypeToken;
import java.util.ArrayList;
import java.util.List;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ArgumentResolversImpl implements ArgumentResolvers {
    private final List<TypedProvider<ArgumentResolver<?>>> ArgumentProviders = new ArrayList<>();

    public ArgumentResolversImpl() {
    }

    @Override
    public <T> ArgumentResolvers add(final Class<T> type, final ArgumentResolver<T> resolver, final int priority) {
        this.ArgumentProviders.add(new TypedProvider<>(type, resolver, priority));
        return this;
    }

    @Override
    public <T> ArgumentResolvers add(final TypeToken<T> type, final ArgumentResolver<T> resolver, final int priority) {
        this.ArgumentProviders.add(new TypedProvider<>(type.getType(), resolver, priority));
        return this;
    }

    @Override
    public <T> ArgumentResolvers add(final Class<T> type, final ArgumentResolver<T> resolver) {
        return this.add(type, resolver, 0);
    }

    @Override
    public <T> ArgumentResolvers add(final TypeToken<T> type, final ArgumentResolver<T> resolver) {
        return this.add(type, resolver, 0);
    }

    public List<TypedProvider<ArgumentResolver<?>>> get() {
        return this.ArgumentProviders;
    }
}
