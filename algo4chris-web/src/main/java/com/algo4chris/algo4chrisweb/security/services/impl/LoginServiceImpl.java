package com.algo4chris.algo4chrisweb.security.services.impl;

import com.algo4chris.algo4chriscommon.common.constant.JwtConstants;
import com.algo4chris.algo4chriscommon.common.constant.RoleConstants;
import com.algo4chris.algo4chriscommon.exception.responsecode.MgrResponseCode;
import com.algo4chris.algo4chriscommon.exception.tokenrefresh.TokenRefreshException;
import com.algo4chris.algo4chriscommon.exception.user.UserException;
import com.algo4chris.algo4chriscommon.utils.IpUtils;
import com.algo4chris.algo4chrisdal.models.ERole;
import com.algo4chris.algo4chrisdal.models.RefreshToken;
import com.algo4chris.algo4chrisdal.models.Role;
import com.algo4chris.algo4chrisdal.models.User;
import com.algo4chris.algo4chrisdal.models.enums.UserStatus;
import com.algo4chris.algo4chrisdal.repository.RoleRepository;
import com.algo4chris.algo4chrisdal.repository.UserRepository;
import com.algo4chris.algo4chrisdal.session.SessionEntity;
import com.algo4chris.algo4chrisweb.payload.request.LoginRequest;
import com.algo4chris.algo4chrisweb.payload.request.SignupRequest;
import com.algo4chris.algo4chrisweb.payload.request.TokenRefreshRequest;
import com.algo4chris.algo4chrisweb.payload.response.JwtResponse;
import com.algo4chris.algo4chrisweb.payload.response.TokenRefreshResponse;
import com.algo4chris.algo4chrisweb.security.jwt.JwtUtils;
import com.algo4chris.algo4chrisweb.security.services.LoginService;
import com.algo4chris.algo4chrisweb.security.services.RefreshTokenService;
import com.algo4chris.algo4chrisweb.security.services.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 登入/登出/註冊service實作類
 *
 * @author chris
 * @Date 2022/02/29
 */
