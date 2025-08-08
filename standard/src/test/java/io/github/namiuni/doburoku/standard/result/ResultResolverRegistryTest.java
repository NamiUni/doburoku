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
package io.github.namiuni.doburoku.standard.result;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.github.namiuni.doburoku.common.DoburokuMethod;
import io.leangen.geantyref.TypeToken;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TranslatableComponent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

final class ResultResolverRegistryTest {

    private static final class Sample {
        @SuppressWarnings("unused")
        static TranslatableComponent returnsTranslatable() {
            throw new UnsupportedOperationException();
        }

        @SuppressWarnings("unused")
        static net.kyori.adventure.text.Component returnsComponent() {
            throw new UnsupportedOperationException();
        }

        @SuppressWarnings("unused")
        static String returnsString() {
            throw new UnsupportedOperationException();
        }

        @SuppressWarnings("unused")
        static Integer returnsInteger() {
            throw new UnsupportedOperationException();
        }

        @SuppressWarnings("unused")
        static List<String> returnsGenericList() {
            throw new UnsupportedOperationException();
        }
    }

    @SuppressWarnings("DataFlowIssue")
    private static DoburokuMethod contextFor(String methodName, Class<?> returnType) throws Exception {
        Method m = ResultResolverRegistryTest.Sample.class.getDeclaredMethod(methodName);
        // Ensure we got the intended method and return type
        if (!m.getReturnType().equals(returnType)) {
            throw new IllegalStateException("Unexpected method selected");
        }
        return DoburokuMethod.of(new Object(), m, null);
    }

    @Test
    @DisplayName("Uses registered transformer (by Class) with priority")
    void testTransformerByClassIsUsed() throws Exception {
        ResultResolverRegistry registry = new ResultResolverRegistry();

        AtomicReference<Method> seenMethod = new AtomicReference<>();
        AtomicReference<TranslatableComponent> seenComponent = new AtomicReference<>();

        registry.put(String.class, (method, component) -> {
            seenMethod.set(method);
            seenComponent.set(component);
            return "transformed";
        });

        DoburokuMethod ctx = contextFor("returnsString", String.class);
        String key = "my.key";
        ComponentLike[] args = new ComponentLike[] { Component.text("arg") };
        TranslatableComponent expected = Component.translatable(key, args);

        Object result = registry.resolve(ctx, key, args);

        assertEquals("transformed", result);
        assertSame(ctx.method(), seenMethod.get(), "Transformer should receive the same Method");
        assertEquals(expected, seenComponent.get(), "Transformer should receive the constructed TranslatableComponent");
    }

    @Test
    @DisplayName("Falls back to returning TranslatableComponent when return type is TranslatableComponent")
    void testFallbackToTranslatableWhenExact() throws Exception {
        ResultResolverRegistry registry = new ResultResolverRegistry();

        DoburokuMethod ctx = contextFor("returnsTranslatable", TranslatableComponent.class);
        String key = "fallback.key";
        ComponentLike[] args = new ComponentLike[] { Component.text("x") };
        TranslatableComponent expected = Component.translatable(key, args);

        Object result = registry.resolve(ctx, key, args);

        assertInstanceOf(TranslatableComponent.class, result);
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Falls back to returning TranslatableComponent when return type is Component (supertype)")
    void testFallbackToTranslatableWhenSupertype() throws Exception {
        ResultResolverRegistry registry = new ResultResolverRegistry();

        DoburokuMethod ctx = contextFor("returnsComponent", Component.class);
        String key = "fallback.super";
        ComponentLike[] args = new ComponentLike[] { Component.text("y") };
        TranslatableComponent expected = Component.translatable(key, args);

        Object result = registry.resolve(ctx, key, args);

        assertInstanceOf(TranslatableComponent.class, result, "Returned value should be a TranslatableComponent");
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Throws when no transformer is available and return type is not compatible")
    void testThrowsWhenNoTransformer() throws Exception {
        ResultResolverRegistry registry = new ResultResolverRegistry();

        DoburokuMethod ctx = contextFor("returnsInteger", Integer.class);
        String key = "no.handler";
        ComponentLike[] args = new ComponentLike[] { Component.text("z") };

        assertThrows(IllegalStateException.class, () -> registry.resolve(ctx, key, args));
    }

    @Test
    @DisplayName("Uses registered transformer (by TypeToken) for generic return type")
    void testTransformerByTypeTokenIsUsed() throws Exception {
        ResultResolverRegistry registry = new ResultResolverRegistry();

        registry.put(new TypeToken<>() {}, (method, component) -> List.of("ok"));

        DoburokuMethod ctx = contextFor("returnsGenericList", List.class);
        String key = "generic.key";
        ComponentLike[] args = new ComponentLike[] { Component.text("g") };

        Object result = registry.resolve(ctx, key, args);

        assertEquals(List.of("ok"), result);
    }
}
