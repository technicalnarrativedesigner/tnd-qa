package com.tnd.playwright.config;

import io.github.cdimascio.dotenv.Dotenv;

public final class TestConfig {
    private final String baseUrl;
    private final String standardUser;
    private final String standardPassword;
    private final String headed;

    private TestConfig(String baseUrl, String standardUser, String standardPassword, String headed) {
        this.baseUrl = baseUrl;
        this.standardUser = standardUser;
        this.standardPassword = standardPassword;
        this.headed = headed;
    }

    public static TestConfig load() {
        // Support both local .env and CI-provided environment variables.
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMalformed()
                .ignoreIfMissing()
                .load();

        return new TestConfig(
                read(dotenv, "BASE_URL", "https://www.saucedemo.com"),
                readRequired(dotenv, "STANDARD_USER"),
                readRequired(dotenv, "STANDARD_PASSWORD"),
                read(dotenv, "HEADED", "false")
        );
    }

    private static String read(Dotenv dotenv, String key, String fallback) {
        String value = dotenv.get(key);
        if (value == null || value.isBlank()) {
            value = System.getenv(key);
        }
        return (value == null || value.isBlank()) ? fallback : value;
    }

    private static String readRequired(Dotenv dotenv, String key) {
        String value = dotenv.get(key);
        if (value == null || value.isBlank()) {
            value = System.getenv(key);
        }
        if (value == null || value.isBlank()) {
            // Fail fast when credentials are missing instead of silently using defaults.
            throw new IllegalStateException(
                    "Missing required config '" + key + "'. Set it in .env or environment variables."
            );
        }
        return value;
    }

    public String baseUrl() {
        return baseUrl;
    }

    public String standardUser() {
        return standardUser;
    }

    public String standardPassword() {
        return standardPassword;
    }

    public boolean headed() {
        return "true".equalsIgnoreCase(headed) || "1".equals(headed);
    }
}
