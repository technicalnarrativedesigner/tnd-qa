import re

import pytest
from selenium.webdriver.common.by import By
from selenium.webdriver.remote.webdriver import WebDriver
from selenium.webdriver.support import expected_conditions as EC

from config import Settings
from pages.inventory_page import InventoryPage
from pages.login_page import LoginPage


@pytest.mark.smoke
def test_valid_login_redirects_to_inventory(
    login_page: LoginPage,
    inventory_page: InventoryPage,
    settings: Settings,
    driver: WebDriver,
) -> None:
    login_page.open()
    login_page.login(settings.standard_user, settings.standard_password)
    inventory_page.expect_loaded()
    assert re.search(r".*inventory\.html$", driver.current_url)


@pytest.mark.regression
def test_invalid_credentials_show_error(login_page: LoginPage) -> None:
    login_page.open()
    login_page.login("invalid_user", "wrong_password")
    error = login_page.error_message
    assert error.is_displayed()
    assert "Username and password do not match any user in this service" in error.text


@pytest.mark.regression
def test_locked_out_user_shows_error(
    login_page: LoginPage,
    settings: Settings,
) -> None:
    login_page.open()
    login_page.login(settings.locked_out_user, settings.locked_out_password)
    error = login_page.error_message
    assert error.is_displayed()
    assert "Sorry, this user has been locked out." in error.text


@pytest.mark.regression
def test_logout_returns_to_login(
    logged_in_inventory: InventoryPage,
    login_page: LoginPage,
    driver: WebDriver,
) -> None:
    logged_in_inventory.logout()
    login_page.wait.until(EC.presence_of_element_located((By.ID, "user-name")))
    assert "inventory.html" not in driver.current_url
