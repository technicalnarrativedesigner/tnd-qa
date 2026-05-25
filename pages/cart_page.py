from playwright.sync_api import Locator, Page

from pages.base_page import BasePage


class CartPage(BasePage):
    def __init__(self, page: Page, base_url: str) -> None:
        super().__init__(page, base_url)
        self._cart_items: Locator = page.locator(".cart_item")
        self._continue_shopping: Locator = page.locator("#continue-shopping")
        self._checkout: Locator = page.locator("#checkout")
        self._cart_badge: Locator = page.locator(".shopping_cart_badge")

    def expect_loaded(self) -> None:
        self.page.wait_for_url("**/cart.html")

    def remove_item_by_name(self, product_name: str) -> None:
        item = self._cart_items.filter(has=self.page.get_by_text(product_name, exact=True))
        item.get_by_role("button", name="Remove").click()

    def continue_shopping(self) -> None:
        self._continue_shopping.click()

    def proceed_to_checkout(self) -> None:
        self._checkout.click()

    @property
    def cart_items(self) -> Locator:
        return self._cart_items

    @property
    def cart_badge(self) -> Locator:
        return self._cart_badge
