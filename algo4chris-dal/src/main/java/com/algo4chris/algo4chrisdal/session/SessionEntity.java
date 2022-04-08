package com.algo4chris.algo4chrisdal.session;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Session
 *
 * @author chris
 * */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionEntity {
    /** 用户名 */
    protected String userName;
    /** 用户id */
    protected Long userId;
    /** email */
    protected String email;
    /** 用户頭貼 */
    protected String avatar;
    /** 用户ip */
    protected String ip;
}
