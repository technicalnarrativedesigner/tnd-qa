"""Playwright authentication tests for the SauceDemo login flow.

This module validates happy-path login plus key negative auth scenarios that
are common regression targets in UI automation suites.
"""

import re

import pytest
from playwright.sync_api import Page, expect

from config import Settings
from pages.inventory_page import InventoryPage
from pages.login_page import LoginPage


@pytest.mark.smoke
def test_valid_login_redirects_to_inventory(
    login_page: LoginPage,
    inventory_page: InventoryPage,
    settings: Settings,
    page: Page,
) -> None:
    """Verify a valid user can sign in and lands on the inventory page."""
    login_page.open()
    login_page.login(settings.standard_user, settings.standard_password)
    inventory_page.expect_loaded()
    expect(page).to_have_url(re.compile(r".*inventory\.html$"))


@pytest.mark.regression
def test_invalid_credentials_show_error(
    login_page: LoginPage,
) -> None:
    """Verify invalid credentials show the expected authentication error."""
    login_page.open()
    login_page.login("invalid_user", "wrong_password")
    expect(login_page.error_message).to_be_visible()
    expect(login_page.error_message).to_contain_text(
        "Username and password do not match any user in this service"
    )


@pytest.mark.regression
def test_locked_out_user_shows_error(
    login_page: LoginPage,
    settings: Settings,
) -> None:
    """Verify a locked account is rejected with the lockout message."""
    login_page.open()
    login_page.login(settings.locked_out_user, settings.locked_out_password)
    expect(login_page.error_message).to_be_visible()
    expect(login_page.error_message).to_contain_text(
        "Sorry, this user has been locked out."
    )


@pytest.mark.regression
def test_logout_returns_to_login(
    logged_in_inventory: InventoryPage,
    login_page: LoginPage,
    page: Page,
) -> None:
    """Verify logout invalidates the session and returns to login screen."""
    logged_in_inventory.logout()
    expect(page).to_have_url(re.compile(r".*/$"))
    expect(page.locator("#login-button")).to_be_visible()
