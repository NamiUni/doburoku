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

import io.github.namiuni.doburoku.standard.annotation.Name;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.minimessage.translation.Argument;
import org.jspecify.annotations.NullMarked;

/**
 * A {@link TranslationArgumentTransformer} that converts rendered arguments into
 * MiniMessage named arguments using a resolved placeholder name.
 * <p>The placeholder name is derived from {@link Name} if present; otherwise the
 * parameter's camelCase name is converted to snake_case.</p>
 * <p>This implementation is stateless aside from a simple cache and is provided as a singleton.</p>
 */
@NullMarked
public final class MiniMessageArgumentRenderer implements TranslationArgumentTransformer {

    private static final MiniMessageArgumentRenderer INSTANCE = new MiniMessageArgumentRenderer();

    private static final Pattern CAMEL_PATTERN = Pattern.compile("(?=\\p{Upper})");
    private static final String ANDER_SCORE = "_";

    private final Map<Parameter, String> cache = new HashMap<>();

    private MiniMessageArgumentRenderer() {
    }

    /**
     * Returns the singleton instance of this transformer.
     *
     * @return the shared {@code MiniMessageArgumentRenderer} instance
     */
    public static MiniMessageArgumentRenderer instance() {
        return INSTANCE;
    }

    /**
     * Wraps the supplied component into a MiniMessage {@link Argument} with the resolved
     * parameter name.
     *
     * @param parameter the reflective parameter for which the argument was rendered
     * @param argument  the component representing the argument value
     * @return a MiniMessage named argument component
     */
    @Override
    @SuppressWarnings("PatternValidation")
    public ComponentLike transform(final Parameter parameter, final ComponentLike argument) {
        final String name = this.resolveName(parameter);
        return Argument.component(name, argument);
    }

    private String resolveName(final Parameter parameter) {
        return this.cache.computeIfAbsent(parameter, it -> {
            if (it.isAnnotationPresent(Name.class)) {
                return it.getAnnotation(Name.class).value();
            } else {
                return CAMEL_PATTERN.splitAsStream(it.getName())
                        .map(String::toLowerCase)
                        .collect(Collectors.joining(ANDER_SCORE));
            }
        });
    }
}
