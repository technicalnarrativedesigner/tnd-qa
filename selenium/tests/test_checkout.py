"""Selenium checkout tests for order completion and validation rules.

This module validates the primary purchase flow and required-field validation
behavior for checkout step one.
"""

import pytest
from selenium.webdriver.remote.webdriver import WebDriver

from pages.cart_page import CartPage
from pages.checkout_page import CheckoutPage
from pages.inventory_page import InventoryPage


PRODUCT_BACKPACK = "Sauce Labs Backpack"


def _add_backpack_and_open_checkout(
    inventory: InventoryPage,
    cart_page: CartPage,
    checkout_page: CheckoutPage,
) -> None:
    """Shared setup: add one product and navigate into checkout step one."""
    inventory.add_item_by_name(PRODUCT_BACKPACK)
    inventory.open_cart()
    cart_page.expect_loaded()
    cart_page.proceed_to_checkout()


@pytest.mark.smoke
def test_complete_purchase_shows_confirmation(
    logged_in_inventory: InventoryPage,
    cart_page: CartPage,
    checkout_page: CheckoutPage,
) -> None:
    """Verify end-to-end checkout shows the success confirmation message."""
    _add_backpack_and_open_checkout(logged_in_inventory, cart_page, checkout_page)
    checkout_page.fill_customer_info("Ada", "Lovelace", "12345")
    checkout_page.continue_to_overview()
    checkout_page.expect_step_two_loaded()
    checkout_page.finish_order()
    assert checkout_page.complete_header.text == "Thank you for your order!"


@pytest.mark.regression
@pytest.mark.parametrize(
    "first_name,last_name,postal_code,expected_fragment",
    [
        ("", "Lovelace", "12345", "First Name is required"),
        ("Ada", "Lovelace", "", "Postal Code is required"),
    ],
)
def test_checkout_validation_errors(
    logged_in_inventory: InventoryPage,
    cart_page: CartPage,
    checkout_page: CheckoutPage,
    first_name: str,
    last_name: str,
    postal_code: str,
    expected_fragment: str,
) -> None:
    """Verify required checkout fields return the expected validation text."""
    _add_backpack_and_open_checkout(logged_in_inventory, cart_page, checkout_page)
    checkout_page.fill_customer_info(first_name, last_name, postal_code)
    checkout_page.continue_to_overview()
    error = checkout_page.wait_for_validation_error()
    assert expected_fragment in error.text


@pytest.mark.regression
def test_cancel_checkout_returns_to_cart(
    logged_in_inventory: InventoryPage,
    cart_page: CartPage,
    checkout_page: CheckoutPage,
    driver: WebDriver,
) -> None:
    """Verify cancel from checkout step one returns user to cart page."""
    _add_backpack_and_open_checkout(logged_in_inventory, cart_page, checkout_page)
    checkout_page.cancel_checkout()
    assert "cart.html" in driver.current_url
