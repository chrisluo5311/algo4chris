package com.algo4chris.algo4chrisweb.advice.annotations;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum HttpRequestElements implements HttpRequestLogService {

    All(0){
        @Override
        public void writeLog(HttpServletRequest httpServletRequest, Object body) {
            //不指定列印
            StringBuilder stringBuilder = new StringBuilder();

            Map<String, String> parameters = buildParametersMap(httpServletRequest);
            stringBuilder.append("Request LOG :");
            stringBuilder.append("method=[").append(httpServletRequest.getMethod()).append("] ");
            stringBuilder.append("path=[").append(httpServletRequest.getRequestURI()).append("] ");
            stringBuilder.append("headers=[").append(buildHeadersMap(httpServletRequest)).append("] ");
            if (!parameters.isEmpty()) {
                stringBuilder.append("parameters=[").append(parameters).append("] ");
            }
            if (body != null) {
                stringBuilder.append("body=[" + body + "]");
            }
            log.info("{}",stringBuilder);
        }
    },
    Method(1){
        @Override
        public void writeLog(HttpServletRequest httpServletRequest, Object body) {
            StringBuilder methodBuilder = new StringBuilder();
            methodBuilder.append("Request method=[").append(httpServletRequest.getMethod()).append("] ");
            log.info("{}",methodBuilder);
        }
    },
    Path(2){
        @Override
        public void writeLog(HttpServletRequest httpServletRequest, Object body) {
            StringBuilder pathBuilder = new StringBuilder();
            pathBuilder.append("Request path=[").append(httpServletRequest.getRequestURI()).append("] ");
            log.info("{}",pathBuilder);
        }
    },
    Headers(3){
        @Override
        public void writeLog(HttpServletRequest httpServletRequest, Object body) {
            Map<String, String> parameters = buildParametersMap(httpServletRequest);
            StringBuilder headersBuilder = new StringBuilder();
            headersBuilder.append("Request headers=[").append(buildHeadersMap(httpServletRequest)).append("] ");
            if (!parameters.isEmpty()) {
                headersBuilder.append("parameters=[").append(parameters).append("] ");
            }
            log.info("{}",headersBuilder);
        }
    },
    Body(4){
        @Override
        public void writeLog(HttpServletRequest httpServletRequest, Object body) {
            StringBuilder bodyBuilder = new StringBuilder();
            if (body != null) {
                bodyBuilder.append("Request body=[" + body + "]");
            }
            log.info("{}",bodyBuilder);
        }
    };

    /** 順序 */
    private int sequence;

    public Map<String, String> buildParametersMap(HttpServletRequest httpServletRequest) {
        Map<String, String> resultMap = new HashMap<>();
        Enumeration<String> parameterNames = httpServletRequest.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String key = parameterNames.nextElement();
            String value = httpServletRequest.getParameter(key);
            resultMap.put(key, value);
        }
        return resultMap;
    }

    public Map<String, String> buildHeadersMap(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        return map;
    }

    /**
     * GET請求印製請求log
     *
     * */
    public static void printGetMethodLog(HttpServletRequest request){
        if(request.getMethod().equals("GET")){
            HttpRequestElements.All.writeLog(request,null);
        }
    }

}
