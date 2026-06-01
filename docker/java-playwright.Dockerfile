FROM mcr.microsoft.com/playwright/java:v1.60.0-jammy

WORKDIR /app/java/playwright

COPY java/playwright/ /app/java/playwright/

RUN mvn -q -DskipTests dependency:go-offline

CMD ["mvn", "test"]
