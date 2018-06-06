package com.github.automatedowl.tools;

import java.lang.reflect.Method;
import com.github.automatedowl.tools.drivers.testngholder.JSErrorsDriverHolder;
import com.github.automatedowl.tools.pages.BlankPage;
import com.github.automatedowl.tools.pages.Site88Page;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

/** Class that contains TestNG unit tests for Chromedriver JS errors collector. */
@Listeners(JSErrorsCollectorListener.class)
public class JSCollectorTestNGTest {

    private WebDriver driver;
    private Site88Page site88Page;

    // Define timeout before closing browser after test.
    private final int BROWSER_WAIT_MILLISECONDS = 4000;

    /** Static method that sets WebDriver system property. */
    @BeforeClass
    static void setChromeDriver() {
        System.setProperty("webdriver.chrome.driver",
                System.getProperty("user.dir")
                        + "/src/main/java/com/github/automatedowl" +
                        "/tools/drivers/webdriver/chromedriver");
    }

    @BeforeMethod
    void setDriverForListener(Method method) {
        driver = new ChromeDriver();
        site88Page = new Site88Page(driver);
        JSErrorsDriverHolder.setDriverForTest(method.getName(), driver);
    }

    /** Test method.
     * It should receive JS reference error and expect related exception. */
    @Test
    @JSErrorsCollectorTestNG
    void referenceErrorTest() throws InterruptedException {
        site88Page.navigateToPage(driver);
        site88Page.getTestButton().click();
        waitBeforeClosingBrowser();
    }

    /** Test method.
     * It should receive JS error but not fail the test due boolean flag false value. */
    @Test
    @JSErrorsCollectorTestNG(assertJSErrors = false)
    void assertJSErrorsFalseTest() throws InterruptedException {
        site88Page.navigateToPage(driver);
        site88Page.getTestButton().click();
        waitBeforeClosingBrowser();
    }

    /** Test method.
     * It should not receive any JS error and end without exception. */
    @Test
    @JSErrorsCollectorTestNG
    void noJSErrorsTest() throws InterruptedException {
        BlankPage blankPage = new BlankPage();
        blankPage.navigateToPage(driver);
        waitBeforeClosingBrowser();
    }

    /** Test method.
     * It should not use JSErrorsCollectorTestNG annotation. */
    @Test
    void noAnnotationTest() {
        Assert.assertTrue(true);
    }

    @AfterMethod
    void closeDriver() {
        driver.quit();
    }

    private void waitBeforeClosingBrowser() throws InterruptedException {
        Thread.sleep(BROWSER_WAIT_MILLISECONDS);
    }
}
