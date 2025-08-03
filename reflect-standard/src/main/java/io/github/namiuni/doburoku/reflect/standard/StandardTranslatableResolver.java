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
package io.github.namiuni.doburoku.reflect.standard;

import io.github.namiuni.doburoku.reflect.api.handlers.TranslatableResolver;
import java.lang.reflect.Method;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import net.kyori.adventure.translation.Translatable;
import org.jspecify.annotations.NullMarked;

/**
 * A standard implementation of {@link TranslatableResolver} that generates translation keys
 * based on class and method names.
 * <p>
 * This resolver constructs a key by joining the formatted names of the declaring classes
 * and the method name, using a configurable delimiter and prefix. For example, a call to
 * {@code my.package.MyService.NestedService.getMessage()} could be resolved to a key like
 * {@code "my.prefix.my.service.nested.service.getMessage"}.
 */
@NullMarked
public final class StandardTranslatableResolver implements TranslatableResolver {

    private final String prefix;
    private final char delimiter;
    private final Pattern separatePattern;

    private StandardTranslatableResolver(
            final String prefix,
            final char delimiter,
            final Pattern separatePattern
    ) {
        this.prefix = prefix;
        this.delimiter = delimiter;
        this.separatePattern = separatePattern;
    }

    /**
     * Creates a new resolver with default settings (empty prefix, '.' delimiter, camel-case splitter).
     *
     * @return a new {@link StandardTranslatableResolver} instance
     */
    public static StandardTranslatableResolver create() {
        final String prefix = "";
        return StandardTranslatableResolver.create(prefix);
    }

    /**
     * Creates a new resolver with a specified prefix.
     *
     * @param prefix the prefix to prepend to all generated keys
     * @return a new {@link StandardTranslatableResolver} instance
     */
    public static StandardTranslatableResolver create(
            final String prefix
    ) {
        final char delimiter = '.';
        return StandardTranslatableResolver.create(prefix, delimiter);
    }

    /**
     * Creates a new resolver with a specified prefix and delimiter.
     *
     * @param prefix    the prefix to prepend to all generated keys
     * @param delimiter the character used to separate parts of the key
     * @return a new {@link StandardTranslatableResolver} instance
     */
    public static StandardTranslatableResolver create(
            final String prefix,
            final char delimiter
    ) {
        final Pattern camelPattern = Pattern.compile("(?=\\p{Upper})");
        return StandardTranslatableResolver.create(prefix, delimiter, camelPattern);
    }

    /**
     * Creates a new resolver with a specified prefix and splitting pattern.
     *
     * @param prefix  the prefix to prepend to all generated keys
     * @param pattern the regex pattern used to split class names into parts (e.g., to break up camelCase)
     * @return a new {@link StandardTranslatableResolver} instance
     */
    public static StandardTranslatableResolver create(
            final String prefix,
            final Pattern pattern
    ) {
        final char delimiter = '.';
        return StandardTranslatableResolver.create(prefix, delimiter, pattern);
    }

    /**
     * Creates a new resolver with a specified delimiter and splitting pattern.
     *
     * @param delimiter the character used to separate parts of the key
     * @param pattern   the regex pattern used to split class names into parts
     * @return a new {@link StandardTranslatableResolver} instance
     */
    public static StandardTranslatableResolver create(
            final char delimiter,
            final Pattern pattern
    ) {
        final String prefix = "";
        return StandardTranslatableResolver.create(prefix, delimiter, pattern);
    }

    /**
     * Creates a new resolver with a specified prefix, delimiter, and splitting pattern.
     *
     * @param prefix       the prefix to prepend to all generated keys
     * @param delimiter    the character used to separate parts of the key
     * @param splitPattern the regex pattern used to split class names into parts
     * @return a new {@link StandardTranslatableResolver} instance
     */
    public static StandardTranslatableResolver create(
            final String prefix,
            final char delimiter,
            final Pattern splitPattern
    ) {
        return new StandardTranslatableResolver(prefix, delimiter, splitPattern);
    }

    /**
     * Resolves a {@link Translatable} key from the given method.
     *
     * @param method the method to resolve the key from
     * @return the resolved {@link Translatable} key
     */
    @Override
    public Translatable resolve(final Method method) {
        final StringBuilder builder = new StringBuilder();
        this.getPath(builder, method.getDeclaringClass());

        final String methodName = this.separatePattern
                .splitAsStream(method.getName())
                .map(String::toLowerCase)
                .collect(Collectors.joining(String.valueOf(this.delimiter)));
        return () -> builder.append(methodName).toString();
    }

    private void getPath(final StringBuilder builder, final Class<?> currentClass) {
        final Class<?> parentClass = currentClass.getDeclaringClass();

        if (parentClass == null) {
            builder.append(this.prefix).append(this.delimiter);
        } else {
            this.getPath(builder, parentClass);
            final String formatted = this.separatePattern
                    .splitAsStream(currentClass.getSimpleName())
                    .map(String::toLowerCase)
                    .collect(Collectors.joining(String.valueOf(this.delimiter)))
                    .transform(s -> s + this.delimiter);
            builder.append(formatted);
        }
    }
}
