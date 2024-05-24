package com.schoolt1.springhttploggerstarter.config;

import com.schoolt1.springhttploggerstarter.inspector.HttpLoggingInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@AutoConfiguration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "http.logging", value = "enabled", havingValue = "true")
public class HttpLoggingWebConfiguration implements WebMvcConfigurer {

    private final HttpLoggingInterceptor httpLoggingInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(httpLoggingInterceptor);
    }
}
