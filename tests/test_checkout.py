import re

import pytest
from playwright.sync_api import Page, expect

from pages.cart_page import CartPage
from pages.checkout_page import CheckoutPage
from pages.inventory_page import InventoryPage


PRODUCT_BACKPACK = "Sauce Labs Backpack"


def _add_backpack_and_open_checkout(
    inventory: InventoryPage,
    cart_page: CartPage,
    checkout_page: CheckoutPage,
) -> None:
    inventory.add_item_by_name(PRODUCT_BACKPACK)
    inventory.open_cart()
    cart_page.expect_loaded()
    cart_page.proceed_to_checkout()
    checkout_page.expect_step_one_loaded()


@pytest.mark.smoke
def test_complete_purchase_shows_confirmation(
    logged_in_inventory: InventoryPage,
    cart_page: CartPage,
    checkout_page: CheckoutPage,
) -> None:
    _add_backpack_and_open_checkout(logged_in_inventory, cart_page, checkout_page)
    checkout_page.fill_customer_info("Ada", "Lovelace", "12345")
    checkout_page.continue_to_overview()
    checkout_page.expect_step_two_loaded()
    checkout_page.finish_order()
    checkout_page.expect_complete_loaded()
    expect(checkout_page.complete_header).to_have_text("Thank you for your order!")


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
    _add_backpack_and_open_checkout(logged_in_inventory, cart_page, checkout_page)
    checkout_page.fill_customer_info(first_name, last_name, postal_code)
    checkout_page.continue_to_overview()
    expect(checkout_page.error_message).to_be_visible()
    expect(checkout_page.error_message).to_contain_text(expected_fragment)


@pytest.mark.regression
def test_cancel_checkout_returns_to_cart(
    logged_in_inventory: InventoryPage,
    cart_page: CartPage,
    checkout_page: CheckoutPage,
    page: Page,
) -> None:
    _add_backpack_and_open_checkout(logged_in_inventory, cart_page, checkout_page)
    checkout_page.cancel_checkout()
    expect(page).to_have_url(re.compile(r".*cart\.html$"))
