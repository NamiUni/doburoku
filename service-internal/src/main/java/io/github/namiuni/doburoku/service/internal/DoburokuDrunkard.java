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
package io.github.namiuni.doburoku.service.internal;

import io.github.namiuni.doburoku.service.internal.providers.ArgumentsProvider;
import io.github.namiuni.doburoku.service.internal.providers.ResultProvider;
import io.github.namiuni.doburoku.service.internal.providers.TranslatableProvider;
import java.util.Objects;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.translation.Translatable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class DoburokuDrunkard<T> {
    private final TranslatableProvider<T> translatableProvider;
    private final ArgumentsProvider<T> argumentsProvider;
    private final ResultProvider<T> resultProvider;

    /**
     * Constructs a new DoburokuDrunkard instance.
     *
     * @param translatableProvider the provider for the translation key
     * @param argumentsProvider    the provider for the translation arguments
     * @param resultProvider       the provider that handles the final result
     */
    private DoburokuDrunkard(
            final TranslatableProvider<T> translatableProvider,
            final ArgumentsProvider<T> argumentsProvider,
            final ResultProvider<T> resultProvider
    ) {
        this.translatableProvider = translatableProvider;
        this.argumentsProvider = argumentsProvider;
        this.resultProvider = resultProvider;
    }

    public static <T> DoburokuDrunkard<T> brew(
            final TranslatableProvider<T> translatableProvider,
            final ArgumentsProvider<T> argumentsProvider,
            final ResultProvider<T> resultProvider
    ) {
        return new DoburokuDrunkard<>(translatableProvider, argumentsProvider, resultProvider);
    }

    public <R> @Nullable R drunk(final T context) {
        final Translatable key = this.translatableProvider.get(context);
        final ComponentLike[] arguments = this.argumentsProvider.get(context);
        return this.resultProvider.get(context, key, arguments);
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        final DoburokuDrunkard<?> doburokuDrunkard = (DoburokuDrunkard<?>) o;
        return Objects.equals(this.translatableProvider, doburokuDrunkard.translatableProvider) &&
                Objects.equals(this.argumentsProvider, doburokuDrunkard.argumentsProvider) &&
                Objects.equals(this.resultProvider, doburokuDrunkard.resultProvider);
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
