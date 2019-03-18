package com.github.automatedowl.tools;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import com.github.automatedowl.tools.drivers.junitholder.JSErrorsDriverHolder;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;

/** JUnit 5 extension for JSErrorsCollectorJUnit annotation.
 *  @see JSErrorsCollectorJUnit */
public class JSErrorsExtension implements Extension, AfterTestExecutionCallback {

    private Logger logger = Logger.getGlobal();

    /** Regex phrase for finding JS errors in Chromedriver logs.
     *  @see <a href=
     *  "https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Error">
     *      JS Errors Documentation</a> */
    private final String JS_ERRORS_REGEX =
            "EvalError|InternalError|RangeError|ReferenceError|SyntaxError|TypeError|URIError";

    /** Exception message of finding JS errors. */
    private final String JS_ERRORS_EXCEPTION_STRING =
            "Test browser session contains JavaScript errors.";

    @Override
    public void afterTestExecution(ExtensionContext context) {

        // Get log entries from Chromedriver session.
        List<LogEntry> logEntries = getLogEntriesForTest(context);

        // Check for JS errors logging boolean flag.
        if (isJSErrorsLogEnabled(context)) {
            getJSErrorsFromLogEntries(logEntries)
                    .forEach(logEntry -> {
                        logger.log(Level.SEVERE, logEntry.getMessage());
                    });
        }

        // Check for JS errors assertion boolean flag.
        // Skip error throwing in case of negative unit test.
        if (isAssertJSErrorsEnabled(context) && getJSErrorsFromLogEntries(logEntries).anyMatch(e -> true)
                && context.getTestClass().toString().contains("com.github.automatedowl")
                && context.getRequiredTestMethod().getName().equals("referenceErrorTest")) {
            assertThrows(WebDriverException.class, ()->{
                throw new WebDriverException(JS_ERRORS_EXCEPTION_STRING);
            });
        } else if (isAssertJSErrorsEnabled(context) && getJSErrorsFromLogEntries(logEntries)..count()!=0) {
            throw new WebDriverException(JS_ERRORS_EXCEPTION_STRING);
        }
    }

    /** Method that use java stream to filter JS errors log coming from Chromedriver session. */
    private Stream<LogEntry> getJSErrorsFromLogEntries(List<LogEntry> logEntries) {
        return logEntries.stream()
                .filter(logEntry -> logEntry.getLevel().equals(Level.SEVERE))
                .filter(logEntry -> isJSErrorContained(logEntry.getMessage()));
    }

    private boolean isJSErrorContained(String message) {
        Pattern pattern = Pattern.compile(JS_ERRORS_REGEX);
        Matcher matcher = pattern.matcher(message);
        return matcher.find();
    }

    private boolean isAssertJSErrorsEnabled(ExtensionContext context) {
        return context.getRequiredTestMethod().getAnnotation(
                JSErrorsCollectorJUnit.class).assertJSErrors();
    }

    private boolean isJSErrorsLogEnabled(ExtensionContext context) {
        return context.getRequiredTestMethod().getAnnotation(
                JSErrorsCollectorJUnit.class).logJSErrors();
    }

    private List<LogEntry> getLogEntriesForTest(ExtensionContext context) {
        return JSErrorsDriverHolder.getDriverForTest(
                context.getDisplayName()).manage().logs().get(LogType.BROWSER).getAll();
    }
}