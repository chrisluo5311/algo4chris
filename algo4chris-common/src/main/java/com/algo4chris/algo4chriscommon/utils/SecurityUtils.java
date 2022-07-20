package com.algo4chris.algo4chriscommon.utils;

import com.algo4chris.algo4chriscommon.exception.badrequest.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author chris
 *
 * */
@Slf4j
public class SecurityUtils {

    /**
     * 通過SecurityContextHolder獲取用戶名稱
     *
     * @return 系统用户名称
     */
    public static String getCurrentMemberName() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new BadRequestException(HttpStatus.UNAUTHORIZED.value(), "用戶登入狀態過期");
        }
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userDetails.getUsername();
        }
        throw new BadRequestException(HttpStatus.UNAUTHORIZED.value(), "查無用戶登入訊息");
    }
}
