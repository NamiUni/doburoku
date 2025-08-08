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
package io.github.namiuni.doburoku.standard.argument;

import io.github.namiuni.doburoku.common.DoburokuMethod;
import io.github.namiuni.doburoku.common.argument.TranslationArgumentResolver;
import io.leangen.geantyref.TypeToken;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Default implementation of {@link TranslationArgumentResolver} that renders method
 * arguments to {@link ComponentLike} instances using a registry of type-specific renderers
 * and an optional {@link TranslationArgumentTransformer}.
 *
 * <p>By default, {@link ComponentLike} values are passed through as-is, while other values
 * are converted via {@code Component.text(String.valueOf(value))}.</p>
 * <p>Note: this class is not thread-safe.</p>
 */
@NullMarked
public final class ArgumentResolverRegistry implements TranslationArgumentResolver {

    private static final TranslationArgumentRenderer<Object> DEFAULT_RENDERER = argument -> Component.text(String.valueOf(argument));
    private static final TranslationArgumentRenderer<ComponentLike> COMPONENT_RENDERER = argument -> argument;

    private final Map<Type, TranslationArgumentRenderer<?>> argumentRenderers = new HashMap<>();
    private final @Nullable TranslationArgumentTransformer argumentTransformer;

    /**
     * Creates a registry with default behavior and no transformer.
     */
    public ArgumentResolverRegistry() {
        this.argumentTransformer = null;
    }

    /**
     * Creates a registry with the given transformer.
     *
     * @param transformer the transformer to use, or {@code null} for default behavior
     */
    public ArgumentResolverRegistry(final TranslationArgumentTransformer transformer) {
        this.argumentTransformer = transformer;
    }

    /**
     * Renders all arguments from the provided method context.
     *
     * @param context the method invocation context
     * @return an array of components aligned with the method's parameter order
     */
    @Override
    public ComponentLike[] resolve(final DoburokuMethod context) {
        final DoburokuMethod.Argument<?>[] arguments = context.arguments();
        final ComponentLike[] translations = new ComponentLike[arguments.length];

        for (int i = 0; i < arguments.length; i++) {
            final DoburokuMethod.Argument<?> argument = arguments[i];
            if (argument.value() == null) {
                translations[i] = Component.empty();
            } else {
                translations[i] = this.render(argument.parameter(), argument.value());
            }
        }

        return translations;
    }

    private <T> ComponentLike render(final Parameter parameter, final T argument) {

        final Type type = parameter.getParameterizedType();

        @SuppressWarnings("unchecked")
        final TranslationArgumentRenderer<T> renderer = (TranslationArgumentRenderer<T>) Objects.requireNonNullElseGet(
                this.argumentRenderers.get(type),
                () -> {
                    if (ComponentLike.class.isAssignableFrom(argument.getClass())) {
                        return COMPONENT_RENDERER;
                    } else {
                        return DEFAULT_RENDERER;
                    }
                }
        );

        final ComponentLike component = renderer.render(argument);
        if (this.argumentTransformer == null) {
            return component;
        } else {
            return this.argumentTransformer.transform(parameter, component);
        }
    }

    /**
     * Registers a renderer for the given raw type.
     *
     * @param <T>      the type to render
     * @param type     the raw class to associate with the renderer
     * @param renderer the renderer implementation
     * @return the previous renderer for the type, or {@code null} if none
     */
    @SuppressWarnings("unchecked")
    public <T> @Nullable TranslationArgumentRenderer<T> put(final Class<T> type, final TranslationArgumentRenderer<T> renderer) {
        return (TranslationArgumentRenderer<T>) this.argumentRenderers.put(type, renderer);
    }

    /**
     * Registers a renderer for the given generic type.
     *
     * @param <T>      the type to render
     * @param type     the generic type token
     * @param renderer the renderer implementation
     * @return the previous renderer for the type, or {@code null} if none
     */
    @SuppressWarnings("unchecked")
    public <T> @Nullable TranslationArgumentRenderer<T> put(final TypeToken<T> type, final TranslationArgumentRenderer<T> renderer) {
        return (TranslationArgumentRenderer<T>) this.argumentRenderers.put(type.getType(), renderer);
    }
}
