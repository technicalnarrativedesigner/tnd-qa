package com.tnd.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page object for SauceDemo login screen (Selenium Java).
 *
 * <p>Purpose: encapsulate authentication interactions reused across flow tests.
 */
public class LoginPage extends BasePage {
    private static final By USERNAME = By.id("user-name");
    private static final By PASSWORD = By.id("password");
    private static final By LOGIN = By.id("login-button");

    /**
     * Creates login page object.
     */
    public LoginPage(WebDriver driver, String baseUrl) {
        super(driver, baseUrl);
    }

    /**
     * Opens login route.
     */
    public void open() {
        goTo("/");
    }

    /**
     * Submits credentials for authentication flow.
     */
    public void login(String username, String password) {
        fill(USERNAME, username);
        fill(PASSWORD, password);
        click(LOGIN);
    }
}
