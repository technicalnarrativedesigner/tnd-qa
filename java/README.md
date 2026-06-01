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

## Proof of Java runs

### Java Playwright

![Java Playwright green run](../images/playwright_Java.png)

### Java Selenium

![Java Selenium green run](../images/selenium_java.png)
