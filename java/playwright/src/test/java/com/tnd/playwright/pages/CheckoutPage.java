package com.tnd.playwright.pages;

import com.microsoft.playwright.Page;

/**
 * Page object for SauceDemo checkout steps (Playwright Java).
 *
 * <p>Purpose: model customer-info, overview, and completion actions in one place.
 */
public class CheckoutPage extends BasePage {
    /**
     * Creates checkout page object.
     */
    public CheckoutPage(Page page, String baseUrl) {
        super(page, baseUrl);
    }

    /**
     * Populates checkout identity form fields used in happy-path flow.
     */
    public void fillCustomerInfo(String firstName, String lastName, String postalCode) {
        // Keep checkout identity data entry in one reusable action.
        page.locator("#first-name").fill(firstName);
        page.locator("#last-name").fill(lastName);
        page.locator("#postal-code").fill(postalCode);
    }

    /**
     * Submits checkout step one and waits until overview step is reached.
     */
    public void continueToOverview() {
        page.locator("#continue").click();
        page.waitForURL("**/checkout-step-two.html");
    }

    /**
     * Completes purchase from checkout overview.
     */
    public void finishOrder() {
        page.locator("#finish").click();
        page.waitForURL("**/checkout-complete.html");
    }

    /**
     * Returns visible completion banner text for final assertion.
     */
    public String completeHeaderText() {
        return page.locator(".complete-header").innerText().trim();
    }
}
