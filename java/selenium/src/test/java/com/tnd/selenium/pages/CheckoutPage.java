package com.tnd.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Page object for SauceDemo checkout stages (Selenium Java).
 *
 * <p>Purpose: encapsulate checkout form, overview progression, and completion assertions.
 */
public class CheckoutPage extends BasePage {
    private static final By FIRST_NAME = By.id("first-name");
    private static final By LAST_NAME = By.id("last-name");
    private static final By POSTAL_CODE = By.id("postal-code");
    private static final By CONTINUE = By.id("continue");
    private static final By FINISH = By.id("finish");
    private static final By COMPLETE_HEADER = By.cssSelector(".complete-header");

    /**
     * Creates checkout page object.
     */
    public CheckoutPage(WebDriver driver, String baseUrl) {
        super(driver, baseUrl);
    }

    /**
     * Populates customer identity fields in checkout step one.
     */
    public void fillCustomerInfo(String firstName, String lastName, String postalCode) {
        // Encapsulate checkout form entry to keep tests business-readable.
        fill(FIRST_NAME, firstName);
        fill(LAST_NAME, lastName);
        fill(POSTAL_CODE, postalCode);
    }

    /**
     * Submits step one and waits for checkout overview route.
     */
    public void continueToOverview() {
        click(CONTINUE);
        wait.until(ExpectedConditions.urlContains("checkout-step-two.html"));
    }

    /**
     * Finishes order from checkout overview page.
     */
    public void finishOrder() {
        click(FINISH);
        wait.until(ExpectedConditions.urlContains("checkout-complete.html"));
    }

    /**
     * Returns completion banner text used in final success assertion.
     */
    public String completeHeaderText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(COMPLETE_HEADER)).getText().trim();
    }
}
