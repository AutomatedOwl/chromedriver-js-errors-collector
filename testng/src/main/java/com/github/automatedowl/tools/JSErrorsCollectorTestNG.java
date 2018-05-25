package com.github.automatedowl.tools;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Java annotation for collection chromedriver JS errors using TestNG. */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.ANNOTATION_TYPE})
public @interface JSErrorsCollectorTestNG {

    /** Boolean flag for asserting JS errors at the end of the test. */
    boolean assertJSErrors() default true;

    /** Boolean flag for logging JS errors at the end of the test. */
    boolean logJSErrors() default true;
}
