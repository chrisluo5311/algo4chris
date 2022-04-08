package com.algo4chris.algo4chrisweb.advice.annotations;

import javax.servlet.http.HttpServletRequest;

public interface HttpRequestLogService {

    void writeLog(HttpServletRequest httpServletRequest, Object body);

}
