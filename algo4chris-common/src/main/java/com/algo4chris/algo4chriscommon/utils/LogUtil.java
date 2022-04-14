package com.algo4chris.algo4chriscommon.utils;

import org.slf4j.MDC;

import java.util.Map;

/**
 * MDC的 key、value 包裝
 *
 * @author chris
 * */
public class LogUtil {

    public static void setMDC(MDCKey key, String value){
        //getCopyOfContextMap: Return a copy of the current thread's context map
        //Returned value may be null.
        Map<String, String> preMDC = MDC.getCopyOfContextMap();
        value = key.getKey()+":"+value;
        if( preMDC != null &&  preMDC.size() > 0){
            value = " "+value;
        }
        MDC.put(key.getKey(), value);
    }

    public enum MDCKey {
        RandomCode("ramCode"),
        MemberName("memberName");

        private final String key;

        MDCKey(String key) {
            this.key = key;
        }
        public String getKey() {
            return this.key;
        }
    }
}
