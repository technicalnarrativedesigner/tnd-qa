"""Playwright inventory tests focused on cart state from product listing.

These tests ensure add/remove actions on the inventory page correctly update
the cart badge and are reflected when the user opens the cart.
"""

import pytest
from playwright.sync_api import expect

from pages.cart_page import CartPage
from pages.inventory_page import InventoryPage


PRODUCT_BACKPACK = "Sauce Labs Backpack"
PRODUCT_BIKE_LIGHT = "Sauce Labs Bike Light"


@pytest.mark.smoke
def test_add_one_item_updates_cart_badge(logged_in_inventory: InventoryPage) -> None:
    """Verify adding one product increments the cart badge to 1."""
    logged_in_inventory.add_item_by_name(PRODUCT_BACKPACK)
    expect(logged_in_inventory.cart_badge).to_have_text("1")


@pytest.mark.regression
def test_add_two_items_updates_cart_badge(logged_in_inventory: InventoryPage) -> None:
    """Verify adding two products increments the cart badge to 2."""
    logged_in_inventory.add_item_by_name(PRODUCT_BACKPACK)
    logged_in_inventory.add_item_by_name(PRODUCT_BIKE_LIGHT)
    expect(logged_in_inventory.cart_badge).to_have_text("2")


@pytest.mark.regression
def test_remove_item_from_inventory_decrements_badge(
    logged_in_inventory: InventoryPage,
) -> None:
    """Verify removing one of two selected items decrements badge count."""
    logged_in_inventory.add_item_by_name(PRODUCT_BACKPACK)
    logged_in_inventory.add_item_by_name(PRODUCT_BIKE_LIGHT)
    logged_in_inventory.remove_item_by_name(PRODUCT_BACKPACK)
    expect(logged_in_inventory.cart_badge).to_have_text("1")


@pytest.mark.regression
def test_cart_page_lists_added_items(
    logged_in_inventory: InventoryPage,
    cart_page: CartPage,
) -> None:
    """Verify the cart contains exactly the products selected in inventory."""
    logged_in_inventory.add_item_by_name(PRODUCT_BACKPACK)
    logged_in_inventory.add_item_by_name(PRODUCT_BIKE_LIGHT)
    logged_in_inventory.open_cart()
    cart_page.expect_loaded()
    expect(cart_page.cart_items).to_have_count(2)
    expect(cart_page.cart_items.filter(has_text=PRODUCT_BACKPACK)).to_have_count(1)
    expect(cart_page.cart_items.filter(has_text=PRODUCT_BIKE_LIGHT)).to_have_count(1)
