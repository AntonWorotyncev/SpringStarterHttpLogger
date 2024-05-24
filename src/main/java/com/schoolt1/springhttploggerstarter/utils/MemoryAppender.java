package com.schoolt1.springhttploggerstarter.utils;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MemoryAppender extends AppenderBase<ILoggingEvent> {

        private List<ILoggingEvent> list = Collections.synchronizedList(new ArrayList<>());

        @Override
        protected void append(ILoggingEvent eventObject) {
            list.add(eventObject);
        }

    public void reset() {
        this.list.clear();
    }

    public List<ILoggingEvent> getLoggedEvents() {
        return Collections.unmodifiableList(this.list);
    }
}
