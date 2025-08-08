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

import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

final class MiniMessageArgumentRendererTest {

//    private static final class Sample {
//        @SuppressWarnings("unused")
//        static void annotated(@Name("greeting") String userName) {
//            // no-op
//        }
//
//        @SuppressWarnings("unused")
//        static void noName(String camelCaseParam) {
//            // no-op
//        }
//    }

    @Test
    @DisplayName("instance() returns a singleton")
    void instanceIsSingleton() {
        MiniMessageArgumentRenderer a = MiniMessageArgumentRenderer.instance();
        MiniMessageArgumentRenderer b = MiniMessageArgumentRenderer.instance();
        assertSame(a, b, "instance() should return the same singleton");
    }

//    @Test
//    @DisplayName("transform() uses @Name value when present")
//    void transformUsesAnnotationName() throws Exception {
//        MiniMessageArgumentRenderer renderer = MiniMessageArgumentRenderer.instance();
//
//        Method method = Sample.class.getDeclaredMethod("annotated", String.class);
//        Parameter parameter = method.getParameters()[0];
//
//        ComponentLike value = Component.text("hello");
//        ComponentLike actual = renderer.transform(parameter, value);
//        ComponentLike expected = Argument.component("greeting", value);
//
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    @DisplayName("transform() falls back to parameter name in snake_case when @Name is absent")
//    void transformUsesParameterNameFallback() throws Exception {
//        MiniMessageArgumentRenderer renderer = MiniMessageArgumentRenderer.instance();
//
//        Method m = Sample.class.getDeclaredMethod("noName", String.class);
//        Parameter p = m.getParameters()[0];
//
//        String expectedName = Pattern.compile("(?=\\p{Upper})")
//                .splitAsStream(p.getName())
//                .map(String::toLowerCase)
//                .collect(Collectors.joining("_"));
//
//        ComponentLike value = Component.text("value");
//        ComponentLike actual = renderer.transform(p, value);
//        ComponentLike expected = Argument.component(expectedName, value);
//
//        assertEquals(expected, actual);
//    }
}
