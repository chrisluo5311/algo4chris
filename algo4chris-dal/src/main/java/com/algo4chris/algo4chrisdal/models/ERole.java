package com.algo4chris.algo4chrisdal.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 角色類型
 * */
@Getter
@AllArgsConstructor
public enum ERole {
    ROLE_USER(1),
    ROLE_SELLER(2),
    ROLE_ADMIN(3);

    private Integer roleId;
}
