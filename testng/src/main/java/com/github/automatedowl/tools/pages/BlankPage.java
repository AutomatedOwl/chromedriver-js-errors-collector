package com.github.automatedowl.tools.pages;

import org.openqa.selenium.WebDriver;

public class BlankPage {

    private static final String BLANK_PAGE_URL =
            "http://this-page-intentionally-left-blank.org";

    public void navigateToPage(WebDriver driver) {
        driver.get(BLANK_PAGE_URL);
    }
}
