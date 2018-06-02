package com.github.automatedowl.tools;

import com.github.automatedowl.tools.drivers.junitholder.JSErrorsDriverHolder;
import com.github.automatedowl.tools.pages.BlankPage;
import com.github.automatedowl.tools.pages.Site88Page;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

/** Class that contains JUnit5 unit tests for Chromedriver JS errors collector. */
public class JSCollectorJUnitTest {

    private WebDriver driver;
    private Site88Page site88Page;

    // Define timeout before closing browser after test.
    private final int BROWSER_WAIT_MILLISECONDS = 4000;

    /** Static method that sets WebDriver system property. */
    @BeforeAll
    static void setChromeDriver() {
        System.setProperty("webdriver.chrome.driver",
                System.getProperty("user.dir")
                        + "/src/main/java/com/github/automatedowl" +
                        "/tools/drivers/webdriver/chromedriver");
    }

    /** Test method.
     * It should receive JS reference error and expect related exception. */
    @Test
    @JSErrorsCollectorJUnit
    void referenceErrorTest(TestInfo testInfo) throws InterruptedException {
        driver = new ChromeDriver();
        site88Page = new Site88Page(driver);
        JSErrorsDriverHolder.setDriverForTest(testInfo.getDisplayName(), driver);

        // Navigate to URL.
        site88Page.navigateToPage(driver);
        site88Page.getTestButton().click();
        waitBeforeClosingBrowser();
    }

    /** Test method.
     * It should receive JS error but not fail the test due boolean flag false value. */
    @Test
    @JSErrorsCollectorJUnit(assertJSErrors = false)
    void assertJSErrorsFalseTest(TestInfo testInfo) throws InterruptedException {
        driver = new ChromeDriver();
        site88Page = new Site88Page(driver);
        JSErrorsDriverHolder.setDriverForTest(testInfo.getDisplayName(), driver);

        // Navigate to URL.
        site88Page.navigateToPage(driver);
        site88Page.getTestButton().click();
        waitBeforeClosingBrowser();
    }

    /** Test method.
     * It should not receive any JS error and end without exception. */
    @Test
    @JSErrorsCollectorJUnit
    void noJSErrorsTest(TestInfo testInfo) throws InterruptedException {
        driver = new ChromeDriver();
        JSErrorsDriverHolder.setDriverForTest(testInfo.getDisplayName(), driver);
        BlankPage blankPage = new BlankPage();

        // Navigate to URL.
        blankPage.navigateToPage(driver);
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
