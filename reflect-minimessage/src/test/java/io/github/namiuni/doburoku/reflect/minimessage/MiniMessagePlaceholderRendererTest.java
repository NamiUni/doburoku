package io.github.namiuni.doburoku.reflect.minimessage;

import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.translation.Argument;
import org.junit.jupiter.api.Test;

class MiniMessagePlaceholderRendererTest {

    interface SimpleInterface {
        void message(Object argument);
    }

    @Test
    void testTransformer() throws NoSuchMethodException {

        @SuppressWarnings("OptionalGetWithoutIsPresent")
        final Parameter parameter = Arrays.stream(SimpleInterface.class.getMethod("message", Object.class).getParameters())
                .findFirst()
                .get();
        final Component component = Component.text("convoy");

        final MiniMessagePlaceholderRenderer transformer = MiniMessagePlaceholderRenderer.instance();
        final Component transformed = transformer.render(component, parameter);

        assertEquals(Argument.component("argument", Component.text("convoy")) ,transformed);
    }
}
