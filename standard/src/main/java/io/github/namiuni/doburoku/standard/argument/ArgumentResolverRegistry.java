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

@NullMarked
public final class ArgumentResolverRegistry implements TranslationArgumentResolver {

    private static final TranslationArgumentRenderer<Object> DEFAULT_RENDERER = argument -> Component.text(String.valueOf(argument));
    private static final TranslationArgumentRenderer<ComponentLike> COMPONENT_RENDERER = argument -> argument;

    private final Map<Type, TranslationArgumentRenderer<?>> argumentRenderers = new HashMap<>();
    private final @Nullable TranslationArgumentTransformer argumentTransformer;

    public ArgumentResolverRegistry() {
        this.argumentTransformer = null;
    }

    private ArgumentResolverRegistry(final TranslationArgumentTransformer transformer) {
        this.argumentTransformer = transformer;
    }

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

    @SuppressWarnings("unchecked")
    public <T> @Nullable TranslationArgumentRenderer<T> put(final Class<T> type, final TranslationArgumentRenderer<T> renderer) {
        return (TranslationArgumentRenderer<T>) this.argumentRenderers.put(type, renderer);
    }

    @SuppressWarnings("unchecked")
    public <T> @Nullable TranslationArgumentRenderer<T> put(final TypeToken<T> type, final TranslationArgumentRenderer<T> renderer) {
        return (TranslationArgumentRenderer<T>) this.argumentRenderers.put(type.getType(), renderer);
    }
}
