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
package io.github.namiuni.doburoku.common;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Represents a captured method invocation within Doburoku's proxy pipeline.
 * <p>
 * This record bundles the proxy instance, the reflected method, and its arguments
 * (paired with their {@link java.lang.reflect.Parameter} metadata) for downstream processing.
 *
 * @param proxy     the proxy instance on which the method was invoked
 * @param method    the reflected method being invoked
 * @param arguments the arguments of the invocation, each coupled with its {@link Parameter}
 */
@NullMarked
@ApiStatus.Internal
public record DoburokuMethod(Object proxy, Method method, Argument<?>[] arguments) {

    /**
     * Represents a single method argument along with its reflective parameter metadata.
     *
     * @param <T>       the compile-time type of the argument value
     * @param parameter the formal {@link Parameter} corresponding to this argument
     * @param value     the actual argument value; may be {@code null}
     */
    @ApiStatus.Internal
    public record Argument<T>(Parameter parameter, @Nullable T value) {
    }

    /**
     * Creates a {@link DoburokuMethod} from the given proxy, method, and raw argument values.
     * <p>
     * The provided {@code args} are paired with the corresponding {@link Parameter}s
     * obtained from {@link Method#getParameters()}. The array is expected to have the same
     * length as the method's parameter list; individual values may be {@code null}.
     *
     * @param proxy  the proxy instance on which the invocation occurs
     * @param method the reflected method being invoked
     * @param args   the raw argument values corresponding to the method parameters; values may be {@code null}
     * @return a new {@code DoburokuMethod} encapsulating the invocation data
     */
    @ApiStatus.Internal
    public static DoburokuMethod of(final Object proxy, final Method method, final @Nullable Object[] args) {
        final Parameter[] parameters = method.getParameters();
        final Argument<?>[] doburokuArguments = new Argument<?>[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            doburokuArguments[i] = new Argument<>(parameters[i], args[i]);
        }

        return new DoburokuMethod(proxy, method, doburokuArguments);
    }
}
