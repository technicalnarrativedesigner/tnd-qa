package com.tnd.selenium.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.tnd.selenium.config.TestConfig;
import com.tnd.selenium.pages.CartPage;
import com.tnd.selenium.pages.CheckoutPage;
import com.tnd.selenium.pages.InventoryPage;
import com.tnd.selenium.pages.LoginPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Minimal Selenium Java flow suite.
 *
 * <p>What is tested:
 * login success, cart mutation signal (badge), and checkout happy-path completion.
 */
class SeleniumFlowsTest {
    private static final String PRODUCT_BACKPACK = "Sauce Labs Backpack";

    private WebDriver driver;
    private TestConfig config;
    private LoginPage loginPage;
    private InventoryPage inventoryPage;
    private CartPage cartPage;
    private CheckoutPage checkoutPage;

    /**
     * Initializes browser and page objects before each test for isolation.
     */
    @BeforeEach
    void setUp() {
        // Keep per-test browser sessions isolated, similar to pytest fixtures.
        config = TestConfig.load();
        String chromeBinary = System.getenv().getOrDefault("CHROME_BIN", "/usr/bin/chromium");
        String chromeVersion = commandOutput(chromeBinary + " --version");
        String driverVersion = commandOutput("chromedriver --version");

        ChromeOptions options = new ChromeOptions();
        options.setBinary(chromeBinary);
        if (!config.headed()) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--window-size=1280,720", "--disable-gpu", "--disable-dev-shm-usage", "--no-sandbox");
        // #region agent log
        System.out.println("{\"sessionId\":\"3ba741\",\"runId\":\"pre-fix\",\"hypothesisId\":\"H3\",\"location\":\"SeleniumFlowsTest.setUp\",\"message\":\"Java Selenium startup params\",\"data\":{\"chromeBinary\":\""
                + chromeBinary
                + "\",\"headed\":"
                + config.headed()
                + ",\"chromeVersion\":\""
                + chromeVersion.replace("\"", "\\\"")
                + "\",\"driverVersion\":\""
                + driverVersion.replace("\"", "\\\"")
                + "},\"timestamp\":"
                + System.currentTimeMillis()
                + "}");
        // #endregion
        try {
            driver = new ChromeDriver(options);
        } catch (SessionNotCreatedException e) {
            // #region agent log
            System.out.println("{\"sessionId\":\"3ba741\",\"runId\":\"pre-fix\",\"hypothesisId\":\"H4\",\"location\":\"SeleniumFlowsTest.setUp\",\"message\":\"Java Selenium session creation failed\",\"data\":{\"error\":\""
                    + e.getClass().getSimpleName()
                    + "\",\"message\":\""
                    + e.getMessage().replace("\"", "\\\"").replace("\n", "\\n")
                    + "\"},\"timestamp\":"
                    + System.currentTimeMillis()
                    + "}");
            // #endregion
            throw e;
        } catch (WebDriverException e) {
            // #region agent log
            System.out.println("{\"sessionId\":\"3ba741\",\"runId\":\"pre-fix\",\"hypothesisId\":\"H5\",\"location\":\"SeleniumFlowsTest.setUp\",\"message\":\"Java Selenium generic webdriver failure\",\"data\":{\"error\":\""
                    + e.getClass().getSimpleName()
                    + "\",\"message\":\""
                    + e.getMessage().replace("\"", "\\\"").replace("\n", "\\n")
                    + "\"},\"timestamp\":"
                    + System.currentTimeMillis()
                    + "}");
            // #endregion
            throw e;
        }
        loginPage = new LoginPage(driver, config.baseUrl());
        inventoryPage = new InventoryPage(driver, config.baseUrl());
        cartPage = new CartPage(driver, config.baseUrl());
        checkoutPage = new CheckoutPage(driver, config.baseUrl());
    }

    private String commandOutput(String command) {
        try {
            Process process = new ProcessBuilder("sh", "-c", command).start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line = reader.readLine();
                process.waitFor();
                return line == null ? "" : line.trim();
            }
        } catch (Exception e) {
            return "unavailable";
        }
    }

    /**
     * Shuts down browser after each test.
     */
    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    /**
     * Verifies valid login lands on inventory page.
     */
    @Test
    void validLoginRedirectsToInventory() {
        loginPage.open();
        loginPage.login(config.standardUser(), config.standardPassword());
        inventoryPage.expectLoaded();
        assertTrue(driver.getCurrentUrl().contains("inventory.html"));
    }

    /**
     * Verifies adding one product updates cart badge to one.
     */
    @Test
    void addItemUpdatesCartBadge() {
        loginPage.open();
        loginPage.login(config.standardUser(), config.standardPassword());
        inventoryPage.expectLoaded();
        inventoryPage.addItemByName(PRODUCT_BACKPACK);
        // Badge value is a compact assertion for cart state transition.
        assertEquals("1", inventoryPage.cartBadgeText());
    }

    /**
     * Verifies checkout happy path reaches expected order confirmation banner.
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
