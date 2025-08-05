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
package io.github.namiuni.doburoku.reflect;

import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Method;
import net.kyori.adventure.translation.Translatable;
import org.junit.jupiter.api.Test;

class DefaultTranslatableResolverTest {

    interface Nest1 {
        interface Nest2 {
            interface Nest3 {
                void message();
            }
        }

        void longMethodName();
    }

    @Test
    void testResolve() throws NoSuchMethodException {

        final DefaultTranslatableResolver resolver = DefaultTranslatableResolver.create("test");

        final Method messageMethod = Nest1.Nest2.Nest3.class.getMethod("message");

        final Translatable result = resolver.resolve(messageMethod);

        assertEquals("test.nest1.nest2.nest3.message", result.translationKey());
    }

    @Test
    void testResolveLongMethodName() throws NoSuchMethodException {

        final DefaultTranslatableResolver resolver = DefaultTranslatableResolver.create("");
        final Method longMethod = Nest1.class.getMethod("longMethodName");
        final Translatable result = resolver.resolve(longMethod);

        assertEquals("nest1.long.method.name", result.translationKey());
    }
}
