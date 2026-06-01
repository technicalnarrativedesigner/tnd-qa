# Java Playwright (Minimal Showcase)

Minimal Java Playwright suite proving core UI flows with Page Object Pattern and JUnit 5.

## Latest run proof

![Java Playwright green run](../../images/playwright_Java.png)

## Setup

```bash
cd java/playwright
cp .env.example .env
mvn test
```

## Covered flows

- valid login redirects to inventory
- add item updates cart badge
- checkout happy path reaches confirmation
