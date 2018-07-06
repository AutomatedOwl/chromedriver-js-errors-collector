# chromedriver-js-errors-collector

Java library which allows to easily collect JavaScript errors received in Chromedriver session, using annotations on test methods. Currently, the library supports JUnit5 and TestNG testing framework.



### Example of JUnit5 usage:

```
    @Test
    @JSErrorsCollectorJUnit
    void referenceErrorTest(TestInfo testInfo) throws InterruptedException {

        // Create a new instance of ChromeDriver.
        driver = new ChromeDriver();

        // Set your test name to point its ChromeDriver session in HashMap.
        JSErrorsDriverHolder.setDriverForTest(testInfo.getDisplayName(), driver);

        // Navigate to URL.
        site88Page.navigateToPage(driver);

        // The click on the button in the test site should cause JS reference error.
        site88Page.getTestButton().click();
        waitBeforeClosingBrowser();
    }
    
    @AfterEach
    void closeDriver() {
        driver.quit();
    }
```

### Example of TestNG usage:

```
@Listeners(JSErrorsCollectorListener.class)
public class JSCollectorTestNGTest {

    private WebDriver driver;

    @BeforeMethod
    void setDriverForListener(Method method) {

        // Create a new instance of ChromeDriver.
        driver = new ChromeDriver();

        // Set your test name to point its ChromeDriver session in HashMap.
        JSErrorsDriverHolder.setDriverForTest(method.getName(), driver);
    }

    /** Test method.
     * It should receive JS reference error and expect related exception. */
    @Test
    @JSErrorsCollectorTestNG
    void referenceErrorTest() throws InterruptedException {

        // Navigate to URL.
        site88Page.navigateToPage(driver);

        // The click on the button in the test site should cause JS reference error.
        site88Page.getTestButton().click();
        waitBeforeClosingBrowser();
    }
    
    @AfterMethod
    void closeDriver() {
        driver.quit();
    }
```

### Closing your browser session

In order for the errors comparison to work properly, you should use 'AfterMethod' in TestNG and 'AfterEach' in JUnit in order to call driver.quit(). This would allow the listeners to interact with your WebDriver object after test execution. 

### Annotation values

By default, using the annotation will cause your test to fail on JS errors received during Chromedriver session,
and it would also use java.util.logging.Logger object to log JS errors after test execution.

To disable asserting JS errors after test execution, use:

```
@JSErrorsCollectorTestNG(assertJSErrors = false)
```

To disable logging JS errors after test execution, use:

```
@JSErrorsCollectorTestNG(logJSErrors = false)
```

To disable both, use:

```
@JSErrorsCollectorTestNG(logJSErrors = false, assertJSErrors = false)
```

### Maven dependencies

JUnit5:
```
    <dependency>
        <groupId>com.github.automatedowl</groupId>
        <artifactId>chromedriver-js-errors-collector-junit</artifactId>
        <version>1.0.3</version>
    </dependency>
```

TestNG:
```
    <dependency>
        <groupId>com.github.automatedowl</groupId>
        <artifactId>chromedriver-js-errors-collector-testng</artifactId>
        <version>1.0.3</version>
    </dependency>
```
