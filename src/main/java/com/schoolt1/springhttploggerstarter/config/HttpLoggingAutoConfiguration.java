package com.schoolt1.springhttploggerstarter.config;

import com.schoolt1.springhttploggerstarter.inspector.HttpLoggingInterceptor;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;


@AutoConfiguration
@EnableConfigurationProperties(HttpLoggingProperties.class)
@ConditionalOnProperty(prefix = "http.logging", value = "enabled", havingValue = "true")
@RequiredArgsConstructor
public class HttpLoggingAutoConfiguration {

    private final HttpLoggingProperties httpLoggingProperties;

    @Bean
    public HttpLoggingInterceptor HttpLoggingInterceptor() {
            return new HttpLoggingInterceptor(httpLoggingProperties);
    }
}

