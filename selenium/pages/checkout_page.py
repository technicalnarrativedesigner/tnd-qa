from selenium.webdriver.common.by import By
from selenium.webdriver.remote.webdriver import WebDriver
from selenium.webdriver.remote.webelement import WebElement
from selenium.webdriver.support import expected_conditions as EC

from pages.base_page import BasePage


class CheckoutPage(BasePage):
    def __init__(self, driver: WebDriver, base_url: str) -> None:
        super().__init__(driver, base_url)
        self._first_name = (By.ID, "first-name")
        self._last_name = (By.ID, "last-name")
        self._postal_code = (By.ID, "postal-code")
        self._continue = (By.CSS_SELECTOR, "[data-test='continue']")
        self._cancel = (By.CSS_SELECTOR, "button[data-test='cancel']")
        self._finish = (By.CSS_SELECTOR, "button[data-test='finish']")
        self._complete_header = (By.CSS_SELECTOR, ".complete-header")
        self._error = (By.CSS_SELECTOR, "[data-test='error']")

    def expect_step_one_loaded(self) -> None:
        self.wait.until(EC.url_contains("checkout-step-one.html"))

    def expect_step_two_loaded(self) -> None:
        self.wait.until(EC.url_contains("checkout-step-two.html"))

    def expect_complete_loaded(self) -> None:
        self.wait.until(EC.url_contains("checkout-complete.html"))

    def fill_customer_info(
        self,
        first_name: str,
        last_name: str,
        postal_code: str,
    ) -> None:
        self.fill_input(self._first_name, first_name)
        self.fill_input(self._last_name, last_name)
        self.fill_input(self._postal_code, postal_code)

    def continue_to_overview(self) -> None:
        self.click_element(self._continue)
        self.wait.until(
            lambda d: "checkout-step-two.html" in d.current_url
            or d.find_elements(*self._error)
        )

    def cancel_checkout(self) -> None:
        self.expect_step_one_loaded()
        cancel = self.wait.until(EC.visibility_of_element_located(self._cancel))
        self.driver.execute_script("arguments[0].click();", cancel)
        self.wait.until(EC.url_contains("cart.html"))

    def finish_order(self) -> None:
        self.expect_step_two_loaded()
        finish = self.wait.until(EC.visibility_of_element_located(self._finish))
        self.driver.execute_script("arguments[0].click();", finish)
        self.wait.until(EC.url_contains("checkout-complete.html"))

    def wait_for_validation_error(self) -> WebElement:
        self.wait.until(EC.url_contains("checkout-step-one.html"))
        return self.wait.until(EC.visibility_of_element_located(self._error))

    @property
    def error_message(self) -> WebElement:
        return self.wait.until(EC.visibility_of_element_located(self._error))

    @property
    def complete_header(self) -> WebElement:
        return self.wait.until(EC.visibility_of_element_located(self._complete_header))
