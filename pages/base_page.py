from playwright.sync_api import Page


class BasePage:
    def __init__(self, page: Page, base_url: str) -> None:
        self.page = page
        self.base_url = base_url.rstrip("/")

    def goto(self, path: str = "/") -> None:
        if path.startswith("http"):
            self.page.goto(path)
        else:
            self.page.goto(f"{self.base_url}{path}")
