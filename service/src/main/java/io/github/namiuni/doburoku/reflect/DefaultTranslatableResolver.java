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
package io.github.namiuni.doburoku.reflect;

import io.github.namiuni.doburoku.service.common.handlers.TranslatableResolver;
import java.lang.reflect.Method;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import net.kyori.adventure.translation.Translatable;
import org.jspecify.annotations.NullMarked;

/**
 * A default implementation of {@link TranslatableResolver} that generates translation keys
 * based on class and method names.
 * <p>
 * This resolver constructs a key by joining the formatted names of the declaring classes
 * and the method name, using a configurable delimiter and prefix. For example, a call to
 * {@code my.package.MyService.NestedService.helloWorld()} could be resolved to a key like
 * {@code "nested.service.hello.world"}.
 */
@NullMarked
public final class DefaultTranslatableResolver implements TranslatableResolver {

    private static final String DELIMITER = ".";
    private static final Pattern SEPARATE_PATTERN = Pattern.compile("(?=\\p{Upper})");

    private final String prefix;

    private DefaultTranslatableResolver(final String prefix) {
        this.prefix = prefix;
    }

    static DefaultTranslatableResolver create() {
        final String prefix = "";
        return DefaultTranslatableResolver.create(prefix);
    }

    /**
     * Creates a new resolver with a specified prefix.
     *
     * @param prefix the prefix to prepend to all generated keys
     * @return a new {@link DefaultTranslatableResolver} instance
     */
    public static DefaultTranslatableResolver create(final String prefix) {
        return new DefaultTranslatableResolver(prefix);
    }

    /**
     * Resolves a {@link Translatable} key from the given method.
     *
     * @param method the method to resolve the key from
     * @return the resolved {@link Translatable} key
     */
    @Override
    public Translatable resolve(final Method method) {
        return () -> {
            final StringBuilder builder = new StringBuilder();
            this.getPath(builder, method.getDeclaringClass());

            if (!builder.isEmpty()) {
                builder.append(DELIMITER);
            }

            final String methodName = SEPARATE_PATTERN
                    .splitAsStream(method.getName())
                    .map(String::toLowerCase)
                    .collect(Collectors.joining(DELIMITER));
            return builder.append(methodName).toString();
        };
    }

    private void getPath(final StringBuilder builder, final Class<?> currentClass) {
        final Class<?> parentClass = currentClass.getDeclaringClass();

        if (parentClass == null) {
            if (!this.prefix.isEmpty()) {
                builder.append(this.prefix);
            }
        } else {
            this.getPath(builder, parentClass);
            if (!builder.isEmpty()) {
                builder.append(DELIMITER);
            }
            final String formatted = SEPARATE_PATTERN
                    .splitAsStream(currentClass.getSimpleName())
                    .map(String::toLowerCase)
                    .collect(Collectors.joining(DELIMITER));
            builder.append(formatted);
        }
    }
}
