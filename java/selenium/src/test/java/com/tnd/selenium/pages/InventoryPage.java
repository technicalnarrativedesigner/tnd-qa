package com.tnd.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Page object for SauceDemo inventory screen (Selenium Java).
 *
 * <p>Purpose: product list operations that feed cart and checkout tests.
 */
public class InventoryPage extends BasePage {
    private static final By CART_BADGE = By.cssSelector(".shopping_cart_badge");
    private static final By CART_LINK = By.cssSelector(".shopping_cart_link");

    /**
     * Creates inventory page object.
     */
    public InventoryPage(WebDriver driver, String baseUrl) {
        super(driver, baseUrl);
    }

    /**
     * Waits until inventory route is loaded.
     */
    public void expectLoaded() {
        wait.until(ExpectedConditions.urlContains("inventory.html"));
    }

    /**
     * Adds a product by visible name using product-scoped locator.
     */
    public void addItemByName(String productName) {
        // Build a product-scoped XPath so we click the right card button.
        By addButton = By.xpath(
                "//div[contains(@class,'inventory_item')][.//div[contains(@class,'inventory_item_name') and normalize-space()='"
                        + productName
                        + "']]//button[contains(@id,'add-to-cart')]"
        );
        click(addButton);
    }

    /**
     * Returns cart badge text used by tests as state-change assertion.
     */
    public String cartBadgeText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(CART_BADGE)).getText().trim();
    }

    /**
     * Opens cart from top navigation icon.
     */
    public void openCart() {
        click(CART_LINK);
        wait.until(ExpectedConditions.urlContains("cart.html"));
    }
}
