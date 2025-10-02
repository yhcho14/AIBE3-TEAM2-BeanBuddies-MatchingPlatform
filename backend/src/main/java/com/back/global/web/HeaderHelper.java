package com.back.global.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HeaderHelper {

    private final HttpServletRequest req;
    private final HttpServletResponse resp;

    public String getHeader(String name, String defaultValue) {
        String headerValue = req.getHeader(name);

        if(headerValue == null || headerValue.isBlank()) {
            return defaultValue;
        }
        return headerValue;
    }

    public void setHeader(String name, String value) {
        if(value == null || value.isBlank()) {
            req.removeAttribute(name);
            return;
        }
        resp.setHeader(name, value);
    }
}
