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
package io.github.namiuni.doburoku.reflect.minimessage;

import java.lang.reflect.Parameter;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.translation.Argument;
import net.kyori.adventure.text.renderer.ComponentRenderer;
import org.jspecify.annotations.NullMarked;

/**
 * A {@link ComponentRenderer} that transforms a {@link Component} into a MiniMessage
 * named placeholder.
 * <p>
 * This renderer takes a resolved component and wraps it in a MiniMessage {@link Argument},
 * using the associated method parameter's name as the placeholder tag. The parameter name
 * is converted from camelCase to snake_case (e.g., {@code "userName"} becomes {@code "user_name"}).
 * The result can be used directly in MiniMessage templates via tags like {@code <user_name>}.
 * <p>
 * This class is a singleton and can be accessed via {@link #instance()}.
 */
@NullMarked
public final class MiniMessagePlaceholderRenderer implements ComponentRenderer<Parameter> {

    private static final MiniMessagePlaceholderRenderer INSTANCE = new MiniMessagePlaceholderRenderer();
    private static final Pattern SEPARATE_PATTERN = Pattern.compile("(?=\\p{Upper})");

    private MiniMessagePlaceholderRenderer() {
    }

    /**
     * Gets the singleton instance of this renderer.
     *
     * @return the {@link MiniMessagePlaceholderRenderer} instance
     */
    public static MiniMessagePlaceholderRenderer instance() {
        return INSTANCE;
    }

    /**
     * Renders the given component as a MiniMessage placeholder, using the context
     * parameter to determine the placeholder's name.
     *
     * @param component the component to be wrapped in a placeholder
     * @param context   the method {@link Parameter} from which the placeholder name will be derived
     * @return a component representing a MiniMessage placeholder argument
     */
    @Override
    @SuppressWarnings("PatternValidation")
    public Component render(final Component component, final Parameter context) {
        return SEPARATE_PATTERN.splitAsStream(context.getName())
                .map(String::toLowerCase)
                .collect(Collectors.joining("_"))
                .transform(name -> Argument.component(name, component).asComponent());
    }
}
