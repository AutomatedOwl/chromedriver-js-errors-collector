package com.github.automatedowl.tools;

import com.github.automatedowl.tools.drivers.testngholder.JSErrorsDriverHolder;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.testng.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/** TestNG listener for JSErrorsCollectorTestNG annotation.
 *  @see JSErrorsCollectorTestNG */
public class JSErrorsCollectorListener implements IInvokedMethodListener {

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
    public void beforeInvocation(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {
        // Do nothing.
    }

    @Override
    public void afterInvocation(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {

        // Check if test method had invoked.
        if (iInvokedMethod.getTestMethod().isTest() && isTestAnnotated(iInvokedMethod)) {

            // Get log entries from Chromedriver session.
            List<LogEntry> logEntries = getLogEntriesForTest(iInvokedMethod);

            // Check for JS errors logging boolean flag.
            if (isJSErrorsLogEnabled(iInvokedMethod)) {
                getJSErrorsFromLogEntries(logEntries)
                        .forEach(logEntry -> {
                            logger.log(Level.SEVERE, logEntry.getMessage());
                        });
            }

            // Check for JS errors assertion boolean flag.
            // Skip error throwing in case of negative unit test.
            if (isAssertJSErrorsEnabled(
                    iInvokedMethod) && getJSErrorsFromLogEntries(logEntries).count()!=0
                    && iInvokedMethod
                    .getTestMethod().getTestClass().toString().contains("com.github.automatedowl")
                    && iInvokedMethod
                    .getTestMethod().getConstructorOrMethod().getMethod().getName().equals("referenceErrorTest")) {
                // Don't throw exception on unit test.
            }
            else if (isAssertJSErrorsEnabled(
                    iInvokedMethod) && getJSErrorsFromLogEntries(logEntries).count()!=0) {
                logger.severe(JS_ERRORS_EXCEPTION_STRING);
                throw new WebDriverException(JS_ERRORS_EXCEPTION_STRING);
            }
        }
    }

    private boolean isTestAnnotated(IInvokedMethod method) {
        return method
                .getTestMethod()
                .getConstructorOrMethod()
                .getMethod()
                .getAnnotation(JSErrorsCollectorTestNG.class) != null;
    }

    private boolean isJSErrorContained(String message) {
        Pattern pattern = Pattern.compile(JS_ERRORS_REGEX);
        Matcher matcher = pattern.matcher(message);
        return matcher.find();
    }

    private boolean isAssertJSErrorsEnabled(IInvokedMethod method) {
        return method.getTestMethod().getConstructorOrMethod().getMethod().getAnnotation(
                JSErrorsCollectorTestNG.class).assertJSErrors();
    }

    private boolean isJSErrorsLogEnabled(IInvokedMethod method) {
        return method.getTestMethod().getConstructorOrMethod().getMethod().getAnnotation(
                JSErrorsCollectorTestNG.class).logJSErrors();
    }

    private List<LogEntry> getLogEntriesForTest(IInvokedMethod method) {
        return JSErrorsDriverHolder.getDriverForTest(
                method.getTestMethod().getMethodName()).manage().logs().get(LogType.BROWSER).getAll();
    }

    /** Method that use java stream to filter JS errors log coming from Chromedriver session. */
    private Stream<LogEntry> getJSErrorsFromLogEntries(List<LogEntry> logEntries) {
        return logEntries.stream()
                .filter(logEntry -> logEntry.getLevel().equals(Level.SEVERE))
                .filter(logEntry -> isJSErrorContained(logEntry.getMessage()));
    }
}
