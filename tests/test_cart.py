import re

import pytest
from playwright.sync_api import Page, expect

from pages.cart_page import CartPage
from pages.inventory_page import InventoryPage


PRODUCT_BACKPACK = "Sauce Labs Backpack"


@pytest.mark.regression
def test_remove_item_on_cart_page_clears_badge(
    logged_in_inventory: InventoryPage,
    cart_page: CartPage,
) -> None:
    logged_in_inventory.add_item_by_name(PRODUCT_BACKPACK)
    logged_in_inventory.open_cart()
    cart_page.expect_loaded()
    cart_page.remove_item_by_name(PRODUCT_BACKPACK)
    expect(cart_page.cart_items).to_have_count(0)
    expect(cart_page.cart_badge).to_have_count(0)


@pytest.mark.regression
def test_continue_shopping_returns_to_inventory(
    logged_in_inventory: InventoryPage,
    cart_page: CartPage,
    page: Page,
) -> None:
    logged_in_inventory.add_item_by_name(PRODUCT_BACKPACK)
    logged_in_inventory.open_cart()
    cart_page.expect_loaded()
    cart_page.continue_shopping()
    expect(page).to_have_url(re.compile(r".*inventory\.html$"))
