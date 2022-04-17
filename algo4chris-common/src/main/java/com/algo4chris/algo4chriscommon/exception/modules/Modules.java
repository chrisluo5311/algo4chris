package com.algo4chris.algo4chriscommon.exception.modules;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@AllArgsConstructor
public enum Modules {

    USER("USER"),
    REFRESH_TOKEN("refreshToken"),

    BAD_REQUEST("BAD_REQUEST");

    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
