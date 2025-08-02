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
package io.github.namiuni.doburoku.api;

import io.github.namiuni.doburoku.api.providers.ArgumentsProvider;
import io.github.namiuni.doburoku.api.providers.ResultProvider;
import io.github.namiuni.doburoku.api.providers.TranslatableProvider;
import java.util.Objects;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.translation.Translatable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * The main class for the Doburoku translation library, orchestrating the translation process.
 * <p>
 * Doburoku (濁酒) is a type of unrefined sake, and this class "brews" a translation
 * by combining a translation key, its arguments, and a final processing step.
 * It is instantiated using providers for each part of the translation process.
 *
 * @param <T> the type of the context object which provides information for translation
 */
@NullMarked
public final class Doburoku<T> {
    private final TranslatableProvider<T> translatableProvider;
    private final ArgumentsProvider<T> argumentsProvider;
    private final ResultProvider<T> resultProvider;

    /**
     * Constructs a new Doburoku instance.
     *
     * @param translatableProvider the provider for the translation key
     * @param argumentsProvider    the provider for the translation arguments
     * @param resultProvider       the provider that handles the final result
     */
    private Doburoku(
            final TranslatableProvider<T> translatableProvider,
            final ArgumentsProvider<T> argumentsProvider,
            final ResultProvider<T> resultProvider
    ) {
        this.translatableProvider = translatableProvider;
        this.argumentsProvider = argumentsProvider;
        this.resultProvider = resultProvider;
    }

    /**
     * Creates, or "brews," a new {@link Doburoku} instance with the specified providers.
     *
     * @param translatableProvider the provider for the {@link Translatable} key
     * @param argumentsProvider    the provider for the translation arguments
     * @param resultProvider       the provider that processes the final {@link net.kyori.adventure.text.TranslatableComponent}
     * @param <T>                  the type of the context object
     * @return a new {@link Doburoku} instance
     */
    public static <T> Doburoku<T> brew(
            final TranslatableProvider<T> translatableProvider,
            final ArgumentsProvider<T> argumentsProvider,
            final ResultProvider<T> resultProvider
    ) {
        return new Doburoku<>(translatableProvider, argumentsProvider, resultProvider);
    }

    /**
     * Executes the translation process, or gets "drunk," using the given context.
     * <p>
     * This method retrieves the translation key and arguments, then passes them to the
     * result provider to generate the final output.
     *
     * @param context the context object supplying data for the translation
     * @param <R>     the type of the returned result
     * @return the processed result, or {@code null} if the {@link ResultProvider} yields no result
     */
    public <R> @Nullable R drunk(final T context) {
        final Translatable key = this.translatableProvider.get(context);
        final ComponentLike[] arguments = this.argumentsProvider.get(context);
        return this.resultProvider.get(context, key, arguments);
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        final Doburoku<?> doburoku = (Doburoku<?>) o;
        return Objects.equals(this.translatableProvider, doburoku.translatableProvider) &&
                Objects.equals(this.argumentsProvider, doburoku.argumentsProvider) &&
                Objects.equals(this.resultProvider, doburoku.resultProvider);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.translatableProvider, this.argumentsProvider, this.resultProvider);
    }

    @Override
    public String toString() {
        return "Doburoku[" +
                "translationKeyProvider=" + this.translatableProvider + ", " +
                "translationArgumentsProvider=" + this.argumentsProvider + ", " +
                "translationHandler=" + this.resultProvider + ']';
    }
}
