package com.algo4chris.algo4chrisweb.payLoad.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * 註冊請求
 *
 * @author chris
 * */
@ApiModel(value = "註冊請求")
@Getter
@Setter
public class SignupRequest {

    @ApiModelProperty(value = "用戶名",example = "chris")
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @ApiModelProperty(value = "EMAIL")
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @ApiModelProperty(value = "角色(1:user 2:mod 3:admin) 不填將預設1",example = "1")
    private Set<Integer> role;

    @ApiModelProperty(value = "密碼")
    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

}
