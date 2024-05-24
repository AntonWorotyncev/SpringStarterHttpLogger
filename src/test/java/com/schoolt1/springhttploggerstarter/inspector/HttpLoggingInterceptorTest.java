package com.schoolt1.springhttploggerstarter.inspector;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import com.schoolt1.springhttploggerstarter.config.HttpLoggingProperties;
import com.schoolt1.springhttploggerstarter.utils.LoggerUtils;
import com.schoolt1.springhttploggerstarter.utils.MemoryAppender;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.After;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class HttpLoggingInterceptorTest {

    private MemoryAppender memoryAppender;

    @Mock
    private HttpLoggingProperties httpLoggingProperties;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private HttpLoggingInterceptor interceptor;

    @BeforeEach
    public void setUp() {
        Logger logger = (Logger) LoggerFactory.getLogger(LoggerUtils.class);
        memoryAppender = new MemoryAppender();
        memoryAppender.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
        logger.setLevel(Level.ALL);
        logger.addAppender(memoryAppender);
        memoryAppender.start();
    }
    @After
    public void cleanUp() {
        memoryAppender.reset();
        memoryAppender.stop();
    }

    @Test
    public void whenPreHandle_thenLogsRequest() {

        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/test-uri");

        Vector<String> headerNames = new Vector<>();
        headerNames.addAll(List.of("Content-Type","User-Agent"));
        when(request.getHeaderNames()).thenReturn(headerNames.elements());
        when(httpLoggingProperties.getLevel()).thenReturn("debug");

        interceptor.preHandle(request, response, new Object());

        boolean logContainsRequest = memoryAppender.getLoggedEvents().stream()
                .anyMatch(event -> event.getFormattedMessage()
                        .contains("Входящий запрос: method=GET, uri=/test-uri, headers=[Content-Type, User-Agent]"));
        assertTrue(logContainsRequest);
    }


    @Test
    public void whenAfterCompletionWithSpecialCharacters_thenLogsResponse() {
        long startTime = 4000;
        when(request.getAttribute("startTime")).thenReturn(startTime);
        when(response.getStatus()).thenReturn(200);
        when(httpLoggingProperties.getLevel()).thenReturn("debug");

        when(response.getHeaderNames()).thenReturn(Collections.singleton("X-Special-Response-Header"));

        interceptor.afterCompletion(request, response, new Object(), null);

        boolean logContainsResponse = memoryAppender.getLoggedEvents().stream()
                .anyMatch(event -> event.getFormattedMessage().contains("Исходящий ответ: status={200}," +
                        " headers=[X-Special-Response-Header], duration="));
        assertTrue(logContainsResponse);
    }

    @Test
    public void whenAfterCompletion_thenLogsResponse() {
        long startTime = 1000;
        when(request.getAttribute("startTime")).thenReturn(startTime);
        when(response.getStatus()).thenReturn(201);

        List<String> headerNames = new ArrayList<>();

        headerNames.addAll(List.of("Content-Type","User-Agent"));
        when(response.getHeaderNames()).thenReturn(headerNames);
        when(httpLoggingProperties.getLevel()).thenReturn("info");

        interceptor.afterCompletion(request, response, new Object(), null);

        boolean logContainsResponse = memoryAppender.getLoggedEvents().stream()
                .anyMatch(event -> event.getFormattedMessage()
                        .contains("Исходящий ответ: status={201}, headers=[Content-Type, User-Agent], duration="));
        assertTrue(logContainsResponse);
    }


    @Test
    public void whenPreHandleWithNoHeaders_thenLogsRequest() {
        when(request.getMethod()).thenReturn("POST");
        when(request.getRequestURI()).thenReturn("/empty-headers");

        when(request.getHeaderNames()).thenReturn(Collections.emptyEnumeration());
        when(httpLoggingProperties.getLevel()).thenReturn("error");

        interceptor.preHandle(request, response, new Object());


        boolean logContainsRequest = memoryAppender.getLoggedEvents().stream()
                .anyMatch(event -> event.getFormattedMessage()
                        .contains("Входящий запрос: method=POST, uri=/empty-headers, headers=[]"));
        assertTrue(logContainsRequest);
    }

    @Test
    public void whenAfterCompletionWithNoHeaders_thenLogsResponse() {
        long startTime = 2000;
        when(request.getAttribute("startTime")).thenReturn(startTime);
        when(response.getStatus()).thenReturn(404);

        when(response.getHeaderNames()).thenReturn(Collections.emptyList());
        when(httpLoggingProperties.getLevel()).thenReturn("error");

        interceptor.afterCompletion(request, response, new Object(), null);

        boolean logContainsResponse = memoryAppender.getLoggedEvents().stream()
                .anyMatch(event -> event.getFormattedMessage().contains("Исходящий ответ: " +
                        "status={404}, headers=[], duration="));
        assertTrue(logContainsResponse);
    }

    @Test
    public void whenPreHandleWithLongUri_thenLogsRequest() {
        String longUri = "/this/is/a/very/long/uri/that/should/be/logged/properly";
        when(request.getMethod()).thenReturn("PUT");
        when(request.getRequestURI()).thenReturn(longUri);

        when(httpLoggingProperties.getLevel()).thenReturn("info");

        Vector<String> headerNames = new Vector<>();
        headerNames.add("X-Long-Header-Name");
        when(request.getHeaderNames()).thenReturn(headerNames.elements());

        interceptor.preHandle(request, response, new Object());

        boolean logContainsRequest = memoryAppender.getLoggedEvents().stream()
                .anyMatch(event -> event.getFormattedMessage()
                        .contains("Входящий запрос: method=PUT, uri=" + longUri + ", headers=[X-Long-Header-Name]"));
        assertTrue(logContainsRequest);
    }

    @Test
    public void whenAfterCompletionWithLongHeaders_thenLogsResponse() {
        long startTime = 3000;
        when(request.getAttribute("startTime")).thenReturn(startTime);
        when(response.getStatus()).thenReturn(500);

        List<String> headerNames = new ArrayList<>();
        headerNames.add("X-Long-Response-Header");
        when(response.getHeaderNames()).thenReturn(headerNames);
        when(response.getHeader("X-Long-Response-Header")).thenReturn("b".repeat(100));
        when(httpLoggingProperties.getLevel()).thenReturn("info");

        interceptor.afterCompletion(request, response, new Object(), null);

        boolean logContainsResponse = memoryAppender.getLoggedEvents().stream()
                .anyMatch(event -> event.getFormattedMessage().contains("Исходящий ответ: status={500}, " +
                        "headers=[X-Long-Response-Header], duration"));
        assertTrue(logContainsResponse);
    }

    @Test
    public void whenPreHandleWithSpecialCharacters_thenLogsRequest() {
        when(request.getMethod()).thenReturn("PATCH");
        when(request.getRequestURI()).thenReturn("/special-characters");

        Vector<String> headerNames = new Vector<>();
        headerNames.add("特殊字符");
        when(request.getHeaderNames()).thenReturn(headerNames.elements());
        when(httpLoggingProperties.getLevel()).thenReturn("info");

        interceptor.preHandle(request, response, new Object());

        boolean logContainsRequest = memoryAppender.getLoggedEvents().stream()
                .anyMatch(event -> event.getFormattedMessage()
                        .contains("Входящий запрос: method=PATCH, uri=/special-characters, headers=[特殊字符]"));
        assertTrue(logContainsRequest);
    }
}
