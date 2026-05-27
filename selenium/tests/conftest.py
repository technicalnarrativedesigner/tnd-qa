import os
from pathlib import Path

import pytest
from dotenv import load_dotenv
from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.remote.webdriver import WebDriver

from config import Settings
from pages.cart_page import CartPage
from pages.checkout_page import CheckoutPage
from pages.inventory_page import InventoryPage
from pages.login_page import LoginPage

load_dotenv(Path(__file__).resolve().parent.parent / ".env")


@pytest.fixture(scope="session")
def settings() -> Settings:
    # One config source for local runs and CI env overrides.
    return Settings(
        base_url=os.getenv("BASE_URL", "https://www.saucedemo.com"),
        standard_user=os.environ["STANDARD_USER"],
        standard_password=os.environ["STANDARD_PASSWORD"],
        locked_out_user=os.environ["LOCKED_OUT_USER"],
        locked_out_password=os.environ["LOCKED_OUT_PASSWORD"],
    )


@pytest.fixture
def driver() -> WebDriver:
    # CI-safe Chrome defaults: headless + shm workaround + no sandbox.
    options = Options()
    if os.getenv("HEADED", "").lower() not in ("1", "true", "yes"):
        options.add_argument("--headless=new")
    options.add_argument("--window-size=1280,720")
    options.add_argument("--disable-gpu")
    options.add_argument("--disable-dev-shm-usage")
    options.add_argument("--no-sandbox")
    browser = webdriver.Chrome(options=options)
    browser.implicitly_wait(0)
    yield browser
    browser.quit()


@pytest.fixture
def login_page(driver: WebDriver, settings: Settings) -> LoginPage:
    return LoginPage(driver, settings.base_url)


@pytest.fixture
def inventory_page(driver: WebDriver, settings: Settings) -> InventoryPage:
    return InventoryPage(driver, settings.base_url)


@pytest.fixture
def cart_page(driver: WebDriver, settings: Settings) -> CartPage:
    return CartPage(driver, settings.base_url)


@pytest.fixture
def checkout_page(driver: WebDriver, settings: Settings) -> CheckoutPage:
    return CheckoutPage(driver, settings.base_url)


@pytest.fixture
def logged_in_inventory(
    driver: WebDriver,
    settings: Settings,
    login_page: LoginPage,
    inventory_page: InventoryPage,
) -> InventoryPage:
    # Reusable authenticated starting point for non-auth tests.
    login_page.open()
    login_page.login(settings.standard_user, settings.standard_password)
    inventory_page.expect_loaded()
    return inventory_page
