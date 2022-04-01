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

    @ApiOperation(value = "user角色", httpMethod = "GET")
    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String userAccess() {
        return "User Content.";
    }

    @ApiOperation(value = "mod角色", httpMethod = "GET")
    @GetMapping("/mod")
    @PreAuthorize("hasRole('MODERATOR')")
    public String moderatorAccess() {
        return "Moderator Board.";
    }

    @ApiOperation(value = "admin角色", httpMethod = "GET")
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "Admin Board.";
    }

}
