# chromedriver-js-errors-collector

Java library which allows to easily collect JS errors received in Chromedriver session, using annotations on test methods.

Currently, the library supports JUnit5 and TestNG testing framework.

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
        driver.get("http://testjs.site88.net");

        // The click on the button in the test site should cause JS reference error.
        driver.findElement(By.name("testClickButton")).click();
        waitBeforeClosingBrowser();
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
        driver.get("http://testjs.site88.net");

        // The click on the button in the test site should cause JS reference error.
        driver.findElement(By.name("testClickButton")).click();
        waitBeforeClosingBrowser();
    }
```

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
            <version>1.0.1</version>
        </dependency>
```

TestNG:
```
        <dependency>
            <groupId>com.github.automatedowl</groupId>
            <artifactId>chromedriver-js-errors-collector-testng</artifactId>
            <version>1.0.1</version>
        </dependency>
```
