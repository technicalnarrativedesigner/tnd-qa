# Testing Strategy

This project is designed as a **learning + showcase portfolio** for automation roles.

## Why These Targets

- **SauceDemo** gives stable, deterministic UI flows for authentication, cart, and checkout
- **ReqRes** gives simple API endpoints to demonstrate request/response assertions and auth handling
- Both are public demo systems that let the suite stay reproducible for reviewers

## Smoke vs Regression

- **Smoke**: critical user journeys that must work to trust the build
  - valid login
  - add-to-cart basics
  - successful checkout
- **Regression**: broader behavior and negative cases
  - invalid credentials / locked user
  - remove/decrement checks
  - checkout validation errors
  - navigation edge paths (cancel, continue shopping)

## Framework Choice Rationale

- **Playwright suite** is the modern baseline:
  - built-in waits
  - cleaner locator ergonomics
  - straightforward trace debugging
- **Selenium suite** mirrors core scenarios:
  - common in legacy enterprise stacks
  - shows explicit wait discipline
  - highlights migration trade-offs in interviews

## API Coverage Approach (Postman/Newman)

- Focus on practical checks:
  - status codes
  - expected JSON structure
  - key fields in response payloads
- Include auth-aware behavior:
  - ReqRes API key requirement via env/secrets
- Run in both:
  - Postman GUI (interactive)
  - Newman CLI (CI-friendly)

## Environment and Secret Management

- Local runs use `.env` (UI) and local Postman environment files (API)
- CI uses GitHub Actions secrets
- Real secrets are never committed to git

## Deliberately Out of Scope (for now)

- Cross-browser matrix expansion
- Visual regression testing
- Performance/load testing
- Full contract schema validation for every endpoint
- Advanced reporting platforms (Allure or custom dashboards)

These are intentionally postponed to keep the repository focused on maintainability and interview-readiness.
