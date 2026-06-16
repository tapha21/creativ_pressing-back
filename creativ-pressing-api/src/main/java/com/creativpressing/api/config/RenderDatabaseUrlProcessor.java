package com.creativpressing.api.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class RenderDatabaseUrlProcessor implements EnvironmentPostProcessor {
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        if (hasText(environment.getProperty("SPRING_DATASOURCE_URL"))) {
            return;
        }

        String rawUrl = firstText(
                environment.getProperty("DATABASE_URL"),
                environment.getProperty("DATABASE_PRIVATE_URL"),
                environment.getProperty("POSTGRES_URL"));

        if (!hasText(rawUrl)) {
            return;
        }

        DatabaseProperties properties = parse(rawUrl);
        if (properties == null) {
            return;
        }

        Map<String, Object> datasource = new HashMap<>();
        datasource.put("spring.datasource.url", properties.jdbcUrl());
        datasource.put("spring.datasource.username", properties.username());
        datasource.put("spring.datasource.password", properties.password());
        environment.getPropertySources().addFirst(new MapPropertySource("renderDatabaseUrl", datasource));
    }

    private DatabaseProperties parse(String rawUrl) {
        try {
            String normalized = rawUrl.startsWith("postgres://")
                    ? rawUrl.replaceFirst("postgres://", "postgresql://")
                    : rawUrl;
            URI uri = URI.create(normalized);
            String userInfo = uri.getUserInfo();
            if (userInfo == null || !hasText(uri.getHost())) {
                return null;
            }

            String[] credentials = userInfo.split(":", 2);
            String username = decode(credentials[0]);
            String password = credentials.length > 1 ? decode(credentials[1]) : "";
            int port = uri.getPort() == -1 ? 5432 : uri.getPort();
            String database = uri.getPath() == null ? "" : uri.getPath();
            String query = hasText(uri.getQuery()) ? "?" + uri.getQuery() : "";
            String jdbcUrl = "jdbc:postgresql://" + uri.getHost() + ":" + port + database + query;

            return new DatabaseProperties(jdbcUrl, username, password);
        } catch (Exception ignored) {
            return null;
        }
    }

    private String firstText(String... values) {
        for (String value : values) {
            if (hasText(value)) {
                return value;
            }
        }
        return null;
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }

    private record DatabaseProperties(String jdbcUrl, String username, String password) {
    }
}
