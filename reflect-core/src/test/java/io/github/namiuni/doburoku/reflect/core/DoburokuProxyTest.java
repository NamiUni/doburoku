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
package io.github.namiuni.doburoku.reflect.core;

import static org.junit.jupiter.api.Assertions.*;
import io.leangen.geantyref.TypeToken;
import java.io.PrintStream;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.TranslationArgument;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class DoburokuProxyTest {

    interface SimpleService {
        TranslatableComponent welcomeMessage(String name, int level);

        default String defaultMethod() {
            return "default-result";
        }
    }

    @Nested
    @DisplayName("Core Functionality Tests")
    class Core {

        @Test
        @DisplayName("Should create a translatable component with default settings")
        void testBasic() {
            final SimpleService service = DoburokuProxy
                    .from(SimpleService.class)
                    .translatable(method -> method::getName)
                    .brew();

            final TranslatableComponent result = service.welcomeMessage("Test", 99);

            // Verify
            assertEquals("welcomeMessage", result.key());
            assertEquals(TranslationArgument.component(Component.text("Test")), result.arguments().getFirst());
            assertEquals(TranslationArgument.component(Component.text(99)), result.arguments().getLast());
        }

        @Test
        @DisplayName("Should handle default interface methods")
        void testDefaultMethod() {
            // Setup
            final SimpleService service = DoburokuProxy
                    .from(SimpleService.class)
                    .translatable(method -> method::getName)
                    .brew();

            // Execute
            String result = service.defaultMethod();

            // Verify
            assertEquals("default-result", result);
        }
    }

    static class User {
        final String name;

        User(final String name) {
            this.name = name;
        }

        String name() {
            return name;
        }

        @Override
        public String toString() {
            return "User-" + name;
        }

        @Override
        public boolean equals(final Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            final User user = (User) o;
            return Objects.equals(name, user.name);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(name);
        }
    }

    static class Admin extends User {
        Admin(final String name) {
            super(name);
        }
    }

    interface ArgumentService {
        TranslatableComponent withCustom(User user);
        TranslatableComponent withGeneric(List<String> strings);
        TranslatableComponent withPrioritized(Admin admin);
    }

    @Nested
    @DisplayName("Argument Resolver Tests")
    class ArgumentResolver {

        @Test
        @DisplayName("Should use custom argument resolver for a specific type")
        void testCustom() {
            // Setup
            final ArgumentService service = DoburokuProxy
                    .from(ArgumentService.class)
                    .translatable(method -> method::getName)
                    .argument(resolvers -> resolvers.add(User.class, user -> Component.text(user.name(), NamedTextColor.GREEN)))
                    .brew();

            // Execute
            final TranslatableComponent result = service.withCustom(new User("Alice"));

            // Verify
            assertEquals(1, result.arguments().size());
            assertEquals(TranslationArgument.component(Component.text("Alice", NamedTextColor.GREEN)), result.arguments().getFirst());
        }

        @Test
        @DisplayName("Should use generic argument resolver for a specific type")
        void testGeneric() {
            // Setup
            final ArgumentService service = DoburokuProxy
                    .from(ArgumentService.class)
                    .translatable(method -> method::getName)
                    .argument(resolvers -> resolvers.add(new TypeToken<List<String>>() { }, strings -> String
                            .join(", ", strings)
                            .transform(Component::text)))
                    .brew();

            // Execute
            final List<String> moonshines = List.of("Moonshine", "Doburoku");
            final TranslatableComponent result = service.withGeneric(moonshines);

            // Verify
            assertEquals(TranslationArgument.component(Component.text("Moonshine, Doburoku")), result.arguments().getFirst());
        }

        @Test
        @DisplayName("Should respect argument resolver priority")
        void testPriority() {
            // Setup
            final ArgumentService service = DoburokuProxy
                    .from(ArgumentService.class)
                    .translatable(method -> method::getName)
                    .argument(resolvers -> resolvers
                            .add(User.class, user -> Component.text("User: " + user.name()), 0)
                            .add(Admin.class, admin -> Component.text("Admin: " + admin.name()), 10))
                    .brew();

            // Execute
            final TranslatableComponent result = service.withPrioritized(new Admin("Bob"));

            // Verify
            assertEquals(TranslationArgument.component(Component.text("Admin: Bob")), result.arguments().getFirst());
        }

        @Test
        @DisplayName("Should apply transformation to all resolved arguments")
        void testTransformer() {
            // Setup
            final Style wrapperStyle = Style.style(NamedTextColor.RED);
            final SimpleService service = DoburokuProxy
                    .from(SimpleService.class)
                    .translatable(method -> method::getName)
                    .argument((component, parameter) -> Component.text().append(component).style(wrapperStyle).build())
                    .brew();

            // Execute
            final TranslatableComponent result = service.welcomeMessage("Test", 99);

            // Verify
            assertEquals(Style.style(NamedTextColor.RED), result.arguments().getFirst().asComponent().style());
            assertEquals(Style.style(NamedTextColor.RED), result.arguments().getLast().asComponent().style());
        }
    }

    interface ResultService {
        Void withVoid();

        Consumer<PrintStream> withGeneric();

        Admin withPrioritized();
    }

    @Nested
    @DisplayName("Component Handler Tests")
    class ComponentHandler {

        @Test
        @DisplayName("Should use void result handler for a component")
        void testVoid() {
            // Setup
            final ResultService service = DoburokuProxy
                    .from(ResultService.class)
                    .translatable(method -> method::getName)
                    .result(handlers -> handlers.add(Void.class, component -> null))
                    .brew();

            // Execute
            final Void result = service.withVoid();

            // Verify
            assertNull(result);
        }

        @Test
        @DisplayName("Should use generic result handler for a component")
        void testGeneric() {
            // Setup
            final ResultService service = DoburokuProxy
                    .from(ResultService.class)
                    .translatable(method -> method::getName)
                    .result(handlers -> handlers.add(new TypeToken<Consumer<PrintStream>>() { }, component -> PrintStream::println))
                    .brew();

            // Execute
            final Consumer<PrintStream> result = service.withGeneric();

            // Verify
            assertDoesNotThrow(() -> result.accept(System.out));
        }

        @Test
        @DisplayName("Should respect component handler priority")
        void testPriority() {
            // Setup
            final ResultService service = DoburokuProxy
                    .from(ResultService.class)
                    .translatable(method -> method::getName)
                    .argument(resolvers -> resolvers
                            .add(User.class, user -> Component.text("User: " + user.name()), 0)
                            .add(Admin.class, admin -> Component.text("Admin: " + admin.name()), 10))
                    .result(handlers -> handlers
                            .add(User.class, component -> new User("Alice"), 0)
                            .add(Admin.class, component -> new Admin("Bob"), 10))
                    .brew();

            // Execute
            final Admin result = service.withPrioritized();

            // Verify
            assertEquals(new Admin("Bob"), result);
        }
    }
}
