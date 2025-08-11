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
package io.github.namiuni.doburoku.runtime.argument;

import io.github.namiuni.doburoku.api.annotation.Name;
import io.github.namiuni.doburoku.api.argument.TranslationArgumentTransformer;
import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.minimessage.translation.Argument;
import org.jspecify.annotations.NullMarked;

/**
 * Translates parameter names into MiniMessage argument names and pairs them with components.
 *
 * <p>Resolves the name from {@link Name} when present, otherwise converts the Java parameter name
 * to snake_case. Returns an {@link net.kyori.adventure.text.minimessage.translation.Argument}
 * wrapping the provided component.</p>
 */
@NullMarked
public final class MiniMessageArgumentTransformer implements TranslationArgumentTransformer {

    private static final Pattern CAMEL_PATTERN = Pattern.compile("(?=\\p{Upper})");
    private static final String ANDER_SCORE = "_";

    private final Map<Parameter, String> cache = new ConcurrentHashMap<>();

    private MiniMessageArgumentTransformer() {
    }

    /**
     * Gets the singleton instance of this renderer.
     *
     * @return the new instance
     */
    public static MiniMessageArgumentTransformer create() {
        return new MiniMessageArgumentTransformer();
    }

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
