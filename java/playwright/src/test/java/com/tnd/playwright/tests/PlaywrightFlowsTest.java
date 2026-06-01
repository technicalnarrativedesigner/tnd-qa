package com.tnd.playwright.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.tnd.playwright.config.TestConfig;
import com.tnd.playwright.pages.CartPage;
import com.tnd.playwright.pages.CheckoutPage;
import com.tnd.playwright.pages.InventoryPage;
import com.tnd.playwright.pages.LoginPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Minimal Playwright Java flow suite.
 *
 * <p>What is tested:
 * login success, cart mutation signal (badge), and checkout happy path completion.
 */
class PlaywrightFlowsTest {
    private static final String PRODUCT_BACKPACK = "Sauce Labs Backpack";

    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private Page page;
    private TestConfig config;
    private LoginPage loginPage;
    private InventoryPage inventoryPage;
    private CartPage cartPage;
    private CheckoutPage checkoutPage;

    /**
     * Builds isolated browser/context/pages before each test to avoid state leakage.
     */
    @BeforeEach
    void setUp() {
        // Build an isolated browser context per test for deterministic state.
        config = TestConfig.load();
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(!config.headed()));
        context = browser.newContext();
        page = context.newPage();

        loginPage = new LoginPage(page, config.baseUrl());
        inventoryPage = new InventoryPage(page, config.baseUrl());
        cartPage = new CartPage(page, config.baseUrl());
        checkoutPage = new CheckoutPage(page, config.baseUrl());
    }

    /**
     * Releases browser resources after each test run.
     */
    @AfterEach
    void tearDown() {
        if (context != null) {
            context.close();
        }
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }

    /**
     * Verifies that valid credentials navigate user to inventory page.
     */
    @Test
    void validLoginRedirectsToInventory() {
        loginPage.open();
        loginPage.login(config.standardUser(), config.standardPassword());
        inventoryPage.expectLoaded();
        assertTrue(page.url().contains("inventory.html"));
    }

    /**
     * Verifies that adding one item updates cart badge to one.
     */
    @Test
    void addItemUpdatesCartBadge() {
        loginPage.open();
        loginPage.login(config.standardUser(), config.standardPassword());
        inventoryPage.expectLoaded();
        inventoryPage.addItemByName(PRODUCT_BACKPACK);
        // Badge text is the fastest assertion that cart state changed.
        assertEquals("1", inventoryPage.cartBadgeText());
    }

    /**
     * Verifies full checkout happy path reaches final confirmation message.
     */
    @Test
    void checkoutHappyPathShowsConfirmation() {
        loginPage.open();
        loginPage.login(config.standardUser(), config.standardPassword());
        inventoryPage.expectLoaded();
        inventoryPage.addItemByName(PRODUCT_BACKPACK);
        inventoryPage.openCart();
        cartPage.expectLoaded();
        cartPage.checkout();
        checkoutPage.fillCustomerInfo("Ada", "Lovelace", "12345");
        checkoutPage.continueToOverview();
        checkoutPage.finishOrder();
        assertEquals("Thank you for your order!", checkoutPage.completeHeaderText());
    }
}
