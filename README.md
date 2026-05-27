# TND QA Portfolio

![CI](https://github.com/technicalnarrativedesigner/tnd-qa/actions/workflows/ci.yml/badge.svg?branch=main)

This repository is a **learning and showcase project** for test automation roles.  
Goal: demonstrate how I design maintainable UI/API automation, manage test environments safely, and run the same suites in local and CI workflows.

## About This Portfolio

- **Who this is for**: recruiters, QA leads, and SDET interviewers evaluating practical automation skills
- **What this demonstrates**: Page Object Pattern, API assertions, env/secrets management, and CI execution
- **Why two UI frameworks**: Playwright (modern default) and Selenium 4 (still common in production teams)

| Suite | Tool | Target | Folder | What it proves |
|-------|------|--------|--------|----------------|
| **UI (Playwright)** | Playwright + pytest + POM | [SauceDemo](https://www.saucedemo.com) | [`ui/`](ui/) | Modern E2E design with readable page objects and stable waits |
| **UI (Selenium)** | Selenium 4 + pytest + POM | [SauceDemo](https://www.saucedemo.com) | [`selenium/`](selenium/) | Legacy-compatible E2E coverage with explicit synchronization |
| **API** | Postman + Newman | [ReqRes](https://reqres.in) | [`api/postman/`](api/postman/) | HTTP assertions, auth handling, and CLI/API CI integration |

## 30-Second Run (Recruiter Quick Check)

### Playwright smoke
```bash
cd ui && python -m venv .venv && source .venv/bin/activate && pip install -e . && playwright install chromium && cp .env.example .env && pytest -m smoke
```

### Selenium smoke
```bash
cd selenium && python -m venv .venv && source .venv/bin/activate && pip install -e . && cp .env.example .env && pytest -m smoke
```

### Postman/Newman API
```bash
cd api/postman && npm install && REQRES_API_KEY=your_key npm test
```

Detailed setup:
- [ui/README.md](ui/README.md)
- [selenium/README.md](selenium/README.md)
- [api/postman/README.md](api/postman/README.md)

## CI (GitHub Actions)

Workflow: [`.github/workflows/ci.yml`](.github/workflows/ci.yml)

Runs on push/PR to `main` and manual dispatch:
- Playwright UI suite (`ui/`)
- Selenium UI suite (`selenium/`)
- Newman API suite (`api/postman/`)

Required GitHub secret:
- `REQRES_API_KEY` (ReqRes `x-api-key` for API tests)

Optional but supported:
- `STANDARD_USER`
- `STANDARD_PASSWORD`
- `LOCKED_OUT_USER`
- `LOCKED_OUT_PASSWORD`

CI uploads:
- Playwright run artifacts (`test-results` and report folder when present)
- Newman report artifacts (`junit`, `json`)

## Test Strategy

See [TESTING.md](TESTING.md) for:
- smoke vs regression intent
- scope choices and trade-offs
- out-of-scope items kept for future iterations

## Interview Talking Points

- **Playwright vs Selenium**: auto-waiting vs explicit waits, debugging ergonomics, and reliability trade-offs
- **POM structure**: why selectors/actions stay in page objects while assertions stay in tests
- **API auth adaptation**: updated ReqRes coverage after `x-api-key` requirement changes
- **Environment management**: local `.env` + GitHub Secrets in CI (no credentials committed to git)
- **Engineering mindset**: this repo is intentionally built as both a learning track and a production-style showcase

## Optional Visual Demo

Add one screenshot or short GIF here later (local green run or Actions green check) to make scanning faster for reviewers.

## License

Portfolio / learning project. SauceDemo and ReqRes are third-party demo services.
