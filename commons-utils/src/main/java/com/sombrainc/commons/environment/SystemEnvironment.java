package com.sombrainc.commons.environment;

import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

import static java.util.Optional.ofNullable;

public class SystemEnvironment {

    public static Optional<String> gerProperty(String propertyName) {
        String propertyValue = System.getProperty(propertyName);

        if (StringUtils.isNoneBlank(propertyValue)) {
            return Optional.of(propertyValue);
        }

        return ofNullable(System.getenv(propertyName));
    }
}
