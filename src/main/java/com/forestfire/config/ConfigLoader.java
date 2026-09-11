package com.forestfire.config;

import com.forestfire.model.SimulationParameters;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class ConfigLoader {
    private static final String CONFIG_FILE_NAME = "config.properties";

    private ConfigLoader() {
    }

    public static SimulationParameters load() throws IOException {
        Properties properties = new Properties();

        Path workingDirectory = Path.of(System.getProperty("user.dir"), CONFIG_FILE_NAME);
        if (Files.exists(workingDirectory)) {
            try (InputStream inputStream = Files.newInputStream(workingDirectory)) {
                properties.load(inputStream);
                return SimulationParameters.fromProperties(properties);
            }
        }

        try (InputStream inputStream = ConfigLoader.class.getClassLoader().getResourceAsStream(CONFIG_FILE_NAME)) {
            if (inputStream == null) {
                throw new IOException("Configuration file '" + CONFIG_FILE_NAME + "' was not found.");
            }
            properties.load(inputStream);
            return SimulationParameters.fromProperties(properties);
        }
    }
}
