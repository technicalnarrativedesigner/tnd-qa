package com.tnd.playwright.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

/**
 * Page object for SauceDemo inventory screen (Playwright Java).
 *
 * <p>Purpose: model product-list interactions that drive cart and checkout flows.
 */
public class InventoryPage extends BasePage {
    /**
     * Creates inventory page object for product listing operations.
     */
    public InventoryPage(Page page, String baseUrl) {
        super(page, baseUrl);
    }

    /**
     * Waits until user is on inventory route after successful login.
     */
    public void expectLoaded() {
        page.waitForURL("**/inventory.html");
    }

    /**
     * Adds a specific product to cart by matching visible item name.
     */
    public void addItemByName(String productName) {
        // Scope the button lookup to one card by visible product name.
        Locator item = page.locator(".inventory_item")
                .filter(new Locator.FilterOptions().setHasText(productName));
        item.getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName("Add to cart")).click();
    }

    /**
     * Returns cart badge value used by tests to verify cart state mutation.
     */
    public String cartBadgeText() {
        return page.locator(".shopping_cart_badge").innerText().trim();
    }

    /**
     * Opens cart page from inventory top bar.
     */
    public void openCart() {
        page.locator(".shopping_cart_link").click();
        page.waitForURL("**/cart.html");
    }
}
