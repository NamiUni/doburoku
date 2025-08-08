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
package io.github.namiuni.doburoku.standard.key;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.github.namiuni.doburoku.common.DoburokuMethod;
import io.github.namiuni.doburoku.standard.annotation.Key;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

final class AnnotationKeyResolverTest {

    @SuppressWarnings("unused")
    private static final class Sample {
        @Key("hello.world")
        static void annotated() {
            // no-op
        }

        static void notAnnotated() {
            // no-op
        }
    }

    private static DoburokuMethod contextFor(final String methodName) throws Exception {
        final Method method = Sample.class.getDeclaredMethod(methodName);
        return DoburokuMethod.of(new Object(), method, new Object[0]);
    }

    @Test
    @DisplayName("Resolves key from @Key annotation on method")
    void resolvesKeyFromAnnotation() throws Exception {
        AnnotationKeyResolver resolver = new AnnotationKeyResolver();
        DoburokuMethod ctx = contextFor("annotated");

        String key = resolver.resolve(ctx);

        assertEquals("hello.world", key);
    }

    @Test
    @DisplayName("Caches resolved key per Method (map size remains 1 on repeated resolves)")
    @SuppressWarnings("unchecked")
    void cachesResolvedKey() throws Exception {
        AnnotationKeyResolver resolver = new AnnotationKeyResolver();
        DoburokuMethod ctx = contextFor("annotated");

        // Resolve multiple times for the same method
        resolver.resolve(ctx);
        resolver.resolve(ctx);

        // Inspect private cache via reflection to assert it holds a single entry
        Field cacheField = AnnotationKeyResolver.class.getDeclaredField("cache");
        cacheField.setAccessible(true);
        Map<Method, String> cache = (Map<Method, String>) cacheField.get(resolver);

        assertEquals(1, cache.size(), "Cache should contain a single entry for the method");
    }

    @Test
    @DisplayName("Throws when method is missing @Key annotation")
    void throwsWhenMissingAnnotation() throws Exception {
        AnnotationKeyResolver resolver = new AnnotationKeyResolver();
        DoburokuMethod ctx = contextFor("notAnnotated");

        assertThrows(IllegalStateException.class, () -> resolver.resolve(ctx));
    }
}
