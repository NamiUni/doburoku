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
package io.github.namiuni.doburoku.core;

import io.github.namiuni.doburoku.common.DoburokuMethod;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
final class DoburokuInvocationHandler implements InvocationHandler {
    private static final @Nullable Object[] EMPTY_ARGUMENTS = new Object[0];

    private final DoburokuDrunkard doburokuDrunkard;
    private final DoburokuProxyFactory doburokuFactory;

    private final Map<Method, Object> childCache = new ConcurrentHashMap<>();

    DoburokuInvocationHandler(
            final DoburokuDrunkard doburokuDrunkard,
            final DoburokuProxyFactory doburokuFactory
    ) {
        this.doburokuDrunkard = doburokuDrunkard;
        this.doburokuFactory = doburokuFactory;
    }

    @Override
    public @Nullable Object invoke(final Object proxy, final Method method, final @Nullable Object @Nullable [] args) throws Throwable {
        // Handle methods from Object class (e.g., equals, hashCode, toString)
        if (method.getDeclaringClass() == Object.class) {
            return this.handleObjectMethod(proxy, method, args);
        }

        // Handle default methods defined in the interface
        if (method.isDefault()) {
            return MethodHandles.privateLookupIn(method.getDeclaringClass(), MethodHandles.lookup())
                    .unreflectSpecial(method, method.getDeclaringClass())
                    .bindTo(proxy)
                    .invokeWithArguments(args);
        }

        if (this.isChildInterface(method)) {
            return this.childCache.computeIfAbsent(method, m ->
                    this.doburokuFactory.create(m.getReturnType()));
        }

        final @Nullable Object[] arguments = Objects.requireNonNullElse(args, EMPTY_ARGUMENTS);
        return this.doburokuDrunkard.drunk(DoburokuMethod.of(proxy, method, arguments));
    }

    private boolean isChildInterface(final Method method) {
        return method.getDeclaringClass() == method.getReturnType().getDeclaringClass();
    }

    private Object handleObjectMethod(final Object proxy, final Method method, final @Nullable Object @Nullable [] args) {
        return switch (method.getName()) {
            case "equals" -> args != null && args.length == 1 && proxy == args[0];
            case "hashCode" -> System.identityHashCode(proxy);
            case "toString" -> "TranslationService<" + proxy.getClass().getInterfaces()[0].getSimpleName() + ">";
            default -> throw new UnsupportedOperationException("Unsupported Object method: " + method.getName());
        };
    }
}
