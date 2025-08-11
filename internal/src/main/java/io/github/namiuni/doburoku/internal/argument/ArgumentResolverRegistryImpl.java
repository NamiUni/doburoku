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
package io.github.namiuni.doburoku.internal.argument;

import io.github.namiuni.doburoku.api.argument.ArgumentResolverRegistry;
import io.github.namiuni.doburoku.api.argument.TranslationArgumentRenderer;
import io.github.namiuni.doburoku.api.argument.TranslationArgumentResolver;
import io.github.namiuni.doburoku.api.invocation.InvocationContext;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.jspecify.annotations.NullMarked;

/**
 * Thread-safe implementation of {@link TranslationArgumentResolver} and {@link ArgumentResolverRegistry}.
 *
 * <p>Renders method arguments into {@link ComponentLike} using a registry of type-specific renderers.
 * By default, {@link ComponentLike} values are passed through as-is, and other values are converted
 * via {@code Component.text(String.valueOf(value))}.</p>
 *
 * <p>This type is internal and may change without notice.</p>
 */
@NullMarked
public final class ArgumentResolverRegistryImpl implements TranslationArgumentResolver, ArgumentResolverRegistry {

    private static final TranslationArgumentRenderer<Object> DEFAULT_RENDERER = (parameter, argument) -> Component.text(String.valueOf(argument));
    private static final TranslationArgumentRenderer<ComponentLike> COMPONENT_RENDERER = (parameter, argument) -> argument;

    private final Map<Type, TranslationArgumentRenderer<?>> argumentRenderers = new ConcurrentHashMap<>();

    /**
     * Creates a registry.
     */
    public ArgumentResolverRegistryImpl() {
    }

    @Override
    public <T> ArgumentResolverRegistry plus(final Type type, final TranslationArgumentRenderer<T> renderer) {
        this.argumentRenderers.put(type, renderer);
        return this;
    }

    /**
     * Renders all arguments from the provided method context.
     *
     * @param context the method invocation context
     * @return an array of components aligned with the method's parameter order
     */
    @Override
    public ComponentLike[] resolve(final InvocationContext context) {
        final InvocationContext.Argument<?>[] arguments = context.arguments();
        final ComponentLike[] translations = new ComponentLike[arguments.length];

        for (int i = 0; i < arguments.length; i++) {
            final InvocationContext.Argument<?> argument = arguments[i];
            final Object value = argument.value();
            if (value == null) {
                translations[i] = Component.empty();
            } else {
                translations[i] = this.render(argument.parameter(), value);
            }
        }

        return translations;
    }

    private <T> ComponentLike render(final Parameter parameter, final T argument) {

        final Type type = parameter.getParameterizedType();

        @SuppressWarnings("unchecked") final TranslationArgumentRenderer<T> renderer = (TranslationArgumentRenderer<T>) Objects.requireNonNullElseGet(
                this.argumentRenderers.get(type),
                () -> {
                    if (ComponentLike.class.isAssignableFrom(argument.getClass())) {
                        return COMPONENT_RENDERER;
                    } else {
                        return DEFAULT_RENDERER;
                    }
                }
        );

        return renderer.render(parameter, argument);
    }
}
