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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

final class ArgumentResolverRegistryRenderTest {

    private ArgumentResolverRegistry registry;
    private Method renderMethod;
    private Parameter stringParam;
    private Parameter integerParam;
    private Parameter componentLikeParam;

    @BeforeEach
    void setUp() throws Exception {
        this.registry = new ArgumentResolverRegistry();

        this.renderMethod = ArgumentResolverRegistry.class.getDeclaredMethod("render", Parameter.class, Object.class);
        this.renderMethod.setAccessible(true);

        Method m = ArgumentResolverRegistryRenderTest.class.getDeclaredMethod("sampleMethod", String.class, Integer.class, ComponentLike.class);
        Parameter[] params = m.getParameters();
        this.stringParam = params[0];
        this.integerParam = params[1];
        this.componentLikeParam = params[2];
    }

    @SuppressWarnings("unused")
    private static void sampleMethod(String s, Integer i, ComponentLike c) {
        // no-op
    }

    @Test
    @DisplayName("Non-ComponentLike defaults to text component via String.valueOf")
    void testDefaultRenderingOfNonComponent() throws Exception {
        Object result = this.renderMethod.invoke(this.registry, this.stringParam, "hello");
        assertEquals(Component.text("hello"), result);
    }

    @Test
    @DisplayName("ComponentLike is passed through (same instance)")
    void testComponentLikePassThrough() throws Exception {
        ComponentLike input = Component.text("world");
        Object result = this.renderMethod.invoke(this.registry, this.componentLikeParam, input);
        assertSame(input, result, "ComponentLike should be returned as is");
    }

    @Test
    @DisplayName("Registered renderer (by Class) is used with priority")
    void testRegisteredRendererByClass() throws Exception {
        this.registry.put(Integer.class, Component::text);
        Object result = this.renderMethod.invoke(this.registry, this.integerParam, 7);
        assertEquals(Component.text("7"), result);
    }
}
