package com.algo4chris.algo4chrisdal.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用戶狀態
 *
 * @author chris
 * */
@Getter
@AllArgsConstructor
public enum MemberStatus {

    ENABLE(1),
    DISABLE(-1);

    private Integer code;

}
