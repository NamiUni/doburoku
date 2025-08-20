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
package io.github.namiuni.doburoku.standard;

import io.github.namiuni.doburoku.api.argument.TranslationArgumentResolver;
import io.github.namiuni.doburoku.api.key.TranslationKeyResolver;
import io.github.namiuni.doburoku.api.result.TranslationResultResolver;
import io.github.namiuni.doburoku.internal.DoburokuDrunkard;
import io.github.namiuni.doburoku.internal.DoburokuProxyFactory;
import io.github.namiuni.doburoku.spi.Doburoku;
import io.github.namiuni.doburoku.standard.argument.TranslationArgumentRegistry;
import io.github.namiuni.doburoku.standard.argument.TranslationArgumentTransformer;
import io.github.namiuni.doburoku.standard.key.AnnotationKeyResolver;
import io.github.namiuni.doburoku.standard.result.TranslationResultResolverRegistry;
import java.util.Objects;
import java.util.function.Consumer;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TranslatableComponent;
import org.jspecify.annotations.NullMarked;

/**
 * Factory for creating dynamic proxies of service interfaces.
 *
 * <p>Provides a standard builder to configure how method calls on a target interface are processed
 * into {@link TranslatableComponent} instances. Unset options fall back to sensible defaults.</p>
 *
 * @param <I> the service interface type
 */
@NullMarked
public final class DoburokuStandard<I> extends Doburoku<DoburokuStandard<I>, I> {

    private DoburokuStandard(final Class<I> serviceInterface) {
        super(serviceInterface);
    }

    /**
     * Starts the builder for the given service interface.
     *
     * @param <I>              the service interface type
     * @param serviceInterface the interface class to proxy
     * @return a new builder instance
     * @throws IllegalArgumentException if the given class is not an interface
     */
    public static <I> DoburokuStandard<I> of(final Class<I> serviceInterface) {
        return new DoburokuStandard<>(serviceInterface);
    }

    /**
     * Configures an {@link TranslationArgumentRegistry} using a consumer function.
     *
     * @param configurator a consumer that configures the registry
     * @return this builder
     */
    public DoburokuStandard<I> argument(final Consumer<TranslationArgumentRegistry> configurator) {
        final TranslationArgumentRegistry registry = new TranslationArgumentRegistry();
        configurator.accept(registry);
        return this.argument(registry);
    }

    /**
     * Configures an {@link TranslationArgumentRegistry} with a transformer and additional configuration.
     *
     * @param configurator a consumer that configures the registry
     * @param transformer  the argument transformer to apply to all rendered components
     * @return this builder
     */
    public DoburokuStandard<I> argument(
            final Consumer<TranslationArgumentRegistry> configurator,
            final TranslationArgumentTransformer transformer
    ) {
        final TranslationArgumentRegistry registry = new TranslationArgumentRegistry();
        configurator.accept(registry);

        return this.argument(context -> {
            final ComponentLike[] components = registry.resolve(context);
            for (int i = 0; i < components.length; i++) {
                components[i] = transformer.transform(context.arguments()[i].parameter(), components[i]);
            }
            return components;
        });
    }

    /**
     * Configures a {@link TranslationResultResolverRegistry} using a consumer function.
     *
     * @param configurator a consumer that configures the registry
     * @return this builder
     */
    public DoburokuStandard<I> result(final Consumer<TranslationResultResolverRegistry> configurator) {
        final TranslationResultResolverRegistry registry = new TranslationResultResolverRegistry();
        configurator.accept(registry);
        return this.result(registry);
    }

    @Override
    public I brew() {
        final Class<I> service = Objects.requireNonNull(this.serviceInterface);
        final TranslationKeyResolver key = Objects.requireNonNullElse(this.keyResolver, new AnnotationKeyResolver());
        final TranslationArgumentResolver argument = Objects.requireNonNullElse(this.argumentResolver, new TranslationArgumentRegistry());
        final TranslationResultResolver result = Objects.requireNonNullElse(this.resultResolver, new TranslationResultResolverRegistry());

        final DoburokuDrunkard drunkard = new DoburokuDrunkard(key, argument, result);
        return DoburokuProxyFactory.of(drunkard).create(service);
    }
}
