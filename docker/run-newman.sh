#!/bin/sh
set -eu

# #region agent log
LOG_PATH="/home/technicalnarrativedesigner/Encrypted/Projects/TND/tnd-qa/.cursor/debug-3ba741.log"
KEY_VALUE="${REQRES_API_KEY:-}"
if [ "${KEY_VALUE}" = "YOUR_API_KEY_HERE" ]; then
  PLACEHOLDER=true
else
  PLACEHOLDER=false
fi
if [ -n "${KEY_VALUE}" ]; then
  PRESENT=true
else
  PRESENT=false
fi
KEY_LEN=${#KEY_VALUE}
LOG_LINE="{\"sessionId\":\"3ba741\",\"runId\":\"post-fix\",\"hypothesisId\":\"H6\",\"location\":\"docker/run-newman.sh\",\"message\":\"api_newman env check\",\"data\":{\"present\":${PRESENT},\"placeholder\":${PLACEHOLDER},\"keyLen\":${KEY_LEN}},\"timestamp\":$(date +%s%3N)}"
mkdir -p "$(dirname "${LOG_PATH}")" 2>/dev/null || true
printf '%s\n' "${LOG_LINE}" >> "${LOG_PATH}" 2>/dev/null || true
printf '%s\n' "${LOG_LINE}"
# #endregion

if [ -z "${REQRES_API_KEY:-}" ] || [ "${REQRES_API_KEY}" = "YOUR_API_KEY_HERE" ]; then
  echo "REQRES_API_KEY is missing. Set it in docker/.env before running api_newman. [DBG-H6]" >&2
  exit 1
fi

npx newman run reqres.collection.json \
  -e reqres.environment.example.json \
  --env-var "apiKey=${REQRES_API_KEY}" \
  --env-var "reqresEnv=prod" \
  --reporters cli
