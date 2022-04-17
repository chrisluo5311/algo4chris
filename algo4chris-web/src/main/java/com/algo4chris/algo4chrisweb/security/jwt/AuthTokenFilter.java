package com.algo4chris.algo4chrisweb.security.jwt;


import com.algo4chris.algo4chriscommon.common.constant.HttpExceptionConst;
import com.algo4chris.algo4chriscommon.utils.LogUtil;
import com.algo4chris.algo4chriscommon.utils.RandomUtil;
import com.algo4chris.algo4chrisdal.session.SessionEntity;
import com.algo4chris.algo4chriscommon.exception.responsecode.MgrResponseCode;
import com.algo4chris.algo4chriscommon.exception.user.UserJwtException;
import com.algo4chris.algo4chrisweb.security.services.RateLimitService;
import com.algo4chris.algo4chrisweb.security.services.UserDetailsImpl;
import com.algo4chris.algo4chrisweb.security.services.UserDetailsServiceImpl;
import com.algo4chris.algo4chriscommon.utils.WebUtils;
import com.algo4chris.algo4chriscommon.utils.SessionUtils;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 過濾每一次請求<br>
 * 若api有註冊則跳過驗證jwt token
 *
 * @author chris
 * */
@Slf4j
public class AuthTokenFilter extends OncePerRequestFilter {

    @Resource
    JwtUtils jwtUtils;

    @Resource
    SessionUtils sessionUtils;

    @Resource
    RateLimitService rateLimitService;

    @Resource
    private UserDetailsServiceImpl userDetailsService;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        LogUtil.setMDC(LogUtil.MDCKey.RandomCode,RandomUtil.getRandom(10));
        String ip = WebUtils.getIp(request);
        log.info("【ip】:{} 【Request Method】:{} 【URI】: {}?{}",ip,request.getMethod(), request.getRequestURI(), request.getQueryString());
//        HttpRequestElements.printGetMethodLog(request);

        Bucket bucket = rateLimitService.getBucket(ip);
        try{
            ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
            if (!probe.isConsumed()) {
                long waitForRefill = probe.getNanosToWaitForRefill() / 1_000_000_000; //單位納秒(1000,000,000 納秒 = 1 秒)
                log.error("【ip】:{} 每秒請求次數超過限制  剩餘:{} 秒回充請求次數 ",ip,waitForRefill);
                request.setAttribute(HttpExceptionConst.API_OUT_OF_LIMIT, HttpStatus.TOO_MANY_REQUESTS);
                throw new BadCredentialsException(MgrResponseCode.TOO_MANY_REQUESTS.getMessage());
            }
            //取 JWT
            String jwt = jwtUtils.parseJwt(request);
            if(jwt != null){
                String memberName = jwtUtils.getUserNameFromJwtToken(jwt);
                LogUtil.setMDC(LogUtil.MDCKey.MemberName,memberName);
                //查看 redis 登出黑名單
                if(Boolean.TRUE.equals(redisTemplate.hasKey(jwt))){
                    throw new UserJwtException(MgrResponseCode.MEMBER_ALREADY_LOGOUT,new Object[]{memberName});
                }
                if(jwtUtils.validateJwtToken(jwt,request)){
                    UserDetails userDetails = userDetailsService.loadUserByUsername(memberName);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    UserDetailsImpl userDetails1 = (UserDetailsImpl) userDetails;
                    SessionEntity sessionEntity = SessionEntity.builder()
                                                               .userId(userDetails1.getId())
                                                               .userName(memberName)
                                                               .email(userDetails1.getEmail())
                                                               .ip(ip)
                                                               .build();
                    //request header中設置session
                    sessionUtils.pushSessionToRequest(sessionEntity,request);
                }
            }
        } catch (ExpiredJwtException e) {
            request.setAttribute(HttpExceptionConst.JWT_EXPIRED_CODE_KEY ,e.getMessage());
        } catch (Exception e){
            log.error("AuthTokenFilter 發生Exception原因: {}",e.getMessage());
        }
        filterChain.doFilter(request, response);
        MDC.clear();
    }

}
