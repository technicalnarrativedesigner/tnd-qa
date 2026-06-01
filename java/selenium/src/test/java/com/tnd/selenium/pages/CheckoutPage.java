package com.tnd.selenium.pages;

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
                "H2",
                "CheckoutPage.fillCustomerInfo",
                "Checkout inputs populated",
                "{\"url\":\""
                        + jsonEscape(driver.getCurrentUrl())
                        + "\",\"firstLen\":"
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
                "H1",
                "CheckoutPage.continueToOverview",
                "Continue button state before submit",
                "{\"url\":\""
                        + jsonEscape(driver.getCurrentUrl())
                        + "\",\"displayed\":"
                        + continueButton.isDisplayed()
                        + ",\"enabled\":"
                        + continueButton.isEnabled()
                        + "}"
        );
        // #endregion

        click(CONTINUE);

        String urlAfterClick = driver.getCurrentUrl();
        boolean hasError = !driver.findElements(ERROR_MESSAGE).isEmpty();
        String errorText = hasError ? driver.findElement(ERROR_MESSAGE).getText().trim() : "";
        // #region agent log
        debugLog(
                "pre-fix",
                "H3",
                "CheckoutPage.continueToOverview",
                "State immediately after continue click",
                "{\"url\":\""
                        + jsonEscape(urlAfterClick)
                        + "\",\"hasError\":"
                        + hasError
                        + ",\"errorText\":\""
                        + jsonEscape(errorText)
                        + "\"}"
        );
        // #endregion

        try {
            wait.until(ExpectedConditions.urlContains("checkout-step-two.html"));
            // #region agent log
            debugLog(
                    "pre-fix",
                    "H4",
                    "CheckoutPage.continueToOverview",
                    "Reached checkout step two",
                    "{\"url\":\"" + jsonEscape(driver.getCurrentUrl()) + "\"}"
            );
            // #endregion
        } catch (TimeoutException e) {
            String timeoutUrl = driver.getCurrentUrl();
            boolean timeoutHasError = !driver.findElements(ERROR_MESSAGE).isEmpty();
            String timeoutErrorText = timeoutHasError ? driver.findElement(ERROR_MESSAGE).getText().trim() : "";
            // #region agent log
            debugLog(
                    "pre-fix",
                    "H5",
                    "CheckoutPage.continueToOverview",
                    "Timed out waiting for checkout step two",
                    "{\"url\":\""
                            + jsonEscape(timeoutUrl)
                            + "\",\"hasError\":"
                            + timeoutHasError
                            + ",\"errorText\":\""
                            + jsonEscape(timeoutErrorText)
                            + "\"}"
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
}
