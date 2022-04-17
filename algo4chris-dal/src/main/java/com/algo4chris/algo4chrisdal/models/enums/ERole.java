package com.algo4chris.algo4chrisdal.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

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

    /**
     * 匹配角色roleId
     * 預設 ROLE_USER
     *
     * @param roleId 前台傳入的roleId
     * */
    public static ERole getERole(int roleId){
        return Arrays.stream(ERole.values())
                .filter(e->e.getRoleId()==roleId)
                .findFirst()
                .orElse(ERole.ROLE_USER);
    }

}
