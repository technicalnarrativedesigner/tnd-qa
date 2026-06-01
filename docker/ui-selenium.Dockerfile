FROM python:3.11-slim

ENV PYTHONUNBUFFERED=1
ENV CHROME_BIN=/usr/bin/chromium
ENV CHROMEDRIVER=/usr/bin/chromedriver

RUN apt-get update \
    && apt-get install -y --no-install-recommends \
        chromium \
        chromium-driver \
        firefox-esr \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app/selenium

COPY selenium/ /app/selenium/

RUN pip install --no-cache-dir --upgrade pip \
    && pip install --no-cache-dir -e .

CMD ["pytest"]
