# TND QA — Playwright Portfolio (SauceDemo)

End-to-end tests for [SauceDemo / Swag Labs](https://www.saucedemo.com) using **Playwright**, **Python**, **pytest**, and the **Page Object Model**. Credentials and base URL come from environment variables so nothing sensitive is committed to git.

## Prerequisites

- Python 3.10+
- pip

## Setup

```bash
cd tnd-qa
python -m venv .venv
source .venv/bin/activate   # Windows: .venv\Scripts\activate
pip install -e .
playwright install chromium
cp .env.example .env
```

Edit `.env` if you need different users or a different base URL.

## Run tests

```bash
pytest                    # full suite (headless Chromium)
pytest -m smoke           # critical path only
pytest -m regression      # broader coverage
pytest --headed           # watch the browser while debugging
```

## Debug failures

Playwright keeps a trace when a test fails:

```bash
playwright show-trace test-results/<path-to-trace.zip>
```

Open the HTML report from the last run:

```bash
playwright show-report
```

## Project layout

| Path | Purpose |
|------|---------|
| `pages/` | Page objects — locators and actions only |
| `tests/` | pytest tests and fixtures |
| `tests/conftest.py` | env loading, page fixtures, logged-in session |

## Test scope

| File | Coverage |
|------|----------|
| `test_auth.py` | Valid login, invalid credentials, locked-out user, logout |
| `test_inventory.py` | Add/remove items, cart badge counts |
| `test_cart.py` | Cart contents, remove on cart page, continue shopping |
| `test_checkout.py` | Full purchase flow, validation errors, cancel |

## Why Playwright (vs Selenium)?

- **Auto-waiting** — less flaky `sleep()` and explicit waits
- **Trace viewer** — step-through replay on failure
- **Speed** — faster browser automation and parallel-friendly design
- **Modern API** — locators, contexts, and built-in assertions via `expect`

## License

Portfolio / learning project — SauceDemo is owned by Sauce Labs.
