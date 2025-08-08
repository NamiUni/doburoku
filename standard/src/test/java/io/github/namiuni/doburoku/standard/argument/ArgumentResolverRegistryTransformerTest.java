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

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

final class ArgumentResolverRegistryTransformerTest {

    private ArgumentResolverRegistry registryWithTransformer;
    private Method renderMethod;
    private Parameter stringParam;

    @BeforeEach
    void setUp() throws Exception {
        Constructor<ArgumentResolverRegistry> constructor = ArgumentResolverRegistry.class.getDeclaredConstructor(TranslationArgumentTransformer.class);
        constructor.setAccessible(true);

        TranslationArgumentTransformer transformer = (parameter, argument) -> Component.text("transformed");
        this.registryWithTransformer = constructor.newInstance(transformer);

        this.renderMethod = ArgumentResolverRegistry.class.getDeclaredMethod("render", Parameter.class, Object.class);
        this.renderMethod.setAccessible(true);

        Method m = ArgumentResolverRegistryTransformerTest.class.getDeclaredMethod(
                "sampleMethod", String.class, ComponentLike.class
        );
        this.stringParam = m.getParameters()[0];
    }
    
    @SuppressWarnings("unused")
    private static void sampleMethod(String s, ComponentLike c) {
        // no-op
    }

    @Test
    @DisplayName("Transformer is applied to the rendered result")
    void testTransformerApplied() throws Exception {
        Object result = this.renderMethod.invoke(this.registryWithTransformer, this.stringParam, "abc");
        assertEquals(Component.text("transformed"), result);
    }
}
