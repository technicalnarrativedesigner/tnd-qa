from typing import Tuple

from selenium.webdriver.common.keys import Keys
from selenium.webdriver.remote.webdriver import WebDriver
from selenium.webdriver.remote.webelement import WebElement
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.support.ui import WebDriverWait

Locator = Tuple[str, str]


class BasePage:
    DEFAULT_TIMEOUT = 15

    def __init__(self, driver: WebDriver, base_url: str) -> None:
        self.driver = driver
        self.base_url = base_url.rstrip("/")
        self.wait = WebDriverWait(driver, self.DEFAULT_TIMEOUT)

    def goto(self, path: str = "/") -> None:
        if path.startswith("http"):
            self.driver.get(path)
        else:
            self.driver.get(f"{self.base_url}{path}")

    def fill_input(self, locator: Locator, value: str) -> None:
        """Set value on React-controlled inputs (clear() alone is unreliable)."""
        field = self.wait.until(EC.element_to_be_clickable(locator))
        field.click()
        field.send_keys(Keys.CONTROL, "a", Keys.BACKSPACE)
        if value:
            field.send_keys(value)
        self.driver.execute_script(
            """
            const el = arguments[0];
            const val = arguments[1];
            const setter = Object.getOwnPropertyDescriptor(
                window.HTMLInputElement.prototype, 'value'
            ).set;
            setter.call(el, val);
            el.dispatchEvent(new Event('input', { bubbles: true }));
            el.dispatchEvent(new Event('change', { bubbles: true }));
            el.dispatchEvent(new Event('blur', { bubbles: true }));
            """,
            field,
            value,
        )

    def click_element(self, locator: Locator) -> None:
        element = self.wait.until(EC.element_to_be_clickable(locator))
        self.driver.execute_script(
            "arguments[0].scrollIntoView({block: 'center'}); arguments[0].click();",
            element,
        )
