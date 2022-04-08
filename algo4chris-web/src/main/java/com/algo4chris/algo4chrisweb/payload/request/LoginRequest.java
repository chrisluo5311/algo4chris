package com.algo4chris.algo4chrisweb.payload.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 登入請求
 *
 * @author chris
 * */
@ApiModel(value = "登入請求")
@Getter
@Setter
public class LoginRequest {

    @ApiModelProperty(value = "用戶名",example = "chris")
    @NotNull(message = "請填寫用戶名")
    private String userName;

    @ApiModelProperty(value = "密碼")
    @NotNull(message = "請填寫密碼")
    private String password;


}
