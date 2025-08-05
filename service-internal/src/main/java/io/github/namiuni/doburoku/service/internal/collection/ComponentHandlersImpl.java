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
package io.github.namiuni.doburoku.service.internal.collection;

import io.github.namiuni.doburoku.service.common.collection.ComponentHandlers;
import io.github.namiuni.doburoku.service.common.handlers.ComponentHandler;
import io.github.namiuni.doburoku.service.internal.common.TypedProvider;
import io.leangen.geantyref.TypeToken;
import java.util.ArrayList;
import java.util.List;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ComponentHandlersImpl implements ComponentHandlers {
    private final List<TypedProvider<ComponentHandler<?>>> resultHandlers = new ArrayList<>();

    public ComponentHandlersImpl() {
    }

    @Override
    public <R> ComponentHandlers add(final TypeToken<R> type, final ComponentHandler<R> handler, final int priority) {
        this.resultHandlers.add(new TypedProvider<>(type.getType(), handler, priority));
        return this;
    }

    @Override
    public <R> ComponentHandlers add(final Class<R> type, final ComponentHandler<R> handler, final int priority) {
        this.resultHandlers.add(new TypedProvider<>(type, handler, priority));
        return this;
    }

    @Override
    public <R> ComponentHandlers add(final TypeToken<R> type, final ComponentHandler<R> handler) {
        return this.add(type, handler, 0);
    }

    @Override
    public <R> ComponentHandlers add(final Class<R> type, final ComponentHandler<R> handler) {
        return this.add(type, handler, 0);
    }

    public List<TypedProvider<ComponentHandler<?>>> get() {
        return this.resultHandlers;
    }
}
