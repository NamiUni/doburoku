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
package io.github.namiuni.doburoku.api.exception;

import org.jspecify.annotations.NullMarked;

/**
 * Indicates that a translation key could not be resolved for a target method.
 * Unchecked exception indicating that a translation key could not be resolved during a translation
 * method invocation.
 *
 * <p>This is typically thrown when no translation mapping exists for the requested key or when
 * the key cannot be derived for the invoked method.</p>
 */
@NullMarked
public final class MissingTranslationKeyException extends RuntimeException {
    
    /**
     * Creates a new exception with the specified error message.
     *
     * @param message the detail message explaining why the key could not be resolved
     */
    public MissingTranslationKeyException(final String message) {
        super(message);
    }

    /**
     * Creates a new exception with the specified error message and underlying cause.
     *
     * @param message the detail message explaining why the key could not be resolved
     * @param cause the underlying cause of this exception
     */
    public MissingTranslationKeyException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
