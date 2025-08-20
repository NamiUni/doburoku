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
package io.github.namiuni.doburoku.standard.argument;

import java.lang.reflect.Parameter;
import net.kyori.adventure.text.ComponentLike;
import org.jspecify.annotations.NullMarked;

/**
 * Post-processes rendered translation arguments.
 *
 * <p>Implementations may wrap or adjust the component based on {@link Parameter} metadata and should be side-effect-free.</p>
 */
@NullMarked
public interface TranslationArgumentTransformer {

    /**
     * Transforms a rendered argument component, optionally using parameter metadata.
     *
     * @param parameter the reflective parameter to which the argument belongs
     * @param argument  the already-rendered component for the argument
     * @return the transformed component
     */
    ComponentLike transform(Parameter parameter, ComponentLike argument);
}
