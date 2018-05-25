package net.autobox.tools.testng.drivers;

import java.util.HashMap;
import java.util.Map;
import org.openqa.selenium.WebDriver;

/** Static class for holding Chromedriver session during the test. */
public class JSErrorsDriverHolder {

    /** HashMap object which points test name to its Chromedriver session. */
    private static Map<String, WebDriver> testToDriverMap = new HashMap<>();

    public synchronized static void setDriverForTest(
            String displayName, WebDriver driver) {
        testToDriverMap.put(displayName, driver);
    }

    public synchronized static WebDriver getDriverForTest(
            String testName) {
        return testToDriverMap.get(testName);
    }
}
