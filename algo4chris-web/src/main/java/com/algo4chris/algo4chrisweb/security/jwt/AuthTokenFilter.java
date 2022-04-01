package com.algo4chris.algo4chrisweb.security.jwt;


import com.algo4chris.algo4chriscommon.common.constant.JwtConstants;
import com.algo4chris.algo4chrisdal.session.SessionEntity;
import com.algo4chris.algo4chriscommon.exception.responsecode.MgrResponseCode;
import com.algo4chris.algo4chriscommon.exception.user.UserJwtException;
import com.algo4chris.algo4chrisweb.security.services.UserDetailsImpl;
import com.algo4chris.algo4chrisweb.security.services.UserDetailsServiceImpl;
import com.algo4chris.algo4chriscommon.utils.IpUtils;
import com.algo4chris.algo4chriscommon.utils.SessionUtils;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
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
    private UserDetailsServiceImpl userDetailsService;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String ip = IpUtils.getIpAddr(request);
        log.info("從 ip {}:發送請求uri: {}",ip, request.getRequestURI());

        try{
            //取 JWT
            String jwt = jwtUtils.parseJwt(request);
            if( jwt!=null){
                String userName = jwtUtils.getUserNameFromJwtToken(jwt);
                //查看 redis 登出黑名單
                if(Boolean.TRUE.equals(redisTemplate.hasKey(jwt))){
                    throw new UserJwtException(MgrResponseCode.USER_ALREADY_LOGOUT,new Object[]{userName});
                }
                if(jwtUtils.validateJwtToken(jwt,request)){
                    UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    UserDetailsImpl userDetails1 = (UserDetailsImpl) userDetails;
                    SessionEntity sessionEntity = SessionEntity.builder()
                                                               .userId(userDetails1.getId())
                                                               .userName(userName)
                                                               .ip(ip)
                                                               .build();
                    //request header中設置session
                    sessionUtils.pushSessionToRequest(sessionEntity,request);
                }
            }
        } catch (ExpiredJwtException e) {
            request.setAttribute(JwtConstants.JWT_EXPIRED_CODE_KEY ,e.getMessage());
        } catch (Exception e){
            log.error("無法設置用戶權限: {}",e);
        }
        filterChain.doFilter(request, response);
    }

}
