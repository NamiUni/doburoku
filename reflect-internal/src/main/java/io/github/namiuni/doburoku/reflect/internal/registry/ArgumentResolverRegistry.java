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

import io.github.namiuni.doburoku.api.providers.ArgumentsProvider;
import io.github.namiuni.doburoku.reflect.api.handlers.ArgumentResolver;
import io.github.namiuni.doburoku.reflect.internal.DoburokuMethod;
import io.leangen.geantyref.GenericTypeReflector;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.renderer.ComponentRenderer;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class ArgumentResolverRegistry implements ArgumentsProvider<DoburokuMethod> {

    private static final ArgumentResolver<Object> DEFAULT_RENDERER = value -> Component.text(String.valueOf(value));
    private static final ArgumentResolver<ComponentLike> COMPONENT_RENDERER = value -> value;

    private final TypedProviderRegistry<ArgumentResolver<?>> argumentResolvers;
    private final @Nullable ComponentRenderer<Parameter> componentTransformer;

    private final Map<Type, Class<?>> erasedTypeCache = new ConcurrentHashMap<>();

    public ArgumentResolverRegistry(
            final TypedProviderRegistry<ArgumentResolver<?>> argumentResolvers,
            final @Nullable ComponentRenderer<Parameter> componentTransformer
    ) {
        this.argumentResolvers = argumentResolvers;
        this.componentTransformer = componentTransformer;
    }

    @Override
    public ComponentLike[] get(final DoburokuMethod context) {
        final DoburokuMethod.Argument<?>[] arguments = context.arguments();
        final ComponentLike[] translations = new ComponentLike[arguments.length];

        for (int i = 0; i < arguments.length; i++) {
            final DoburokuMethod.Argument<?> argument = arguments[i];
            if (argument.value() == null) {
                translations[i] = Component.empty();
            } else {
                translations[i] = this.resolve(argument.parameter(), argument.value());
            }
        }

        return translations;
    }

    private <T> ComponentLike resolve(final Parameter parameter, final T value) {

        final Type type = parameter.getParameterizedType();
        this.validate(type, value);

        @SuppressWarnings("unchecked")
        final ArgumentResolver<T> resolver = (ArgumentResolver<T>) Objects.requireNonNullElseGet(
                this.argumentResolvers.find(type),
                () -> {
                    if (ComponentLike.class.isAssignableFrom(value.getClass())) {
                        return COMPONENT_RENDERER;
                    } else {
                        return DEFAULT_RENDERER;
                    }
                }
        );

        final ComponentLike result = resolver.resolve(value);
        if (this.componentTransformer == null) {
            return result;
        }

        return this.componentTransformer.render(result.asComponent(), parameter);
    }

    private <T> void validate(final Type targetType, final T value) {
        final Class<?> erasedTargetType = this.erasedTypeCache.computeIfAbsent(targetType, GenericTypeReflector::erase);
        final Class<?> boxedTargetType = (Class<?>) GenericTypeReflector.box(erasedTargetType);

        if (!boxedTargetType.isInstance(value)) {
            throw new IllegalArgumentException("Value of type %s is not assignable to expected type %s"
                    .formatted(value.getClass().getSimpleName(), targetType.getTypeName()));
        }
    }
}
