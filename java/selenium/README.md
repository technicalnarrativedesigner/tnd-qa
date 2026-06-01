# Java Selenium (Minimal Showcase)

Minimal Java Selenium suite proving core UI flows with Page Object Pattern and JUnit 5.

## Latest run proof

![Java Selenium green run](../../images/selenium_java.png)

## Setup

```bash
cd java/selenium
cp .env.example .env
mvn test
```

## Covered flows

- valid login redirects to inventory
- add item updates cart badge
- checkout happy path reaches confirmation
