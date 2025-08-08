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

import io.github.namiuni.doburoku.common.DoburokuMethod;
import io.github.namiuni.doburoku.common.argument.TranslationArgumentResolver;
import io.github.namiuni.doburoku.common.key.TranslationKeyResolver;
import io.github.namiuni.doburoku.common.result.TranslationResultResolver;
import net.kyori.adventure.text.ComponentLike;
import org.jspecify.annotations.NullMarked;

@NullMarked
final class DoburokuDrunkard {
    private final TranslationKeyResolver keyResolver;
    private final TranslationArgumentResolver argumentResolver;
    private final TranslationResultResolver resultResolver;

    DoburokuDrunkard(
            final TranslationKeyResolver keyResolver,
            final TranslationArgumentResolver argumentResolver,
            final TranslationResultResolver resultResolver
    ) {
        this.keyResolver = keyResolver;
        this.argumentResolver = argumentResolver;
        this.resultResolver = resultResolver;
    }

    public <R> R drunk(final DoburokuMethod doburokuMethod) {
        final String key = this.keyResolver.resolve(doburokuMethod);
        final ComponentLike[] translationArguments = this.argumentResolver.resolve(doburokuMethod);
        return this.resultResolver.resolve(doburokuMethod, key, translationArguments);
    }
}
