package com.algo4chris.algo4chrisweb.controller.RoleTestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 權限管理測試
 *
 * @author chris
 * */
@Api(tags = "權限管理測試")
@Slf4j
@RestController
@RequestMapping("/api/test")
public class TestController {

    @ApiOperation(value = "全部角色", httpMethod = "GET")
    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }

    @ApiOperation(value = "MEMBER角色", httpMethod = "GET")
    @GetMapping("/allMember")
    @PreAuthorize("hasRole('ROLE_MEMBER') or hasRole('ROLE_SELLER') or hasRole('ROLE_ADMIN')")
    public String userAccess() {
        return "Member Content.";
    }

    @ApiOperation(value = "SELLER角色", httpMethod = "GET")
    @GetMapping("/seller")
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public String moderatorAccess() {
        return "Moderator Board.";
    }

    @ApiOperation(value = "admin角色", httpMethod = "GET")
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String adminAccess() {
        return "Admin Board.";
    }

}
