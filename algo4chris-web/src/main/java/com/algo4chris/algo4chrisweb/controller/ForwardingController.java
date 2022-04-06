package com.algo4chris.algo4chrisweb.controller;

import com.algo4chris.algo4chrisdal.session.SessionEntity;
import com.algo4chris.algo4chrisweb.config.AlgoProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@EnableWebMvc
@RequestMapping("/")
@CrossOrigin
@Slf4j
public class ForwardingController {

    private static final String MGR_API = "/api/mgr";

    @Resource
    RestTemplate restTemplate;
    
    @Resource
    ObjectMapper objectMapper;


    @RequestMapping(value = MGR_API + "/**", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object forwardToMgr(HttpServletRequest request,
                               SessionEntity sessionEntity) throws IOException {
        return forward(request, AlgoProperties.serviceHost , sessionEntity);
    }

    private Object forward(HttpServletRequest request,
                           String host,
                           SessionEntity sessionEntity) throws IOException {
        String apiUri = request.getRequestURI().substring(ForwardingController.MGR_API.length());
        String queryString = request.getQueryString();
        String url = host + apiUri + (queryString != null ? "?" + queryString : "");
        url = URLDecoder.decode(url, StandardCharsets.UTF_8.name());
        log.info("【ForwardingController】转发送请求url: {} ",url);

        HttpHeaders headers = new HttpHeaders();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.set(headerName, request.getHeader(headerName));
        }
        //sessionEntity 里目前仅有 用户名称 与 jwt token , ip
        headers.set("BestPay-Session", encodeSessionEntity(sessionEntity));

        Object body = null;

        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        if (request instanceof MultipartHttpServletRequest) {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            Map<String, String[]> parameterMap = multipartRequest.getParameterMap();
            for(Map.Entry<String, String[]> entry :parameterMap.entrySet()) {
                map.add(entry.getKey(), entry.getValue()[0]);
                log.info("multipart/form-data parameter key:{}, val:{}", entry.getKey(), Arrays.toString(entry.getValue()));
            }

            Iterator<String> filenames = multipartRequest.getFileNames();
            while (filenames.hasNext()) {
                MultipartFile file = multipartRequest.getFile(filenames.next());
                if (file != null) {
                    map.add(file.getName(), new ByteArrayResource(file.getBytes()) {
                        @Override
                        public String getFilename() {
                            return file.getOriginalFilename();
                        }
                    });
                }
            }
            body = map;
        } else {
            headers.setContentType(MediaType.APPLICATION_JSON);
            if (request.getContentLength() > 0) {
                BufferedReader bufferedReader = request.getReader();
                body = bufferedReader.lines().collect(Collectors.joining(System.lineSeparator()));
            }
        }

        HttpEntity<?> entity = new HttpEntity<>(body, headers);
        SimpleClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(1000);
        clientHttpRequestFactory.setReadTimeout(180000);
        restTemplate.setRequestFactory(clientHttpRequestFactory);
        if (apiUri.endsWith("excel") || apiUri.endsWith("csv")) {
            return restTemplate.exchange(url, HttpMethod.resolve(request.getMethod()), entity, byte[].class);
        } else {
            ResponseEntity<String> responseEntity =
                    restTemplate.exchange(url, HttpMethod.resolve(request.getMethod()), entity, String.class);
            return responseEntity.getBody();
        }
    }

    private String encodeSessionEntity(SessionEntity sessionEntity) {
        try {
            String json = objectMapper.writeValueAsString(sessionEntity);
            log.info("【ForwardingController】转发送请求json: {} ",json);
            return Base64.getEncoder().encodeToString(json.getBytes(StandardCharsets.UTF_8));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }
}
