package com.algo4chris.algo4chrisweb.advice.logging;

import com.algo4chris.algo4chrisweb.advice.annotations.HttpRequestElements;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Component
@Slf4j
public class LoggingServiceImpl implements LoggingService {

    @Override
    public void logRequest(HttpServletRequest httpServletRequest, Object body, List<HttpRequestElements> httpElementsList) {
        if(Objects.isNull(httpElementsList)){
            HttpRequestElements.All.writeLog(httpServletRequest,body);
        } else {
            httpElementsList.forEach(httpElements -> httpElements.writeLog(httpServletRequest,body));
        }
    }

    @Override
    public void logResponse(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object body) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("RESPONSE LOG :");
        stringBuilder.append("request method=[").append(httpServletRequest.getMethod()).append("] ");
        stringBuilder.append("path=[").append(httpServletRequest.getRequestURI()).append("] ");
        stringBuilder.append("responseHeaders=[").append(buildHeadersMap(httpServletResponse)).append("] ");
        stringBuilder.append("responseBody=[").append(body).append("] ");

        log.info(stringBuilder.toString());
    }

    private Map<String, String> buildHeadersMap(HttpServletResponse response) {
        Map<String, String> map = new HashMap<>();

        Collection<String> headerNames = response.getHeaderNames();
        for (String header : headerNames) {
            map.put(header, response.getHeader(header));
        }
        return map;
    }
}
