package com.github.automatedowl.tools;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;

/** Java annotation for collection chromedriver JS errors using JUnit 5. */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.ANNOTATION_TYPE})
@ExtendWith(JSErrorsExtension.class)
public @interface JSErrorsCollectorJUnit {

    /** Boolean flag for asserting JS errors at the end of the test. */
    boolean assertJSErrors() default true;

    /** Boolean flag for logging JS errors at the end of the test. */
    boolean logJSErrors() default true;
}
