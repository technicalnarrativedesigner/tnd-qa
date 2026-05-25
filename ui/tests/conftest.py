import os
from pathlib import Path

import pytest
from dotenv import load_dotenv
from playwright.sync_api import Page

from config import Settings
from pages.cart_page import CartPage
from pages.checkout_page import CheckoutPage
from pages.inventory_page import InventoryPage
from pages.login_page import LoginPage

load_dotenv(Path(__file__).resolve().parent.parent / ".env")


@pytest.fixture(scope="session")
def settings() -> Settings:
    return Settings(
        base_url=os.getenv("BASE_URL", "https://www.saucedemo.com"),
        standard_user=os.environ["STANDARD_USER"],
        standard_password=os.environ["STANDARD_PASSWORD"],
        locked_out_user=os.environ["LOCKED_OUT_USER"],
        locked_out_password=os.environ["LOCKED_OUT_PASSWORD"],
    )


@pytest.fixture
def login_page(page: Page, settings: Settings) -> LoginPage:
    return LoginPage(page, settings.base_url)


@pytest.fixture
def inventory_page(page: Page, settings: Settings) -> InventoryPage:
    return InventoryPage(page, settings.base_url)


@pytest.fixture
def cart_page(page: Page, settings: Settings) -> CartPage:
    return CartPage(page, settings.base_url)


@pytest.fixture
def checkout_page(page: Page, settings: Settings) -> CheckoutPage:
    return CheckoutPage(page, settings.base_url)


@pytest.fixture
def logged_in_inventory(
    page: Page,
    settings: Settings,
    login_page: LoginPage,
    inventory_page: InventoryPage,
) -> InventoryPage:
    login_page.open()
    login_page.login(settings.standard_user, settings.standard_password)
    inventory_page.expect_loaded()
    return inventory_page
