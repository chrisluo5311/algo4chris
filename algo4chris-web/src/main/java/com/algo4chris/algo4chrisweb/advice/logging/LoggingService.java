package com.algo4chris.algo4chrisweb.advice.logging;


import com.algo4chris.algo4chrisweb.advice.annotations.HttpRequestElements;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface LoggingService {

    void logRequest(HttpServletRequest httpServletRequest, Object body, List<HttpRequestElements> httpElementsList);

    void logResponse(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object body);
}
