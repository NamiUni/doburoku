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
package io.github.namiuni.doburoku.core;

import io.github.namiuni.doburoku.core.argument.ArgumentResolver;
import io.github.namiuni.doburoku.core.key.KeyResolver;
import io.github.namiuni.doburoku.core.result.ResultHandler;
import java.util.Objects;
import net.kyori.adventure.text.TranslatableComponent;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * A factory for creating translation service proxies.
 *
 * <p>This class provides a builder to configure and create a dynamic proxy
 * implementation of a given service interface. The generated proxy translates
 * method calls into {@link TranslatableComponent}s.</p>
 */
@NullMarked
public final class TranslationService {
    private TranslationService() {
    }

    /**
     * Creates a new builder for a {@link TranslationService}.
     *
     * @param serviceInterface the interface to be implemented by the service proxy
     * @param <I>              the type of the service interface
     * @return a new {@link Builder} instance
     */
    public static <I> Builder<I> builder(final Class<I> serviceInterface) {
        return new Builder<>(serviceInterface);
    }

    /**
     * A builder for creating {@link TranslationService} proxies.
     *
     * @param <I> the type of the service interface
     */
    public static final class Builder<I> {

        private final Class<I> serviceInterface;
        private @Nullable KeyResolver keyResolver;
        private @Nullable ArgumentResolver argumentResolver;
        private @Nullable ResultHandler resultHandler;

        private Builder(final Class<I> serviceInterface) {
            this.serviceInterface = serviceInterface;
        }

        /**
         * Sets the {@link KeyResolver} to be used for generating translation keys.
         *
         * @param keyResolver the key resolver
         * @return this builder
         */
        public Builder<I> key(final KeyResolver keyResolver) {
            this.keyResolver = keyResolver;
            return this;
        }

        /**
         * Sets the {@link ArgumentResolver} to be used for generating translatable component arguments.
         *
         * @param argumentResolver the argument resolver
         * @return this builder
         */
        public Builder<I> argument(final ArgumentResolver argumentResolver) {
            this.argumentResolver = argumentResolver;
            return this;
        }

        /**
         * Sets the {@link ResultHandler} to be used for handling result component.
         *
         * @param resultHandler the resultHandler
         * @return this builder
         */
        public Builder<I> result(final ResultHandler resultHandler) {
            this.resultHandler = resultHandler;
            return this;
        }

        /**
         * Builds the translation service proxy instance.
         *
         * @return a new proxy instance of the service interface
         */
        public I build() {
            Objects.requireNonNull(keyResolver, "keyResolver");
            Objects.requireNonNull(argumentResolver, "argumentResolver");
            Objects.requireNonNull(resultHandler, "resultHandler");
            final TranslationServiceFactory factory = new TranslationServiceFactory(
                    this.argumentResolver,
                    this.resultHandler,
                    this.keyResolver,
                    ""
            );

            return factory.create(this.serviceInterface);
        }
    }
}
