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
