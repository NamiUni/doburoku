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

import io.github.namiuni.doburoku.core.key.KeyResolver;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TranslatableComponent;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * An {@link InvocationHandler} that translates method calls into {@link TranslatableComponent}s.
 *
 * <p>This is the core of the translation service, intercepting method calls on a proxy
 * interface, resolving arguments into {@link Component}s, and constructing a
 * {@link TranslatableComponent} with a key derived from the method name.</p>
 */
@NullMarked
final class TranslationInvocationHandler implements InvocationHandler {
    private static final @Nullable Object[] EMPTY_ARGUMENTS = new Object[0];

    private final TranslationServiceFactory factory;
    private final Map<Method, CachedMethod> cachedMethods;

    /**
     * Constructs a new {@code TranslationInvocationHandler}.
     *
     * @param factory        the factory containing configuration for argument and result handling
     * @param serviceInterface the interface being proxied
     * @param keyResolver    the resolver for generating translation keys
     * @param parentKey      the parent key for nested services
     */
    TranslationInvocationHandler(
            final TranslationServiceFactory factory,
            final Class<?> serviceInterface,
            final KeyResolver keyResolver,
            final String parentKey
    ) {
        this.factory = factory;
        this.cachedMethods = Arrays.stream(serviceInterface.getMethods())
                .filter(method -> !method.isDefault() && method.getDeclaringClass() != Object.class)
                .collect(Collectors.toUnmodifiableMap(
                        method -> method,
                        method -> CachedMethod.create(factory, keyResolver, method, parentKey)
                ));
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

        final CachedMethod cachedMethod = this.cachedMethods.get(method);
        // Handle nested service interfaces
        if (cachedMethod.childServiceSupplier() != null) {
            return cachedMethod.childServiceSupplier().get();
        }

        // Process arguments and create TranslatableComponent
        final @Nullable Object[] arguments = args == null ? EMPTY_ARGUMENTS : args;
        final ComponentLike[] componentArgs = new ComponentLike[arguments.length];
        final Parameter[] parameters = method.getParameters();
        for (int i = 0; i < arguments.length; i++) {
            final Object argument = arguments[i];
            componentArgs[i] = argument == null
                    ? Component.empty()
                    // Resolve each argument into a ComponentLike object
                    : this.factory.argumentResolver().resolve(parameters[i], argument);
        }

        // Create the translatable component
        final TranslatableComponent result = Component.translatable(cachedMethod.key(), componentArgs);
        // Handle the result and convert it to the expected return type
        return this.factory.resultHandler().handle(method.getGenericReturnType(), result);
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
