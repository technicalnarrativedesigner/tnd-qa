FROM mcr.microsoft.com/playwright/python:v1.60.0-jammy

WORKDIR /app/ui

COPY ui/ /app/ui/

RUN pip install --no-cache-dir --upgrade pip \
    && pip install --no-cache-dir -e .

CMD ["pytest"]
