package com.algo4chris.algo4chrisweb.security.services.impl;

import com.algo4chris.algo4chriscommon.common.constant.JwtConstants;
import com.algo4chris.algo4chriscommon.exception.responsecode.MgrResponseCode;
import com.algo4chris.algo4chriscommon.exception.tokenrefresh.TokenRefreshException;
import com.algo4chris.algo4chriscommon.exception.user.UserException;
import com.algo4chris.algo4chriscommon.utils.WebUtils;
import com.algo4chris.algo4chrisdal.models.*;
import com.algo4chris.algo4chrisdal.models.enums.ERole;
import com.algo4chris.algo4chrisdal.models.enums.Provider;
import com.algo4chris.algo4chrisdal.repository.RoleRepository;
import com.algo4chris.algo4chrisdal.repository.UserRepository;
import com.algo4chris.algo4chrisdal.session.SessionEntity;
import com.algo4chris.algo4chrisweb.payload.request.AlgoOAuth2User;
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
import org.springframework.security.core.GrantedAuthority;
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
        String memberName = loginRequest.getMemberName();
        String password   = loginRequest.getPassword();
        log.info("{} 用戶:{} 發送登入請求", LOG_PREFIX, memberName);
        //驗證 用戶名與密碼
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(memberName, password));
        } catch (DisabledException e) {
            log.error("用戶名:{}登入失败 USER_DISABLED : {}", memberName, e.getMessage());
            throw new UserException(MgrResponseCode.MEMBER_NOT_FOUND, new Object[]{memberName});
        } catch (BadCredentialsException e) {
            log.error("用戶名:{}登入失败 INVALID_CREDENTIALS : {}", memberName, e.getMessage());
            throw new UserException(MgrResponseCode.MEMBER_PASSWORD_INVALID, new Object[]{memberName});
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        //產生jwtToken
        String jwtToken = jwtUtils.generateJwtToken(userDetails);
        //角色
        List<String> roles = userDetails.getAuthorities()
                                        .stream()
                                        .map(GrantedAuthority::getAuthority)
                                        .collect(Collectors.toList());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        log.info("{} 用戶:{} 擁有角色:{} 登入成功", LOG_PREFIX, memberName, roles);
        //回傳JwtResponse
        return new JwtResponse(jwtToken, refreshToken.getToken(), userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles);
    }

    /**
     * 注册<br>
     * 未輸入權限一律預設為一般使用者
     *
     * @param signUpRequest 注册请求
     * @param servletRequest HttpServletRequest
     * @return Member 用户
     * */
    @Transactional
    @Override
    public Member signUp(SignupRequest signUpRequest, HttpServletRequest servletRequest) {
        String ip         = WebUtils.getIp(servletRequest);
        String email      = signUpRequest.getEmail();
        String memberName = signUpRequest.getMemberName();
        log.info("{} 新用戶:{} 註冊帳號", LOG_PREFIX, memberName);
        existsByMemberNameAndEmail(memberName,email);

        //創建用戶 狀態:預設 1:啟用
        Member member = Member.builder()
                              .memberName(memberName)
                              .email(email)
                              .password(encoder.encode(signUpRequest.getPassword()))
                              .ip(ip)
                              .createTime(new Date())
                              .build();
        //取得註冊腳色
        Set<Integer> intRoles = signUpRequest.getRole();
        Set<Role> userRoles   = new HashSet<>();
        //为 null 预设USER
        if(Objects.isNull(intRoles)){
            roleRepository.findById(ERole.ROLE_MEMBER.getRoleId())
                    .map(userRoles::add)
                    .orElseThrow(() ->
                            new UserException(MgrResponseCode.ROLE_NOT_FOUND, new Object[]{ERole.ROLE_MEMBER}));
        } else {
            //未知權限一律預設為一般使用者
            intRoles.stream().map(ERole::getERole).forEach(e->{
                roleRepository.findById(e.getRoleId()).map(userRoles::add).orElseThrow(() ->
                        new UserException(MgrResponseCode.ROLE_NOT_FOUND, new Object[]{e}));
            });
        }
        member.setRoles(userRoles);
        Member memberResult = userRepository.save(member);
        log.info("{} 新用戶:{} 註冊成功", LOG_PREFIX,memberName);
        return memberResult;
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
                .map(RefreshToken::getMember)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromUsername(user.getMemberName());
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
        Member member = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(MgrResponseCode.MEMBER_NOT_FOUND, new Object[]{userId}));
        refreshTokenService.deleteByUserId(member);
        String jwtToken = jwtUtils.parseJwt(servletRequest);
        redisTemplate.opsForValue().set(jwtToken, jwtToken, JwtConstants.LOGOUT_EXPIRATION_TIME, TimeUnit.HOURS);
        log.info("{} 用戶:{} 登出裝置成功", LOG_PREFIX, member.getMemberName());
    }

    @Override
    public JwtResponse processOAuthPostLogin(AlgoOAuth2User oAuth2User,String ip) {
        String memberName = oAuth2User.getName();
        if(!userRepository.existsByMemberName(memberName)){
            Member member = Member.builder()
                    .memberName(memberName)
                    .email(oAuth2User.getEmail())
                    .ip(ip)
                    .createTime(new Date())
                    .provider(Provider.GOOGLE)
                    .build();
            Set<Role> memberRoles   = new HashSet<>();
            roleRepository.findById(ERole.ROLE_MEMBER.getRoleId())
                    .map(memberRoles::add)
                    .orElseThrow(() ->
                            new UserException(MgrResponseCode.ROLE_NOT_FOUND, new Object[]{ERole.ROLE_MEMBER}));
            member.setRoles(memberRoles);
            Member result = userRepository.save(member);
            //產生jwtToken
            String jwtToken = jwtUtils.generateTokenFromUsername(memberName);
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(result.getId());
            List<String> rolesList = memberRoles.stream().map(Role::getName).map(ERole::name).collect(Collectors.toList());
            log.info("{} 新用戶:{} 來源:{} 擁有角色:{} 註冊成功",LOG_PREFIX,memberName,Provider.GOOGLE,memberRoles);
            //回傳JwtResponse
            return new JwtResponse(jwtToken, refreshToken.getToken(),result.getId(),
                                   memberName, oAuth2User.getEmail(), rolesList);
        } else {
            throw new UserException(MgrResponseCode.MEMBER_ALREADY_EXISTS,new Object[]{memberName});
        }
    }

    /**
     * 用戶名與Email是否重複
     *
     * @param memberName 用戶名
     * @param email email
     * */
    public void existsByMemberNameAndEmail(String memberName,String email) throws UserException{
        if (userRepository.existsByMemberName(memberName)) {
            throw new UserException(MgrResponseCode.MEMBER_ALREADY_EXISTS, new Object[]{memberName});
        }

        if (userRepository.existsByEmail(email)) {
            throw new UserException(MgrResponseCode.MEMBER_EMAIL_ALREADY_EXISTS, new Object[]{email});
        }
    }

}
