package com.tnd.selenium.pages;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Page object for SauceDemo checkout stages (Selenium Java).
 *
 * <p>Purpose: encapsulate checkout form, overview progression, and completion assertions.
 */
public class CheckoutPage extends BasePage {
    private static final String DEBUG_LOG_PATH =
            "/home/technicalnarrativedesigner/Encrypted/Projects/TND/tnd-qa/.cursor/debug-3ba741.log";
    private static final String DEBUG_SESSION_ID = "3ba741";
    private static final By FIRST_NAME = By.id("first-name");
    private static final By LAST_NAME = By.id("last-name");
    private static final By POSTAL_CODE = By.id("postal-code");
    private static final By CONTINUE = By.id("continue");
    private static final By FINISH = By.id("finish");
    private static final By COMPLETE_HEADER = By.cssSelector(".complete-header");
    private static final By ERROR_MESSAGE = By.cssSelector("[data-test='error']");

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
        String firstValue = driver.findElement(FIRST_NAME).getAttribute("value");
        String lastValue = driver.findElement(LAST_NAME).getAttribute("value");
        String postalValue = driver.findElement(POSTAL_CODE).getAttribute("value");
        // #region agent log
        debugLog(
                "pre-fix",
                "H1",
                "CheckoutPage.fillCustomerInfo",
                "Checkout field values after fill",
                "{\"firstLen\":"
                        + firstValue.length()
                        + ",\"lastLen\":"
                        + lastValue.length()
                        + ",\"postalLen\":"
                        + postalValue.length()
                        + "}"
        );
        // #endregion
    }

    /**
     * Submits step one and waits for checkout overview route.
     */
    public void continueToOverview() {
        WebElement continueButton = wait.until(ExpectedConditions.elementToBeClickable(CONTINUE));
        // #region agent log
        debugLog(
                "pre-fix",
                "H2",
                "CheckoutPage.continueToOverview",
                "Continue button ready state",
                "{\"displayed\":"
                        + continueButton.isDisplayed()
                        + ",\"enabled\":"
                        + continueButton.isEnabled()
                        + ",\"url\":\""
                        + jsonEscape(driver.getCurrentUrl())
                        + "\"}"
        );
        // #endregion

        click(CONTINUE);
        boolean hasErrorAfterClick = !driver.findElements(ERROR_MESSAGE).isEmpty();
        String errorAfterClick = hasErrorAfterClick ? driver.findElement(ERROR_MESSAGE).getText().trim() : "";
        // #region agent log
        debugLog(
                "pre-fix",
                "H3",
                "CheckoutPage.continueToOverview",
                "State after continue click",
                "{\"url\":\""
                        + jsonEscape(driver.getCurrentUrl())
                        + "\",\"hasError\":"
                        + hasErrorAfterClick
                        + ",\"errorText\":\""
                        + jsonEscape(errorAfterClick)
                        + "\"}"
        );
        // #endregion

        try {
            wait.until(ExpectedConditions.urlContains("checkout-step-two.html"));
        } catch (TimeoutException e) {
            String firstValue = driver.findElement(FIRST_NAME).getAttribute("value");
            String lastValue = driver.findElement(LAST_NAME).getAttribute("value");
            String postalValue = driver.findElement(POSTAL_CODE).getAttribute("value");
            boolean hasErrorOnTimeout = !driver.findElements(ERROR_MESSAGE).isEmpty();
            String errorOnTimeout = hasErrorOnTimeout ? driver.findElement(ERROR_MESSAGE).getText().trim() : "";
            // #region agent log
            debugLog(
                    "pre-fix",
                    "H4",
                    "CheckoutPage.continueToOverview",
                    "Timed out waiting for checkout step two",
                    "{\"url\":\""
                            + jsonEscape(driver.getCurrentUrl())
                            + "\",\"hasError\":"
                            + hasErrorOnTimeout
                            + ",\"errorText\":\""
                            + jsonEscape(errorOnTimeout)
                            + "\",\"firstLen\":"
                            + firstValue.length()
                            + ",\"lastLen\":"
                            + lastValue.length()
                            + ",\"postalLen\":"
                            + postalValue.length()
                            + "}"
            );
            // #endregion
            throw e;
        }
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

    private void debugLog(String runId, String hypothesisId, String location, String message, String dataJson) {
        String line = "{\"sessionId\":\""
                + DEBUG_SESSION_ID
                + "\",\"runId\":\""
                + runId
                + "\",\"hypothesisId\":\""
                + hypothesisId
                + "\",\"location\":\""
                + location
                + "\",\"message\":\""
                + jsonEscape(message)
                + "\",\"data\":"
                + dataJson
                + ",\"timestamp\":"
                + System.currentTimeMillis()
                + "}";
        try {
            Path logPath = Path.of(DEBUG_LOG_PATH);
            if (logPath.getParent() != null) {
                Files.createDirectories(logPath.getParent());
            }
            Files.writeString(logPath, line + System.lineSeparator(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException ignored) {
            // Keep debug instrumentation non-blocking.
        }
        System.out.println(line);
    }

    private String jsonEscape(String value) {
        if (value == null) {
            return "";
        }
        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}
