package com.virginmoney.transactionlog.config.propertysource;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "user")
public record BasicAuthProperties(@NotEmpty String username, @NotEmpty String password) {
}
