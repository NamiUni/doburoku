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
package io.github.namiuni.doburoku.annotation.processor;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import java.util.Set;
import javax.lang.model.SourceVersion;
import javax.tools.JavaFileObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ResourceBundleProcessorTest {

    // Test constants
    private static final String TEST_PACKAGE = "test";
    private static final String TEST_BASE_NAME = "test-messages";

    private final ResourceBundleProcessor processor = new ResourceBundleProcessor();

    @Nested
    @DisplayName("Basic functionality tests")
    class BasicFunctionalityTests {

        @Test
        @DisplayName("Verify that supported annotation types are correct")
        void testGetSupportedAnnotationTypes() {
            final Set<String> supportedTypes = processor.getSupportedAnnotationTypes();

            // Verify that expected annotations are included
            assertTrue(supportedTypes.contains("io.github.namiuni.doburoku.annotation.annotations.Key"));
            assertTrue(supportedTypes.contains("io.github.namiuni.doburoku.annotation.annotations.ResourceBundle"));
            assertTrue(supportedTypes.contains("io.github.namiuni.doburoku.annotation.annotations.Value"));
            assertTrue(supportedTypes.contains("io.github.namiuni.doburoku.annotation.annotations.Values"));

            // Verify that exactly 4 annotation types are supported
            assertEquals(4, supportedTypes.size());

            // Verify that unsupported annotations are not included
            assertFalse(supportedTypes.contains("java.lang.Override"));
            assertFalse(supportedTypes.contains("java.lang.Deprecated"));
        }

        @Test
        @DisplayName("Verify that supported Java source version is correct")
        void testGetSupportedSourceVersion() {
            assertEquals(SourceVersion.RELEASE_21, processor.getSupportedSourceVersion());
        }
    }

    @Nested
    @DisplayName("Successful processing tests")
    class SuccessfulProcessingTests {

        @Test
        @DisplayName("Verify that valid interface is processed correctly")
        void testProcessWithValidInterface() {
            final JavaFileObject testMessageService = createValidInterfaceSource();

            final Compilation compilation = javac()
                    .withProcessors(processor)
                    .compile(testMessageService);

            assertThat(compilation).succeeded();
            assertThat(compilation).hadNoteContaining("Generated resource bundle");
        }

        @Test
        @DisplayName("Verify that method with single @Value annotation is processed correctly")
        void testProcessWithSingleValueAnnotation() {
            final JavaFileObject testInterface = JavaFileObjects.forSourceString(
                    TEST_PACKAGE + ".SingleValueInterface",
                    """
                            package test;
                            
                            import io.github.namiuni.doburoku.annotation.Locales;
                            import io.github.namiuni.doburoku.annotation.annotations.Key;
                            import io.github.namiuni.doburoku.annotation.annotations.ResourceBundle;
                            import io.github.namiuni.doburoku.annotation.annotations.Value;
                            
                            @ResourceBundle(baseName = "single-value")
                            public interface SingleValueInterface {
                                @Key("hello.world")
                                @Value(locale = Locales.EN_US, content = "Hello, World!")
                                void helloWorld();
                            }
                            """
            );

            final Compilation compilation = javac()
                    .withProcessors(processor)
                    .compile(testInterface);

            assertThat(compilation).succeeded();
        }

        @Test
        @DisplayName("Verify that empty interface is processed without warnings")
        void testProcessWithEmptyInterface() {
            final JavaFileObject emptyInterface = JavaFileObjects.forSourceString(
                    TEST_PACKAGE + ".EmptyInterface",
                    """
                            package test;
                            
                            import io.github.namiuni.doburoku.annotation.annotations.ResourceBundle;
                            
                            @ResourceBundle(baseName = "empty")
                            public interface EmptyInterface {
                            }
                            """
            );

            final Compilation compilation = javac()
                    .withProcessors(processor)
                    .compile(emptyInterface);

            assertThat(compilation).succeeded();
        }

        @Test
        @DisplayName("Verify that repeatable @Value annotations are processed correctly")
        void testProcessWithRepeatableValueAnnotations() {
            final JavaFileObject testInterface = JavaFileObjects.forSourceString(
                    TEST_PACKAGE + ".RepeatableValueInterface",
                    """
                            package test;

                            import io.github.namiuni.doburoku.annotation.Locales;
                            import io.github.namiuni.doburoku.annotation.annotations.Key;
                            import io.github.namiuni.doburoku.annotation.annotations.ResourceBundle;
                            import io.github.namiuni.doburoku.annotation.annotations.Value;

                            @ResourceBundle(baseName = "repeatable-values")
                            public interface RepeatableValueInterface {
                                @Key("test.repeatable.message")
                                @Value(locale = Locales.EN_US, content = "Hello")
                                @Value(locale = Locales.JA_JP, content = "こんにちは")
                                @Value(locale = Locales.DE_DE, content = "Hallo")
                                void multipleRepeatableValues();
                            }
                            """
            );

            final Compilation compilation = javac()
                    .withProcessors(processor)
                    .compile(testInterface);

            assertThat(compilation).succeeded();
            assertThat(compilation).hadNoteContaining("Generated resource bundle");
        }

        @Test
        @DisplayName("Verify that mixed @Value and @Values annotations work correctly")
        void testProcessWithMixedValueAnnotations() {
            final JavaFileObject testInterface = JavaFileObjects.forSourceString(
                    TEST_PACKAGE + ".MixedValueInterface",
                    """
                            package test;

                            import io.github.namiuni.doburoku.annotation.Locales;
                            import io.github.namiuni.doburoku.annotation.annotations.Key;
                            import io.github.namiuni.doburoku.annotation.annotations.ResourceBundle;
                            import io.github.namiuni.doburoku.annotation.annotations.Value;
                            import io.github.namiuni.doburoku.annotation.annotations.Values;

                            @ResourceBundle(baseName = "mixed-values")
                            public interface MixedValueInterface {
                                @Key("test.repeatable")
                                @Value(locale = Locales.EN_US, content = "Repeatable Hello")
                                @Value(locale = Locales.JA_JP, content = "リピータブル こんにちは")
                                void repeatableValues();

                                @Key("test.wrapped")
                                @Values({
                                    @Value(locale = Locales.EN_US, content = "Wrapped Hello"),
                                    @Value(locale = Locales.DE_DE, content = "Eingepackt Hallo")
                                })
                                void wrappedValues();
                            }
                            """
            );

            final Compilation compilation = javac()
                    .withProcessors(processor)
                    .compile(testInterface);

            assertThat(compilation).succeeded();
            assertThat(compilation).hadNoteContaining("Generated resource bundle");
        }
    }

    @Nested
    @DisplayName("Error case tests")
    class ErrorCasesTests {

        @Test
        @DisplayName("Verify error when @ResourceBundle is applied to non-interface")
        void testProcessWithNonInterface() {
            final JavaFileObject testClass = JavaFileObjects.forSourceString(
                    TEST_PACKAGE + ".TestClass",
                    """
                            package test;
                            
                            import io.github.namiuni.doburoku.annotation.annotations.ResourceBundle;
                            
                            @ResourceBundle(baseName = "test")
                            public class TestClass {
                            }
                            """
            );

            final Compilation compilation = javac()
                    .withProcessors(processor)
                    .compile(testClass);

            assertThat(compilation).hadErrorContaining("@ResourceBundle can only be applied to interfaces");
        }

        @Test
        @DisplayName("Verify error when @ResourceBundle is applied to abstract class")
        void testProcessWithAbstractClass() {
            final JavaFileObject abstractClass = JavaFileObjects.forSourceString(
                    TEST_PACKAGE + ".TestAbstractClass",
                    """
                            package test;
                            
                            import io.github.namiuni.doburoku.annotation.annotations.ResourceBundle;
                            
                            @ResourceBundle(baseName = "test")
                            public abstract class TestAbstractClass {
                            }
                            """
            );

            final Compilation compilation = javac()
                    .withProcessors(processor)
                    .compile(abstractClass);

            assertThat(compilation).hadErrorContaining("@ResourceBundle can only be applied to interfaces");
        }

        @Test
        @DisplayName("Verify error when @ResourceBundle is applied to enum")
        void testProcessWithEnum() {
            final JavaFileObject testEnum = JavaFileObjects.forSourceString(
                    TEST_PACKAGE + ".TestEnum",
                    """
                            package test;
                            
                            import io.github.namiuni.doburoku.annotation.annotations.ResourceBundle;
                            
                            @ResourceBundle(baseName = "test")
                            public enum TestEnum {
                                VALUE1, VALUE2
                            }
                            """
            );

            final Compilation compilation = javac()
                    .withProcessors(processor)
                    .compile(testEnum);

            assertThat(compilation).hadErrorContaining("@ResourceBundle can only be applied to interfaces");
        }
    }

    @Nested
    @DisplayName("Warning case tests")
    class WarningCasesTests {

        @Test
        @DisplayName("Verify warning for method without @Key annotation")
        void testMethodWithoutKeyAnnotation() {
            final JavaFileObject testInterface = JavaFileObjects.forSourceString(
                    TEST_PACKAGE + ".TestInterface",
                    """
                            package test;
                            
                            import io.github.namiuni.doburoku.annotation.annotations.ResourceBundle;
                            
                            @ResourceBundle(baseName = "test")
                            public interface TestInterface {
                                void methodWithoutAnnotations();
                            }
                            """
            );

            final Compilation compilation = javac()
                    .withProcessors(processor)
                    .compile(testInterface);

            assertThat(compilation).hadWarningContaining("Method missing @Key annotation");
        }

        @Test
        @DisplayName("Verify processing of method with @Key but no @Value/@Values")
        void testMethodWithKeyButNoValue() {
            final JavaFileObject testInterface = JavaFileObjects.forSourceString(
                    TEST_PACKAGE + ".IncompleteInterface",
                    """
                            package test;
                            
                            import io.github.namiuni.doburoku.annotation.annotations.Key;
                            import io.github.namiuni.doburoku.annotation.annotations.ResourceBundle;
                            
                            @ResourceBundle(baseName = "incomplete")
                            public interface IncompleteInterface {
                                @Key("test.key")
                                void methodWithKeyOnly();
                            }
                            """
            );

            final Compilation compilation = javac()
                    .withProcessors(processor)
                    .compile(testInterface);

            // No warning or error when @Key exists but @Value/@Values is missing (processed as empty value array)
            assertThat(compilation).succeeded();
        }

        @Test
        @DisplayName("Verify processing when multiple methods are mixed")
        void testMixedMethods() {
            final JavaFileObject testInterface = JavaFileObjects.forSourceString(
                    TEST_PACKAGE + ".MixedInterface",
                    """
                            package test;
                            
                            import io.github.namiuni.doburoku.annotation.Locales;
                            import io.github.namiuni.doburoku.annotation.annotations.Key;
                            import io.github.namiuni.doburoku.annotation.annotations.ResourceBundle;
                            import io.github.namiuni.doburoku.annotation.annotations.Value;
                            
                            @ResourceBundle(baseName = "mixed")
                            public interface MixedInterface {
                                @Key("valid.key")
                                @Value(locale = Locales.EN_US, content = "Valid content")
                                void validMethod();
                            
                                void methodWithoutAnnotations();
                            
                                @Key("incomplete.key")
                                void incompleteMethod();
                            }
                            """
            );

            final Compilation compilation = javac()
                    .withProcessors(processor)
                    .compile(testInterface);

            assertThat(compilation).succeeded();
            assertThat(compilation).hadWarningContaining("Method missing @Key annotation");
        }
    }

    // Helper methods
    private JavaFileObject createValidInterfaceSource() {
        return JavaFileObjects.forSourceString(
                TEST_PACKAGE + ".TestMessageService",
                """
                        package test;
                        
                        import io.github.namiuni.doburoku.annotation.Locales;
                        import io.github.namiuni.doburoku.annotation.annotations.Key;
                        import io.github.namiuni.doburoku.annotation.annotations.ResourceBundle;
                        import io.github.namiuni.doburoku.annotation.annotations.Value;
                        import io.github.namiuni.doburoku.annotation.annotations.Values;
                        
                        @ResourceBundle(baseName = "%s")
                        public interface TestMessageService {
                        
                            @Key("test.simple.message")
                            @Value(locale = Locales.EN_US, content = "Hello World")
                            void simpleMessage();
                        
                            @Key("test.multiple.locales")
                            @Values({
                                @Value(locale = Locales.EN_US, content = "Welcome"),
                                @Value(locale = Locales.JA_JP, content = "ようこそ")
                            })
                            void multipleLocales();
                        
                            @Key("test.with.parameters")
                            @Value(locale = Locales.EN_US, content = "Hello <name>!")
                            void withParameters(String name);
                        }
                        """.formatted(TEST_BASE_NAME)
        );
    }
}
