package com.github.automatedowl.tools;

import com.github.automatedowl.tools.drivers.junitholder.JSErrorsDriverHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

/** Class that contains JUnit5 unit tests for Chromedriver JS errors collector. */
public class JSCollectorJUnitTest {

    private WebDriver driver;
    private final int BROWSER_WAIT_MILLISECONDS = 4000;

    /** Static method that sets WebDriver system property. */
    @BeforeAll
    static void setChromeDriver() {
        System.setProperty("webdriver.chrome.driver",
                System.getProperty("user.dir")
                        + "/src/main/java/com/github/automatedowl/tools/drivers/webdriver/chromedriver");
    }

    /** Test method.
     * It should receive JS reference error and expect related exception. */
    @Test
    @JSErrorsCollectorJUnit
    void referenceErrorTest(TestInfo testInfo) throws InterruptedException {
        driver = new ChromeDriver();
        JSErrorsDriverHolder.setDriverForTest(testInfo.getDisplayName(), driver);
        driver.get("http://testjs.site88.net");
        driver.findElement(By.name("testClickButton")).click();
        waitBeforeClosingBrowser();
    }

    /** Test method.
     * It should not receive any JS error and end without exception. */
    @Test
    @JSErrorsCollectorJUnit
    void noJSErrorsTest(TestInfo testInfo) throws InterruptedException {
        driver = new ChromeDriver();
        JSErrorsDriverHolder.setDriverForTest(testInfo.getDisplayName(), driver);
        driver.get("http://this-page-intentionally-left-blank.org/");
        waitBeforeClosingBrowser();
    }

    /** Test method.
     * It should receive JS error but not fail the test due boolean flag false value. */
    @Test
    @JSErrorsCollectorJUnit(assertJSErrors = false)
    void assertJSErrorsFalseTest(TestInfo testInfo) throws InterruptedException {
        driver = new ChromeDriver();
        JSErrorsDriverHolder.setDriverForTest(testInfo.getDisplayName(), driver);
        driver.get("http://testjs.site88.net");
        driver.findElement(By.name("testClickButton")).click();
        waitBeforeClosingBrowser();
    }

    @AfterEach
    void closeDriver() {
        driver.quit();
    }

    private void waitBeforeClosingBrowser() throws InterruptedException {
        Thread.sleep(BROWSER_WAIT_MILLISECONDS);
    }
}
