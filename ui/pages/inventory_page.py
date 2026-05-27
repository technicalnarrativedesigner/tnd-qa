from playwright.sync_api import Locator, Page

from pages.base_page import BasePage


class InventoryPage(BasePage):
    """Inventory interactions: add/remove products, cart, and logout."""

    def __init__(self, page: Page, base_url: str) -> None:
        super().__init__(page, base_url)
        self._cart_link: Locator = page.locator(".shopping_cart_link")
        self._cart_badge: Locator = page.locator(".shopping_cart_badge")
        self._inventory_items: Locator = page.locator(".inventory_item")
        self._menu_button: Locator = page.locator("#react-burger-menu-btn")
        self._logout_link: Locator = page.locator("#logout_sidebar_link")

    def expect_loaded(self) -> None:
        self.page.wait_for_url("**/inventory.html")

    def add_item_by_name(self, product_name: str) -> None:
        # Scope button lookup to a single product card by visible item name.
        item = self._inventory_items.filter(has=self.page.get_by_text(product_name, exact=True))
        item.get_by_role("button", name="Add to cart").click()

    def remove_item_by_name(self, product_name: str) -> None:
        item = self._inventory_items.filter(has=self.page.get_by_text(product_name, exact=True))
        item.get_by_role("button", name="Remove").click()

    def open_cart(self) -> None:
        self._cart_link.click()

    def logout(self) -> None:
        self._menu_button.click()
        self._logout_link.click()

    @property
    def cart_badge(self) -> Locator:
        return self._cart_badge

    @property
    def inventory_items(self) -> Locator:
        return self._inventory_items
