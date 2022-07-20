package com.algo4chris.algo4chrisweb.security.jwt;

import com.algo4chris.algo4chriscommon.common.constant.HttpExceptionConst;
import com.algo4chris.algo4chriscommon.common.constant.JwtConstants;
import com.algo4chris.algo4chrisweb.security.services.UserDetailsImpl;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

import static com.algo4chris.algo4chriscommon.common.constant.JwtConstants.AUTHORIZATION_CODE_KEY;

/**
 * jwt 工具類
 *
 * @author chris
 * @Date 2022/01/29
 * */
@Slf4j
@Component
public class JwtUtils {

    /**
     * 解密後密鑰
     * */
    private String decryptJwtSecret;

    /**
     *  JWT 密鑰
     * */
    @Value("${algo4chris.app.jwtSecret}")
    private String jwtSecret;

    /**
     *  jwt token 超時時間: 1小時
     * */
    @Value("${algo4chris.app.jwtExpirationMs}")
    private int jwtExpirationMs;


    @PostConstruct
    public void init(){
        byte[] decode = Base64.getDecoder().decode(jwtSecret);
        decryptJwtSecret = new String(decode);
    }


    /**
     * 產生jwt token
     *
     * @param userPrincipal 用戶資訊
     * @return jwt token
     * */
    public String generateJwtToken(UserDetailsImpl userPrincipal) {
        return generateTokenFromUsername(userPrincipal.getUsername());
    }

    /**
     * 透過用戶名自產生jwt token
     *
     * @param memberName 用戶名
     * @return jwt token
     * */
    public String generateTokenFromUsername(String memberName) {
        return Jwts.builder()
                   .setSubject(memberName)
                   .setIssuedAt(new Date())
                   .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))//設expiration
                   .signWith(SignatureAlgorithm.HS512, decryptJwtSecret)//簽名方式(帶密鑰
                   .compact();
    }


    /**
     * 從 jwt token 中取出用戶名
     *
     * @param token jwt token
     * @return 用戶名
     * */
    public String getUserNameFromJwtToken(String token) {
       return Jwts.parser().setSigningKey(decryptJwtSecret).parseClaimsJws(token).getBody().getSubject();
    }


    public String getUserNameIgnoredExpired(String token){
        return Jwts.parser().setSigningKey(decryptJwtSecret).parsePlaintextJws(token).getBody();
    }

    /**
     * 驗證JWT
     *
     * @param authToken jwt token
     * @param servletRequest HttpServletRequest
     * */
    public boolean validateJwtToken(String authToken,HttpServletRequest servletRequest) {
        try {
            Jwts.parser().setSigningKey(decryptJwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            log.error("無效的 JWT 簽名: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("無效的 JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token 超時: {}", e.getMessage());
            servletRequest.setAttribute(HttpExceptionConst.JWT_EXPIRED_CODE_KEY ,e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token 不支持: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string 為空: {}", e.getMessage());
        }
        return false;
    }

    /**
     * 從 HttpServletRequest 的 Header 取 Authorization 的值<br>
     * 並截斷 Bearer 字段只取後方的token
     *
     * @param request HttpServletRequest
     * @return jwt token
     */
    public String parseJwt(HttpServletRequest request) {
        String jwtToken = null;
        String requestTokenHeader = null;
        if(request.getParameter(AUTHORIZATION_CODE_KEY)!=null){
            requestTokenHeader = request.getParameter(AUTHORIZATION_CODE_KEY);
        } else {
            requestTokenHeader = request.getHeader(AUTHORIZATION_CODE_KEY);
        }
        // JWT Token在"Bearer token"里 移除Bearer字段只取Token
        if (requestTokenHeader != null) {
            if (requestTokenHeader.startsWith(JwtConstants.BEARER_CODE_KEY)) {
                jwtToken = requestTokenHeader.substring(7);
            } else {
                log.warn("JWT Token 不在Bearer里面");
            }
        }
        return jwtToken;
    }

}


