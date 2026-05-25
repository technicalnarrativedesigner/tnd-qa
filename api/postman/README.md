# API — Postman (ReqRes)

REST API checks for [ReqRes](https://reqres.in): status codes, JSON structure, login/register flows, and CRUD-style user operations.

## Prerequisites

- [Node.js](https://nodejs.org/) 18+ (for Newman CLI), **or** [Postman](https://www.postman.com/downloads/) desktop app

## Run with Newman (CLI)

```bash
cd api/postman
npm install
npm test
```

Equivalent:

```bash
npx newman run reqres.collection.json -e reqres.environment.json
```

## Run in Postman GUI

1. Import `reqres.collection.json`
2. Import `reqres.environment.json`
3. Select the **ReqRes** environment
4. Run the collection

## Environment variables

| Variable | Purpose |
|----------|---------|
| `baseUrl` | API host (`https://reqres.in`) |
| `validEmail` | Login / register test user |
| `validPassword` | Login / register password |
| `userId` | Default user id for GET/PUT/DELETE |

Copy values from the table into a Postman environment if you customize them; no `.env` file is required for Newman (values live in `reqres.environment.json`).

## Collection scope

| Folder | Requests |
|--------|----------|
| **Users** | List, get, create, update, delete |
| **Auth** | Login success, login validation error, register |
| **Resources** | User 404, list “unknown” resources |

Each request includes **Tests** tab scripts (assertions) checked by Newman and Postman.