@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    private static final String LOG_PREFIX = "[LoginServiceImpl]";

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    AuthenticationManager authenticationManager;

    @Resource
    RefreshTokenService refreshTokenService;

    @Resource
    UserRepository userRepository;

    @Resource
    RoleRepository roleRepository;

    @Resource
    PasswordEncoder encoder;

    @Resource
    JwtUtils jwtUtils;

    /**
     * 登入<br>
     * jwt token 时效为1小时<br>
     * refresh token 时效为24小时
     *
     * @param loginRequest 登入請求
     * @return JwtResponse
     * */
    @Override
    public JwtResponse loginMember(LoginRequest loginRequest) {
        String userName = loginRequest.getUserName();
        String password = loginRequest.getPassword();
        log.info("{} 用戶:{} 發送登入請求", LOG_PREFIX, userName);
        //驗證 用戶名與密碼
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
        } catch (DisabledException e) {
            log.error("用戶名:{}登入失败 USER_DISABLED : {}", userName, e.getMessage());
            throw new UserException(MgrResponseCode.USER_NOT_FOUND, new Object[]{userName});
        } catch (BadCredentialsException e) {
            log.error("用戶名:{}登入失败 INVALID_CREDENTIALS : {}", userName, e.getMessage());
            throw new UserException(MgrResponseCode.USER_PASSWORD_INVALID, new Object[]{userName});
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        //產生jwtToken
        String jwtToken = jwtUtils.generateJwtToken(userDetails);
        //角色
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        log.info("{} 用戶:{} 擁有角色:{} 登入成功", LOG_PREFIX, userName, roles);
        //回傳JwtResponse
        return new JwtResponse(jwtToken, refreshToken.getToken(), userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles);
    }

    /**
     * 注册<br>
     * 未輸入權限一律預設為一般使用者
     *
     * @param signUpRequest 注册请求
     * @param servletRequest HttpServletRequest
     * @return User 用户
     * */
    @Transactional
    @Override
    public User signUp(SignupRequest signUpRequest, HttpServletRequest servletRequest) {
        String ip       = IpUtils.getIpAddr(servletRequest);
        String email    = signUpRequest.getEmail();
        String userName = signUpRequest.getUserName();
        log.info("{} 新用戶:{} 註冊帳號", LOG_PREFIX, userName);
        if (userRepository.existsByUserName(userName)) {
            throw new UserException(MgrResponseCode.USER_ALREADY_EXISTS, new Object[]{userName});
        }

        if (userRepository.existsByEmail(email)) {
            throw new UserException(MgrResponseCode.USER_EMAIL_ALREADY_EXISTS, new Object[]{email});
        }

        //創建用戶 狀態:預設 1:啟用
        User user = User.builder()
                        .userName(userName)
                        .email(email)
                        .password(encoder.encode(signUpRequest.getPassword()))
                        .ip(ip)
                        .status(UserStatus.ENABLE.getCode())
                        .createTime(new Date())
                        .build();
        //取得註冊腳色
        Set<Integer> intRoles = signUpRequest.getRole();
        Set<Role> userRoles = new HashSet<>();
        //为 null 预设USER
        if(Objects.isNull(intRoles)){
            roleRepository.findById(ERole.ROLE_USER.getRoleId())
                    .map(userRoles::add)
                    .orElseThrow(() ->
                            new UserException(MgrResponseCode.ROLE_NOT_FOUND, new Object[]{ERole.ROLE_USER}));
        } else {
            //未知權限一律預設為一般使用者
            intRoles.forEach(r -> {
                switch (r) {
                    case RoleConstants.ROLE_ADMIN_INT:
                        roleRepository.findById(ERole.ROLE_ADMIN.getRoleId())
                                .map(userRoles::add)
                                .orElseThrow(() ->
                                        new UserException(MgrResponseCode.ROLE_NOT_FOUND, new Object[]{ERole.ROLE_ADMIN}));
                    case RoleConstants.ROLE_SELLER_INT:
                        roleRepository.findById(ERole.ROLE_SELLER.getRoleId())
                                .map(userRoles::add)
                                .orElseThrow(() ->
                                        new UserException(MgrResponseCode.ROLE_NOT_FOUND, new Object[]{ERole.ROLE_SELLER}));
                    default:
                        roleRepository.findById(ERole.ROLE_USER.getRoleId())
                                .map(userRoles::add)
                                .orElseThrow(() ->
                                        new UserException(MgrResponseCode.ROLE_NOT_FOUND, new Object[]{ERole.ROLE_USER}));
                }
            });
        }

        user.setRoles(userRoles);
        User userResult = userRepository.save(user);
        log.info("{} 新用戶:{} 註冊成功", LOG_PREFIX, userResult.getUserName());
        return userResult;
    }

    /**
     * 獲得新token
     *
     * @param refreshRequest Token Refresh 請求
     * @return TokenRefreshResponse
     * */
    @Transactional
    @Override
    public TokenRefreshResponse refreshToken(TokenRefreshRequest refreshRequest) {
        String requestRefreshToken = refreshRequest.getRefreshToken();
        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromUsername(user.getUserName());
                    return new TokenRefreshResponse(token, requestRefreshToken);
                })
                .orElseThrow(() -> new TokenRefreshException(MgrResponseCode.REFRESH_TOKEN_NOT_EXISTS_IN_DB, requestRefreshToken));
    }

    /**
     * 登出用戶
     *
     * @param servletRequest HttpServletRequest
     * @param sessionEntity Session
     * */
    @Override
    public void logOutUser(SessionEntity sessionEntity, HttpServletRequest servletRequest) {
        Long userId = sessionEntity.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(MgrResponseCode.USER_NOT_FOUND, new Object[]{userId}));
        refreshTokenService.deleteByUserId(user);
        String jwtToken = jwtUtils.parseJwt(servletRequest);
        redisTemplate.opsForValue().set(jwtToken, jwtToken, JwtConstants.LOGOUT_EXPIRATION_TIME, TimeUnit.HOURS);
        log.info("{} 用戶:{} 登出裝置成功", LOG_PREFIX, user.getUserName());
    }


}
