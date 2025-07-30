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
import java.lang.reflect.Proxy;
import org.jspecify.annotations.NullMarked;

@NullMarked
final class TranslationServiceFactory {
    private final ArgumentResolver argumentResolver;
    private final ResultHandler resultHandler;
    private final KeyResolver keyResolver;
    private final String parentKey;

    TranslationServiceFactory(
            final ArgumentResolver argumentResolver,
            final ResultHandler resultHandler,
            final KeyResolver keyResolver,
            final String parentKey
    ) {
        this.argumentResolver = argumentResolver;
        this.resultHandler = resultHandler;
        this.keyResolver = keyResolver;
        this.parentKey = parentKey;
    }

    <I> I create(final Class<I> serviceInterface) {
        final TranslationInvocationHandler rootHandler = new TranslationInvocationHandler(
                this,
                serviceInterface,
                this.keyResolver,
                this.parentKey
        );

        @SuppressWarnings("unchecked") final I proxy = (I) Proxy.newProxyInstance(
                serviceInterface.getClassLoader(),
                new Class<?>[] {serviceInterface},
                rootHandler
        );
        return proxy;
    }

    ArgumentResolver argumentResolver() {
        return this.argumentResolver;
    }

    ResultHandler resultHandler() {
        return this.resultHandler;
    }
}
