package com.tnd.playwright.pages;

import com.microsoft.playwright.Page;

/**
 * Base class for Java Playwright page objects.
 *
 * <p>Purpose: centralize shared navigation behavior so concrete page classes
 * focus only on domain actions (login, cart, checkout), not URL plumbing.
 */
public class BasePage {
    protected final Page page;
    protected final String baseUrl;

    /**
     * Creates a reusable page-object base with Playwright page handle and app base URL.
     */
    public BasePage(Page page, String baseUrl) {
        this.page = page;
        this.baseUrl = baseUrl;
    }

    /**
     * Navigates to either an absolute URL or a path relative to the configured base URL.
     */
    protected void goTo(String path) {
        // Allow either app-relative paths or absolute URLs.
        if (path.startsWith("http")) {
            page.navigate(path);
        } else {
            page.navigate(baseUrl + path);
        }
    }
}
