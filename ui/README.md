# UI — Playwright (SauceDemo)

> Same scenarios also run under [Selenium 4](../selenium/) in this repo.

End-to-end tests for [SauceDemo / Swag Labs](https://www.saucedemo.com) using **Playwright**, **Python**, **pytest**, and the **Page Object Model**. Credentials and base URL come from environment variables.

## Prerequisites

- Python 3.10+
- pip

## Setup

```bash
cd ui
python -m venv .venv
source .venv/bin/activate   # Windows: .venv\Scripts\activate
pip install -e .
playwright install chromium
cp .env.example .env
```

## Run tests

```bash
pytest                    # full suite (headless Chromium)
pytest -m smoke           # critical path only
pytest -m regression      # broader coverage
pytest --headed           # watch the browser while debugging
```

## Debug failures

```bash
playwright show-trace test-results/<path-to-trace.zip>
playwright show-report
```

## Layout

| Path | Purpose |
|------|---------|
| `pages/` | Page objects — locators and actions |
| `tests/` | pytest tests and fixtures |
| `config.py` | Settings dataclass (loaded from `.env`) |

## Test scope

| File | Coverage |
|------|----------|
| `test_auth.py` | Valid login, invalid credentials, locked-out user, logout |
| `test_inventory.py` | Add/remove items, cart badge counts |
| `test_cart.py` | Cart contents, remove on cart page, continue shopping |
| `test_checkout.py` | Full purchase flow, validation errors, cancel |
