package com.schoolt1.springhttploggerstarter.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;

@Slf4j
public class LoggerUtils {

    public static Consumer<String> getLogger(String level) {
        switch (level.toLowerCase()) {
            case "info":
                return msg -> log.info(msg);
            case "error":
                return msg -> log.error(msg);
            case "debug":
                return msg -> log.debug(msg);
            default:
                return msg -> {};
        }
    }
}