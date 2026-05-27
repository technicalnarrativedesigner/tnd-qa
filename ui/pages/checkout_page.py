from playwright.sync_api import Locator, Page

from pages.base_page import BasePage


class CheckoutPage(BasePage):
    """Checkout steps: customer info, overview, and completion actions."""

    def __init__(self, page: Page, base_url: str) -> None:
        super().__init__(page, base_url)
        self._first_name: Locator = page.locator("#first-name")
        self._last_name: Locator = page.locator("#last-name")
        self._postal_code: Locator = page.locator("#postal-code")
        self._continue: Locator = page.locator("#continue")
        self._cancel: Locator = page.locator("#cancel")
        self._finish: Locator = page.locator("#finish")
        self._complete_header: Locator = page.locator(".complete-header")

    def expect_step_one_loaded(self) -> None:
        self.page.wait_for_url("**/checkout-step-one.html")

    def expect_step_two_loaded(self) -> None:
        self.page.wait_for_url("**/checkout-step-two.html")

    def expect_complete_loaded(self) -> None:
        self.page.wait_for_url("**/checkout-complete.html")

    def fill_customer_info(
        self,
        first_name: str,
        last_name: str,
        postal_code: str,
    ) -> None:
        # Keep field entry in one method so tests read like business flow.
        self._first_name.fill(first_name)
        self._last_name.fill(last_name)
        self._postal_code.fill(postal_code)

    def continue_to_overview(self) -> None:
        self._continue.click()

    def cancel_checkout(self) -> None:
        self._cancel.click()

    def finish_order(self) -> None:
        self._finish.click()

    @property
    def error_message(self) -> Locator:
        return self.page.locator("[data-test='error']")

    @property
    def complete_header(self) -> Locator:
        return self._complete_header
