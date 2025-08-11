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
package io.github.namiuni.doburoku.internal.result;

import io.github.namiuni.doburoku.api.invocation.InvocationContext;
import io.github.namiuni.doburoku.api.result.TranslationResultResolver;
import io.github.namiuni.doburoku.spi.result.ResultResolverRegistry;
import io.github.namiuni.doburoku.spi.result.TranslatableComponentTransformer;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TranslatableComponent;
import org.jspecify.annotations.NullMarked;

/**
 * Default implementation of {@link TranslationResultResolver}.
 *
 * <p>Builds a {@link TranslatableComponent} from a key and arguments, then adapts it via a
 * registered {@link TranslatableComponentTransformer} based on the method's return type.
 * This class is not thread-safe.</p>
 *
 * <p>This type is internal and may change without notice.</p>
 */
@NullMarked
public final class ResultResolverRegistryImpl implements TranslationResultResolver, ResultResolverRegistry {

    private final Map<Type, TranslatableComponentTransformer<?>> transformers = new HashMap<>();

    /**
     * Creates a new instance.
     */
    public ResultResolverRegistryImpl() {
    }

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
            final InvocationContext context,
            final String key,
            final ComponentLike[] arguments
    ) {
        final Method method = context.method();
        final TranslatableComponent result = Component.translatable(key, arguments);
        final Type type = method.getGenericReturnType();
        final TranslatableComponentTransformer<R> transformer = (TranslatableComponentTransformer<R>) this.transformers.get(type);

        if (transformer != null) {
            return transformer.transform(method, result);
        }

        if (method.getReturnType().isAssignableFrom(TranslatableComponent.class)) {
            return (R) result;
        }

        throw new IllegalStateException("No result handler found for return type: %s".formatted(type));
    }

    @Override
    public <T> ResultResolverRegistry plus(final Type type, final TranslatableComponentTransformer<T> transformer) {
        this.transformers.put(type, transformer);
        return this;
    }
}
