from selenium.webdriver.common.by import By
from selenium.webdriver.remote.webdriver import WebDriver
from selenium.webdriver.remote.webelement import WebElement
from selenium.webdriver.support import expected_conditions as EC

from pages.base_page import BasePage


class CartPage(BasePage):
    """Cart page operations used by cart-focused and checkout-flow tests."""

    def __init__(self, driver: WebDriver, base_url: str) -> None:
        super().__init__(driver, base_url)
        self._cart_items = (By.CSS_SELECTOR, ".cart_item")
        self._continue_shopping = (By.ID, "continue-shopping")
        self._checkout = (By.ID, "checkout")
        self._cart_badge = (By.CSS_SELECTOR, ".shopping_cart_badge")

    def expect_loaded(self) -> None:
        self.wait.until(EC.url_contains("cart.html"))

    def _item_xpath(self, product_name: str) -> str:
        return (
            f"//div[contains(@class,'cart_item')]"
            f"[.//div[contains(@class,'inventory_item_name') and normalize-space()='{product_name}']]"
        )

    def remove_item_by_name(self, product_name: str) -> None:
        # Assert row count drops to confirm remove click actually applied.
        current_count = len(self.cart_items)
        button = (
            By.XPATH,
            f"{self._item_xpath(product_name)}//button[contains(@id,'remove')]",
        )
        self.click_element(button)
        self.wait.until(lambda d: len(self.cart_items) < current_count)

    def continue_shopping(self) -> None:
        self.click_element(self._continue_shopping)
        self.wait.until(EC.url_contains("inventory.html"))

    def proceed_to_checkout(self) -> None:
        self.click_element(self._checkout)
        self.wait.until(EC.url_contains("checkout-step-one.html"))

    @property
    def cart_items(self) -> list[WebElement]:
        return self.driver.find_elements(*self._cart_items)

    def cart_badge_count(self) -> int:
        return len(self.driver.find_elements(*self._cart_badge))

    def get_product_names(self) -> list[str]:
        # Return visible cart names for clean assertions in tests.
        names: list[str] = []
        for item in self.cart_items:
            name_el = item.find_element(By.CSS_SELECTOR, ".inventory_item_name")
            names.append(name_el.text.strip())
        return names
