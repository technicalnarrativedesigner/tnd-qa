from selenium.webdriver.common.by import By
from selenium.webdriver.remote.webdriver import WebDriver
from selenium.webdriver.remote.webelement import WebElement
from selenium.webdriver.support import expected_conditions as EC

from pages.base_page import BasePage


class InventoryPage(BasePage):
    def __init__(self, driver: WebDriver, base_url: str) -> None:
        super().__init__(driver, base_url)
        self._cart_link = (By.CSS_SELECTOR, ".shopping_cart_link")
        self._cart_badge = (By.CSS_SELECTOR, ".shopping_cart_badge")
        self._inventory_items = (By.CSS_SELECTOR, ".inventory_item")
        self._menu_button = (By.ID, "react-burger-menu-btn")
        self._logout_link = (By.ID, "logout_sidebar_link")

    def expect_loaded(self) -> None:
        self.wait.until(EC.url_contains("inventory.html"))

    def _item_xpath(self, product_name: str) -> str:
        return (
            f"//div[contains(@class,'inventory_item')]"
            f"[.//div[contains(@class,'inventory_item_name') and normalize-space()='{product_name}']]"
        )

    def add_item_by_name(self, product_name: str) -> None:
        button = (
            By.XPATH,
            f"{self._item_xpath(product_name)}//button[contains(@id,'add-to-cart')]",
        )
        self.click_element(button)
        self.wait.until(
            EC.presence_of_element_located(
                (
                    By.XPATH,
                    f"{self._item_xpath(product_name)}//button[contains(@id,'remove')]",
                )
            )
        )

    def remove_item_by_name(self, product_name: str) -> None:
        button = (
            By.XPATH,
            f"{self._item_xpath(product_name)}//button[contains(@id,'remove')]",
        )
        self.click_element(button)
        self.wait.until(
            EC.presence_of_element_located(
                (
                    By.XPATH,
                    f"{self._item_xpath(product_name)}//button[contains(@id,'add-to-cart')]",
                )
            )
        )

    def open_cart(self) -> None:
        self.click_element(self._cart_link)
        self.wait.until(EC.url_contains("cart.html"))

    def logout(self) -> None:
        self.click_element(self._menu_button)
        logout = self.wait.until(EC.element_to_be_clickable(self._logout_link))
        self.driver.execute_script("arguments[0].click();", logout)
        self.wait.until(lambda d: "inventory.html" not in d.current_url)

    @property
    def cart_badge(self) -> WebElement:
        return self.wait.until(EC.visibility_of_element_located(self._cart_badge))

    def cart_badge_count(self) -> int:
        return len(self.driver.find_elements(*self._cart_badge))

    @property
    def inventory_items(self) -> list[WebElement]:
        return self.driver.find_elements(*self._inventory_items)
