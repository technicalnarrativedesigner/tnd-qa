# TND QA Portfolio

QA automation samples in one repo: **browser E2E** and **REST API** testing.

| Suite | Tool | Target | Folder |
|-------|------|--------|--------|
| **UI** | Playwright + pytest + POM | [SauceDemo](https://www.saucedemo.com) | [`ui/`](ui/) |
| **API** | Postman + Newman | [ReqRes](https://reqres.in) | [`api/postman/`](api/postman/) |

## Quick start

### UI (Playwright)

```bash
cd ui
python -m venv .venv && source .venv/bin/activate
pip install -e .
playwright install chromium
cp .env.example .env
pytest
```

Details: [ui/README.md](ui/README.md)

### API (Postman / Newman)

```bash
cd api/postman
cp reqres.environment.example.json reqres.environment.json
# Edit reqres.environment.json — set apiKey from https://app.reqres.in
npm install
npm test
```

Or import `reqres.collection.json` and `reqres.environment.json` into the Postman app.

Details: [api/postman/README.md](api/postman/README.md)

## Why one repo?

- Single portfolio link for recruiters
- Shared conventions (env files, README structure)
- UI and API skills side by side without mixing Python page objects with Postman JSON

## License

Portfolio / learning project. SauceDemo and ReqRes are third-party demo services.
