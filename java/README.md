# Java Showcase Layer

This folder contains minimal Java ports of the core automation story to show that test architecture transfers across language stacks.

## Projects

- [`java/selenium`](selenium/) - Selenium + JUnit 5 + Maven
- [`java/playwright`](playwright/) - Playwright Java + JUnit 5 + Maven

## Scope

Each Java project intentionally covers 3 core flows:

- valid login
- add-to-cart badge update
- checkout happy path

The full broader coverage remains in the Python suites.

## Run with Docker

From repo root:

```bash
cp docker/.env.example docker/.env
docker compose build java_playwright java_selenium
docker compose run --rm -e BROWSER=chromium java_playwright
docker compose run --rm -e BROWSER=firefox java_playwright
docker compose run --rm -e BROWSER=webkit java_playwright
docker compose run --rm -e SELENIUM_BROWSER=chromium java_selenium
docker compose run --rm -e SELENIUM_BROWSER=firefox java_selenium
```

WebKit note: Linux WebKit provides Safari-like coverage for Playwright, but it is not a full replacement for native Safari/macOS validation.

## Proof of Java runs

### Java Playwright

![Java Playwright green run](../images/playwright_Java.png)

### Java Selenium

![Java Selenium green run](../images/selenium_java.png)
