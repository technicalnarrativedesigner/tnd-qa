package com.tnd.selenium.pages;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Base class for Java Selenium page objects.
 *
 * <p>Purpose: centralize wait/click/fill primitives so page classes stay focused on behavior.
 */
public class BasePage {
    private static final String DEBUG_LOG_PATH =
            "/home/technicalnarrativedesigner/Encrypted/Projects/TND/tnd-qa/.cursor/debug-3ba741.log";
    private static final String DEBUG_SESSION_ID = "3ba741";

    protected final WebDriver driver;
    protected final WebDriverWait wait;
    protected final String baseUrl;

    /**
     * Creates reusable Selenium page-object foundation with explicit wait strategy.
     */
    public BasePage(WebDriver driver, String baseUrl) {
        this.driver = driver;
        this.baseUrl = baseUrl;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    /**
     * Navigates to either absolute URL or route relative to base URL.
     */
    protected void goTo(String path) {
        if (path.startsWith("http")) {
            driver.get(path);
        } else {
            driver.get(baseUrl + path);
        }
    }

    /**
     * Clicks element using JS after wait/scroll to reduce headless flakiness.
     */
    protected void click(By locator) {
        // JS click keeps interactions stable in headless or scroll-heavy pages.
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    /**
     * Fills input field after visibility wait.
     */
    protected void fill(By locator, String value) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.click();
        element.clear();
        element.sendKeys(value);

        // Some CI/headless runs on React-like forms can drop sendKeys updates.
        // If value did not stick, force-set value and dispatch input/change events.
        String currentValue = element.getAttribute("value");
        if (currentValue == null || !currentValue.equals(value)) {
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].value = arguments[1];"
                            + "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));"
                            + "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
                    element,
                    value
            );
        }
    }

    /**
     * Writes one NDJSON debug event for runtime hypothesis validation.
     */
    protected void debugLog(String runId, String hypothesisId, String location, String message, String dataJson) {
        String line = "{\"sessionId\":\""
                + jsonEscape(DEBUG_SESSION_ID)
                + "\",\"runId\":\""
                + jsonEscape(runId)
                + "\",\"hypothesisId\":\""
                + jsonEscape(hypothesisId)
                + "\",\"location\":\""
                + jsonEscape(location)
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
            Files.writeString(
                    logPath,
                    line + System.lineSeparator(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );
        } catch (IOException ignored) {
            // Never fail the test because of debug logging.
        }
        System.out.println(line);
    }

    /**
     * Escapes string content for JSON literals in debug events.
     */
    protected String jsonEscape(String value) {
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
