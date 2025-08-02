package io.github.namiuni.doburoku.reflect.messageformat;

import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Method;
import net.kyori.adventure.translation.Translatable;
import org.junit.jupiter.api.Test;

class StandardTranslatableResolverTest {

    interface Nest1 {
        interface Nest2 {
            interface Nest3 {
                void message();
            }
        }
    }

    @Test
    void testResolve() throws NoSuchMethodException {

        final StandardTranslatableResolver resolver = StandardTranslatableResolver.create("test");

        final Method messageMethod = Nest1.Nest2.Nest3.class.getMethod("message");

        final Translatable result = resolver.resolve(messageMethod);

        assertEquals("test.nest1.nest2.nest3.message", result.translationKey());
    }
}
