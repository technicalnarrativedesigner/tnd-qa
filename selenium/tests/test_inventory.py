import pytest

from pages.cart_page import CartPage
from pages.inventory_page import InventoryPage


PRODUCT_BACKPACK = "Sauce Labs Backpack"
PRODUCT_BIKE_LIGHT = "Sauce Labs Bike Light"


@pytest.mark.smoke
def test_add_one_item_updates_cart_badge(logged_in_inventory: InventoryPage) -> None:
    logged_in_inventory.add_item_by_name(PRODUCT_BACKPACK)
    assert logged_in_inventory.cart_badge.text == "1"


@pytest.mark.regression
def test_add_two_items_updates_cart_badge(logged_in_inventory: InventoryPage) -> None:
    logged_in_inventory.add_item_by_name(PRODUCT_BACKPACK)
    logged_in_inventory.add_item_by_name(PRODUCT_BIKE_LIGHT)
    assert logged_in_inventory.cart_badge.text == "2"


@pytest.mark.regression
def test_remove_item_from_inventory_decrements_badge(
    logged_in_inventory: InventoryPage,
) -> None:
    logged_in_inventory.add_item_by_name(PRODUCT_BACKPACK)
    logged_in_inventory.add_item_by_name(PRODUCT_BIKE_LIGHT)
    logged_in_inventory.remove_item_by_name(PRODUCT_BACKPACK)
    assert logged_in_inventory.cart_badge.text == "1"


@pytest.mark.regression
def test_cart_page_lists_added_items(
    logged_in_inventory: InventoryPage,
    cart_page: CartPage,
) -> None:
    logged_in_inventory.add_item_by_name(PRODUCT_BACKPACK)
    logged_in_inventory.add_item_by_name(PRODUCT_BIKE_LIGHT)
    logged_in_inventory.open_cart()
    cart_page.expect_loaded()
    assert len(cart_page.cart_items) == 2
    product_names = cart_page.get_product_names()
    assert product_names.count(PRODUCT_BACKPACK) == 1
    assert product_names.count(PRODUCT_BIKE_LIGHT) == 1
