package com.tnd.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Page object for SauceDemo cart screen (Selenium Java).
 *
 * <p>Purpose: transition from cart into checkout pipeline.
 */
public class CartPage extends BasePage {
    private static final By CHECKOUT = By.id("checkout");

    /**
     * Creates cart page object.
     */
    public CartPage(WebDriver driver, String baseUrl) {
        super(driver, baseUrl);
    }

    /**
     * Waits until cart route is active.
     */
    public void expectLoaded() {
        wait.until(ExpectedConditions.urlContains("cart.html"));
    }

    /**
     * Starts checkout and waits for step-one route.
     */
    public void checkout() {
        click(CHECKOUT);
        wait.until(ExpectedConditions.urlContains("checkout-step-one.html"));
    }
}
