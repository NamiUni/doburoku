package io.github.namiuni.doburoku.reflect.core;

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

        final DefaultTranslatableResolver resolver = DefaultTranslatableResolver.create();
        final Method longMethod = Nest1.class.getMethod("longMethodName");
        final Translatable result = resolver.resolve(longMethod);

        assertEquals("nest1.long.method.name", result.translationKey());
    }
}
