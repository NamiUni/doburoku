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
package io.github.namiuni.doburoku.api.invocation;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Represents the captured method invocation context within Doburoku's pipeline.
 *
 * <p>Implementations provide access to the proxy instance, the reflected method, and its arguments.</p>
 */
@NullMarked
public interface InvocationContext {

    /**
     * The proxy instance on which the method was invoked.
     *
     * @return the proxy instance
     */
    Object proxy();

    /**
     * The reflected method being invoked.
     *
     * @return the reflected method
     */
    Method method();

    /**
     * The arguments of the invocation, aligned with the method's parameter list.
     *
     * @return the invocation arguments
     */
    Argument<?>[] arguments();

    /**
     * Represents a single method argument alongside its reflective parameter metadata.
     *
     * @param <T> compile-time type of the argument value
     */
    interface Argument<T> {

        /**
         * The formal parameter corresponding to this argument.
         *
         * @return the formal parameter
         */
        Parameter parameter();

        /**
         * The actual argument value; may be null.
         *
         * @return the argument value
         */
        @Nullable T value();
    }
}
