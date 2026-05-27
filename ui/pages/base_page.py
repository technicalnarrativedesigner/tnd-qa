from playwright.sync_api import Page


class BasePage:
    """Shared Playwright page helpers used by all page objects."""

    def __init__(self, page: Page, base_url: str) -> None:
        self.page = page
        self.base_url = base_url.rstrip("/")

    def goto(self, path: str = "/") -> None:
        # Accept both relative app paths and full URLs for flexibility in tests.
        if path.startswith("http"):
            self.page.goto(path)
        else:
            self.page.goto(f"{self.base_url}{path}")
