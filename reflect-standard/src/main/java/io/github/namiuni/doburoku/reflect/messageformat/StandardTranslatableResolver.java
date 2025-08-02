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
package io.github.namiuni.doburoku.reflect.messageformat;

import io.github.namiuni.doburoku.reflect.api.handlers.TranslatableResolver;
import java.lang.reflect.Method;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import net.kyori.adventure.translation.Translatable;
import org.jspecify.annotations.NullMarked;

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

    public static StandardTranslatableResolver create() {
        final String prefix = "";
        return StandardTranslatableResolver.create(prefix);
    }

    public static StandardTranslatableResolver create(
            final String prefix
    ) {
        final char delimiter = '.';
        return StandardTranslatableResolver.create(prefix, delimiter);
    }

    public static StandardTranslatableResolver create(
            final String prefix,
            final char delimiter
    ) {
        final Pattern camelPattern = Pattern.compile("(?=\\p{Upper})");
        return StandardTranslatableResolver.create(prefix, delimiter, camelPattern);
    }

    public static StandardTranslatableResolver create(
            final String prefix,
            final Pattern pattern
    ) {
        final char delimiter = '.';
        return StandardTranslatableResolver.create(prefix, delimiter, pattern);
    }

    public static StandardTranslatableResolver create(
            final char delimiter,
            final Pattern pattern
    ) {
        final String prefix = "";
        return StandardTranslatableResolver.create(prefix, delimiter, pattern);
    }

    public static StandardTranslatableResolver create(
            final String prefix,
            final char delimiter,
            final Pattern splitPattern
    ) {
        return new StandardTranslatableResolver(prefix, delimiter, splitPattern);
    }

    @Override
    public Translatable resolve(final Method method) {
        final StringBuilder builder = new StringBuilder();
        this.getPath(builder, method.getDeclaringClass());
        return () -> builder.append(method.getName()).toString();
    }

    private void getPath(final StringBuilder builder, final Class<?> currentClass) {
        final Class<?> parentClass = currentClass.getDeclaringClass();

        if (parentClass == null) {
            builder.append(this.prefix).append(this.delimiter);
        } else {
            getPath(builder, parentClass);
            final String formatted = this.separatePattern
                    .splitAsStream(currentClass.getSimpleName())
                    .map(String::toLowerCase)
                    .collect(Collectors.joining(String.valueOf(this.delimiter)))
                    .transform(s -> s + delimiter);
            builder.append(formatted);
        }
    }
}
