package com.algo4chris.algo4chrisweb.controller.SessionTestController;

import com.algo4chris.algo4chriscommon.common.constant.InnerRouteConst;
import com.algo4chris.algo4chriscommon.exception.responsecode.MgrResponseCode;
import com.algo4chris.algo4chriscommon.exception.user.UserException;
import com.algo4chris.algo4chrisdal.models.Member;
import com.algo4chris.algo4chrisdal.repository.UserRepository;
import com.algo4chris.algo4chrisdal.session.SessionEntity;
import com.algo4chris.algo4chrisweb.security.jwt.JwtUtils;
import com.algo4chris.algo4chrisweb.security.services.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Base64;

@Api(tags = "內部測試Api")
@Slf4j
@RestController
@RequestMapping(InnerRouteConst.PATH)
public class SessionTestController {

    @Value("${knife4j.production:false}")
    private boolean isProd;

    @Resource
    UserRepository userRepository;

    @Resource
    ObjectMapper objectMapper;

    @Resource
    UserDetailsServiceImpl userDetailsService;

    @Resource
    JwtUtils jwtUtils;

    @ApiOperation(value = "產生sessionEntity", httpMethod = "GET")
    @GetMapping(value = "/session")
    public String createSession(@RequestParam(required = true,defaultValue = "chris") String userName) throws Exception {
        if(isProd) {
            return "無操作權限";
        }
        Member member = userRepository.findByMemberName(userName)
                .orElseThrow(() -> new UserException(MgrResponseCode.USER_NOT_FOUND,new Object[]{userName}));
        SessionEntity sessionEntity = SessionEntity.builder().userId(member.getId()).userName(userName).build();
        String writeValueAsString = objectMapper.writeValueAsString(sessionEntity);
        byte[] encode = Base64.getEncoder().encode(writeValueAsString.getBytes());
        return new String(encode);
    }

    @ApiOperation(value = "产生Authorization", httpMethod = "GET")
    @GetMapping(value = "/authorization")
    public String createAuthorization(@RequestParam(required = true,defaultValue = "chris") String userName) throws Exception {
        if(isProd) {
            return "无操作权限";
        }

        UserDetails userDetails = null;
        try{
            userDetails = userDetailsService.loadUserByUsername(userName);
        } catch (UsernameNotFoundException e){
            log.error("帐号名:{}登陆失败 找不到此账号",userName);
        }

        final String token = jwtUtils.generateTokenFromUsername(userDetails.getUsername());
        return token;
    }

}
