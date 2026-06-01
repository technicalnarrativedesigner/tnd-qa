package com.tnd.playwright.pages;

import com.microsoft.playwright.Page;

/**
 * Page object for SauceDemo cart screen (Playwright Java).
 *
 * <p>Purpose: encapsulate cart-level actions required before checkout.
 */
public class CartPage extends BasePage {
    /**
     * Creates cart page object bound to active Playwright page.
     */
    public CartPage(Page page, String baseUrl) {
        super(page, baseUrl);
    }

    /**
     * Asserts route transition to cart page.
     */
    public void expectLoaded() {
        page.waitForURL("**/cart.html");
    }

    /**
     * Moves from cart page to checkout step one.
     */
    public void checkout() {
        page.locator("#checkout").click();
        page.waitForURL("**/checkout-step-one.html");
    }
}
