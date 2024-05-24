package com.schoolt1.springhttploggerstarter.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "http.logging")
public class HttpLoggingProperties {
    private boolean enabled = false;
    private String level = "info";
}
