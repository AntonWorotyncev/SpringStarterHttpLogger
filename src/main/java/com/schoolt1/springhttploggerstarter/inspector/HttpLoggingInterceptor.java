package com.schoolt1.springhttploggerstarter.inspector;
import com.schoolt1.springhttploggerstarter.config.HttpLoggingProperties;
import com.schoolt1.springhttploggerstarter.utils.LoggerUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Collections;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@Slf4j
public class HttpLoggingInterceptor implements HandlerInterceptor {

    private final HttpLoggingProperties httpLoggingProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)  {
        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);
        logRequest(request);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        long startTime = (Long) request.getAttribute("startTime");
        long duration = System.currentTimeMillis() - startTime;
        logResponse(response, duration);
    }

    private void logRequest(HttpServletRequest request) {

        String headers = Collections.list(request.getHeaderNames())
                .stream()
                .collect(Collectors.joining(", "));
        Consumer<String> logger = LoggerUtils.getLogger(httpLoggingProperties.getLevel());
        logger.accept(String.format("Входящий запрос: method=%s, uri=%s, headers=[%s]",
                request.getMethod(), request.getRequestURI(), headers));
    }

    private void logResponse(HttpServletResponse response, long duration) {

        String headers = (response.getHeaderNames())
                .stream()
                .collect(Collectors.joining(", "));
        Consumer<String> logger = LoggerUtils.getLogger(httpLoggingProperties.getLevel());
        logger.accept(String.format("Исходящий ответ: status={%s}, headers=[%s], duration={%s}ms",
                response.getStatus(), headers, duration));
    }
}