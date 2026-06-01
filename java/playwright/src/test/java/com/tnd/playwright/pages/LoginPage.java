package com.tnd.playwright.pages;

import com.microsoft.playwright.Page;

/**
 * Page object for SauceDemo login screen (Playwright Java).
 *
 * <p>Purpose: encapsulate auth-related UI actions used by all flow tests.
 */
public class LoginPage extends BasePage {
    /**
     * Creates login page object bound to current browser page and base URL.
     */
    public LoginPage(Page page, String baseUrl) {
        super(page, baseUrl);
    }

    /**
     * Opens the login entry route.
     */
    public void open() {
        goTo("/");
    }

    /**
     * Performs credential submission for positive/negative auth scenarios.
     */
    public void login(String username, String password) {
        // Shared login action used by all core flow tests.
        page.locator("#user-name").fill(username);
        page.locator("#password").fill(password);
        page.locator("#login-button").click();
    }
}
