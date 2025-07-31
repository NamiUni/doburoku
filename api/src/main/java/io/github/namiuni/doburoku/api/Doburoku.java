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

import io.github.namiuni.doburoku.api.provider.ArgumentsProvider;
import io.github.namiuni.doburoku.api.provider.KeyProvider;
import io.github.namiuni.doburoku.api.provider.ResultProvider;
import java.util.Objects;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TranslatableComponent;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class Doburoku<T> {
    private final KeyProvider<T> keyProvider;
    private final ArgumentsProvider<T> argumentsProvider;
    private final ResultProvider<T> resultProvider;

    private Doburoku(
            KeyProvider<T> keyProvider,
            ArgumentsProvider<T> argumentsProvider,
            ResultProvider<T> resultProvider
    ) {
        this.keyProvider = keyProvider;
        this.argumentsProvider = argumentsProvider;
        this.resultProvider = resultProvider;
    }

    public static <T> Doburoku<T> produce(
            final KeyProvider<T> keyProvider,
            final ArgumentsProvider<T> argumentsProvider,
            final ResultProvider<T> resultProvider) {
        return new Doburoku<>(keyProvider, argumentsProvider, resultProvider);
    }

    public <R> @Nullable R drunk(final T context) {
        final String key = this.keyProvider.get(context);
        final ComponentLike[] arguments = this.argumentsProvider.get(context);
        final TranslatableComponent result = Component.translatable(key, arguments);
        return this.resultProvider.get(context, result);
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        final Doburoku<?> doburoku = (Doburoku<?>) o;
        return Objects.equals(keyProvider, doburoku.keyProvider) &&
                Objects.equals(argumentsProvider, doburoku.argumentsProvider) &&
                Objects.equals(resultProvider, doburoku.resultProvider);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.keyProvider, this.argumentsProvider, this.resultProvider);
    }

    @Override
    public String toString() {
        return "Doburoku[" +
                "translationKeyProvider=" + this.keyProvider + ", " +
                "translationArgumentsProvider=" + this.argumentsProvider + ", " +
                "translationHandler=" + this.resultProvider + ']';
    }
}
