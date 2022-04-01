package com.algo4chris.algo4chrisweb.payLoad.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@ApiModel(value = "登出請求")
@Getter
public class LogOutRequest {

    @ApiModelProperty(value = "用戶id")
    @NotNull
    private Integer userId;
}
