from playwright.sync_api import Locator, Page

from pages.base_page import BasePage


class LoginPage(BasePage):
    """Actions/selectors for the SauceDemo login screen."""

    PATH = "/"

    def __init__(self, page: Page, base_url: str) -> None:
        super().__init__(page, base_url)
        self._username: Locator = page.locator("#user-name")
        self._password: Locator = page.locator("#password")
        self._submit: Locator = page.locator("#login-button")

    def open(self) -> None:
        self.goto(self.PATH)

    def login(self, username: str, password: str) -> None:
        # Single login entry-point used by positive and negative auth tests.
        self._username.fill(username)
        self._password.fill(password)
        self._submit.click()

    @property
    def error_message(self) -> Locator:
        return self.page.locator("[data-test='error']")
