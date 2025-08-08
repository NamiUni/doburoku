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
