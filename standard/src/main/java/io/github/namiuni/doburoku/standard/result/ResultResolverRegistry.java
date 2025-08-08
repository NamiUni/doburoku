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
package io.github.namiuni.doburoku.standard.result;

import io.github.namiuni.doburoku.common.DoburokuMethod;
import io.github.namiuni.doburoku.common.result.TranslationResultResolver;
import io.leangen.geantyref.TypeToken;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TranslatableComponent;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Default implementation of {@link TranslationResultResolver} that creates a
 * {@link TranslatableComponent} from a key and arguments, then adapts it via
 * a registered {@link TranslatableComponentTransformer} based on the method's return type.
 * <p>
 * Note: this class is not thread-safe.
 * </p>
 */
@NullMarked
public final class ResultResolverRegistry implements TranslationResultResolver {

    private final Map<Type, TranslatableComponentTransformer<?>> transformers = new HashMap<>();

    /**
     * Resolves a result for the given invocation by building a {@link TranslatableComponent}
     * and transforming it to the declared return type when a matching transformer exists.
     *
     * @param context the invocation context
     * @param key the translation key
     * @param arguments the rendered translation arguments
     * @param <R> the method's return type
     * @return the resolved result
     * @throws IllegalStateException if no transformer exists and the method does not return {@link TranslatableComponent}
     */
    @SuppressWarnings("unchecked")
    @Override
    public <R> R resolve(
            final DoburokuMethod context,
            final String key,
            final ComponentLike[] arguments
    ) {
        final Method method = context.method();
        final TranslatableComponent result = Component.translatable(key, arguments);
        final Type type = method.getGenericReturnType();
        final TranslatableComponentTransformer<R> transformer = (TranslatableComponentTransformer<R>) this.transformers.get(type);

        if (transformer != null) {
            return transformer.handle(method, result);
        }

        if (method.getReturnType().isAssignableFrom(TranslatableComponent.class)) {
            return (R) result;
        }

        throw new IllegalStateException("No result handler found for return type: %s".formatted(type));
    }

    /**
     * Registers a transformer for a raw return type.
     *
     * @param <T> the result type
     * @param type the raw class to associate with the transformer
     * @param transformer the transformer to register
     * @return the previous transformer for the type, or {@code null} if none
     */
    @SuppressWarnings("unchecked")
    public <T> @Nullable TranslatableComponentTransformer<T> put(final Class<T> type, final TranslatableComponentTransformer<T> transformer) {
        return (TranslatableComponentTransformer<T>) this.transformers.put(type, transformer);
    }

    /**
     * Registers a transformer for a generic return type.
     *
     * @param <T> the result type
     * @param type the generic type token
     * @param transformer the transformer to register
     * @return the previous transformer for the type, or {@code null} if none
     */
    @SuppressWarnings("unchecked")
    public <T> @Nullable TranslatableComponentTransformer<T> put(final TypeToken<T> type, final TranslatableComponentTransformer<T> transformer) {
        return (TranslatableComponentTransformer<T>) this.transformers.put(type.getType(), transformer);
    }
}
