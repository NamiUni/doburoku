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
