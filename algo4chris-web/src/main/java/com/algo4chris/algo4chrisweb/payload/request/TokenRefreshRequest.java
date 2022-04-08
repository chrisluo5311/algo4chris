package com.algo4chris.algo4chrisweb.payload.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "Token Refresh 請求")
@Getter
@Setter
public class TokenRefreshRequest {

    @ApiModelProperty(value = "Refresh Token")
    @NotBlank
    private String refreshToken;

}
