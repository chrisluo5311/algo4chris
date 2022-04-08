package com.algo4chris.algo4chrisweb.security.services;

import com.algo4chris.algo4chrisdal.models.BlackWhiteList;
import com.algo4chris.algo4chrisdal.repository.BlackWhiteListRepository;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * api 請求次數限制service
 *
 * @author chris
 * */
@Slf4j
@Service
public class RateLimitService {

    /** 一般人員每秒限制次數 */
    private static final int NORMAL_MAX_REQUEST = 2;
    /** 測試人員每秒限制次數 */
    private static final int PRO_TEST_MAX_REQUEST = 30;

    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    @Resource
    BlackWhiteListRepository blackWhiteListRepo;

    private List<String> host;

    @PostConstruct
    public void initIpWhiteList(){
        //載入白名單
        host = blackWhiteListRepo.findByType(1)
                                 .stream()
                                 .map(BlackWhiteList::getIp)
                                 .collect(Collectors.toList());
    }

    public Bucket getBucket(String ip){
        return cache.computeIfAbsent(ip,this::newBucket);
    }

    public Bucket newBucket(String ip) {
        if(host.contains(ip)){
            log.info("【ip】:{} 測試人員 每秒請求次數限制:{}",ip,PRO_TEST_MAX_REQUEST);
            return Bucket.builder()
                    .addLimit(Bandwidth.classic(PRO_TEST_MAX_REQUEST, Refill.greedy(PRO_TEST_MAX_REQUEST, Duration.ofSeconds(1))))
                    .build();
        } else {
            log.info("【ip】:{} 一般用戶 每秒請求次數限制:{}",ip,NORMAL_MAX_REQUEST);
            return Bucket.builder()
                    .addLimit(Bandwidth.classic(NORMAL_MAX_REQUEST, Refill.greedy(NORMAL_MAX_REQUEST, Duration.ofSeconds(1))))
                    .build();
        }
    }


}
