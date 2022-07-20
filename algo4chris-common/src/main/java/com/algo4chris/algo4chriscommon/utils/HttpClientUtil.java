package com.algo4chris.algo4chriscommon.utils;

import cn.hutool.http.HttpRequest;
import lombok.extern.slf4j.Slf4j;

import java.net.SocketTimeoutException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


/**
 * 發送HttpClient請求
 * */
@Slf4j
public class HttpClientUtil {

    private static final String LOG_PREFIX = "[HttpClientUtil]";

    /**
     * 發送HttpClient Get請求
     * */
    public static void httpClientGet(String url, Map<String,String> header) throws SocketTimeoutException {
        try{
            if(header!=null){
                Set<String> keySet= header.keySet();
                Iterator<String> it2 = keySet.iterator();
                while(it2.hasNext()){
                    String key = it2.next();
                    HttpRequest.get(url)
                            .header(key,header.get(key))
                            .timeout(20000)
                            .execute();
                }
            }else{
                HttpRequest.get(url)
                        .timeout(20000)
                        .execute();
            }
        }catch (Exception e){
            log.info("{} HttpClientGet請求失敗 Url:{} Msg:{}",LOG_PREFIX,url,e.getMessage());
        }
    }

}
