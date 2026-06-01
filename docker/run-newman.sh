#!/bin/sh
set -eu

if [ -z "${REQRES_API_KEY:-}" ] || [ "${REQRES_API_KEY}" = "YOUR_API_KEY_HERE" ]; then
  echo "REQRES_API_KEY is missing. Set it in docker/.env before running api_newman." >&2
  exit 1
fi

npx newman run reqres.collection.json \
  -e reqres.environment.example.json \
  --env-var "apiKey=${REQRES_API_KEY}" \
  --env-var "reqresEnv=prod" \
  --reporters cli
