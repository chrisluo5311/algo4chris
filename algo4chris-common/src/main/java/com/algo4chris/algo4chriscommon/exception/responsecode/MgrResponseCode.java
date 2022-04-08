package com.algo4chris.algo4chriscommon.exception.responsecode;

import lombok.AllArgsConstructor;

/**
 * 錯誤響應碼與信息對照表
 *
 * @author chris
 * @date 2022/01/29
 * */
@AllArgsConstructor
public enum MgrResponseCode {

    SUCCESS("0000", "成功"),

    INVALID_TOKEN("0001", "無效Token"),
    INVALID_CAPTCHA("0002", "驗證碼錯誤"),
    INVALID_REQUEST("0003", "請求錯誤"),
    INVALID_REMOTE_API("0004", "遠程調用異常"),
    CAPTCHA_EXPIRE_OR_NOT_EXIST("0005", "驗證碼超時或不存在，請重新產生"),
    JWT_TOKEN_EXPIRED("0006","Jwt Token 已超時，請重新登入"),
    REFRESH_TOKEN_EXPIRED("0007","Refresh Token 已超時，請重新獲取"),
    REFRESH_TOKEN_NOT_EXISTS_IN_DB("0008","Refresh Token 不存在 DB"),
    REQUEST_WITHOUT_TOKEN("0009","請求中未帶有Session Token"),
    REQUEST_ACCESS_DENIED("0010","非法請求權限，拒絕存取"),
    TOO_MANY_REQUESTS("0011","Too Many Requests"),

    PARAM_NOT_FOUND("0101", "參數不存在"),
    PARAM_INVALID("0102", "無效的參數"),

    USER_NOT_FOUND("0201","查無用戶"),
    USER_ALREADY_EXISTS("0202","用戶已存在"),
    USER_EMAIL_ALREADY_EXISTS("0203","用戶EMAIL已存在"),
    USER_ROLES_NOT_FOUND("0204","用戶權限不存在"),
    USER_ALREADY_LOGOUT("0205","用戶已經登出"),
    USER_PASSWORD_INVALID("0206","用戶或密碼錯誤"),
    USER_DISABLED("0207", "用戶狀態為禁用"),

    ROLE_NOT_FOUND("0301","DB權限表不存在"),

    DB_FAIL("0401", "資料庫操作失敗"),

    UNKNOWN_ERROR("9999", "系统错误");

    private String code;
    private String message;

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

}
