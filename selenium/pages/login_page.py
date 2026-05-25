from selenium.webdriver.common.by import By
from selenium.webdriver.remote.webdriver import WebDriver
from selenium.webdriver.remote.webelement import WebElement
from selenium.webdriver.support import expected_conditions as EC

from pages.base_page import BasePage


class LoginPage(BasePage):
    PATH = "/"

    def __init__(self, driver: WebDriver, base_url: str) -> None:
        super().__init__(driver, base_url)
        self._username = (By.ID, "user-name")
        self._password = (By.ID, "password")
        self._submit = (By.ID, "login-button")
        self._error = (By.CSS_SELECTOR, "[data-test='error']")

    def open(self) -> None:
        self.goto(self.PATH)

    def login(self, username: str, password: str) -> None:
        self.wait.until(EC.visibility_of_element_located(self._username))
        self.driver.find_element(*self._username).clear()
        self.driver.find_element(*self._username).send_keys(username)
        self.driver.find_element(*self._password).clear()
        self.driver.find_element(*self._password).send_keys(password)
        self.driver.find_element(*self._submit).click()

    @property
    def error_message(self) -> WebElement:
        return self.wait.until(EC.visibility_of_element_located(self._error))
