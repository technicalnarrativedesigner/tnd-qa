# API — Postman (ReqRes)

REST API checks for [ReqRes](https://reqres.in): status codes, JSON structure, login/register flows, and CRUD-style user operations.

This suite validates the API puzzle layer behind UI behavior:
- request/response contracts
- auth and validation outcomes
- CRUD-like endpoint behavior under expected conditions

## Latest green run (proof)

![Postman Newman green run](../../images/postman_sanitized.png)

This run shows requests, scripts, and assertions all passing in one execution.

**Important:** ReqRes now requires an API key on every request. The old public API no longer works without authentication (you will see `401` / `missing_api_key`).

## Get an API key (one-time, free)

1. Sign up at [app.reqres.in](https://app.reqres.in) (no card required for free tier).
2. Open your project dashboard and copy a **manage key** (`pro_…`) or **public key** (`pub_…`).
3. Paste it into your environment file (see below).

Docs: [reqres.in/docs](https://reqres.in/docs)

## Prerequisites

- [Node.js](https://nodejs.org/) 18+ (for Newman CLI), **or** [Postman](https://www.postman.com/downloads/) desktop app

## Configure environment

```bash
cd api/postman
cp reqres.environment.example.json reqres.environment.json
```

Edit `reqres.environment.json` and replace `YOUR_API_KEY_HERE` with your real key.

Alternatively, pass the key without editing the file:

```bash
export REQRES_API_KEY=pro_your_key_here
npm test
```

## Run with Newman (CLI)

```bash
npm install
npm test
```

Equivalent:

```bash
npx newman run reqres.collection.json -e reqres.environment.json
```

With an env var override:

```bash
npx newman run reqres.collection.json -e reqres.environment.json --env-var "apiKey=$REQRES_API_KEY"
```

## Run with Docker

From repo root:

```bash
cp docker/.env.example docker/.env
# set REQRES_API_KEY in docker/.env before running
docker compose build api_newman
docker compose run --rm api_newman
```

## Run in Postman GUI

1. Import `reqres.collection.json`
2. Import `reqres.environment.json` (after you set `apiKey`)
3. Select the **ReqRes** environment
4. Run the collection

Collection-level auth sends `x-api-key: {{apiKey}}` and `X-Reqres-Env: prod` on every request.

## Environment variables

| Variable | Purpose |
|----------|---------|
| `baseUrl` | API host (`https://reqres.in`) |
| `apiKey` | **Required** — from [app.reqres.in](https://app.reqres.in) |
| `reqresEnv` | `prod` or `dev` (default `prod`) |
| `validEmail` | Login / register test user |
| `validPassword` | Login / register password |
| `userId` | Default user id for GET/PUT/DELETE |

Do not commit your real `apiKey` to git. Use `reqres.environment.json` locally (gitignored) or `REQRES_API_KEY` in the shell.

## Collection scope

| Folder | Requests |
|--------|----------|
| **Users** | List, get, create, update, delete |
| **Auth** | Login success, login validation error, register |
| **Resources** | User 404, list “unknown” resources |

Each request includes **Tests** tab scripts (assertions) checked by Newman and Postman.
