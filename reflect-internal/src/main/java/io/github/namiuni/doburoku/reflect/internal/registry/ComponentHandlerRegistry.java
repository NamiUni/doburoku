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

import io.github.namiuni.doburoku.api.providers.ResultProvider;
import io.github.namiuni.doburoku.reflect.api.handlers.ComponentHandler;
import io.github.namiuni.doburoku.reflect.internal.DoburokuMethod;
import java.lang.reflect.Type;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.translation.Translatable;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ComponentHandlerRegistry implements ResultProvider<DoburokuMethod> {

    private final TypedProviderRegistry<ComponentHandler<?>> componentHandlers;

    public ComponentHandlerRegistry(final TypedProviderRegistry<ComponentHandler<?>> componentHandlers) {
        this.componentHandlers = componentHandlers;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R> R get(
            final DoburokuMethod context,
            final Translatable translatable,
            final ComponentLike[] arguments
    ) {
        final Type type = context.method().getGenericReturnType();
        final ComponentHandler<R> componentHandler = (ComponentHandler<R>) this.componentHandlers.find(type);

        final TranslatableComponent result = Component.translatable(translatable, arguments);
        if (componentHandler != null) {
            return componentHandler.handle(result);
        }

        if (context.method().getReturnType().isAssignableFrom(TranslatableComponent.class)) {
            return (R) result;
        }

        throw new IllegalStateException("No result handler found for return type: %s".formatted(type));
    }
}
