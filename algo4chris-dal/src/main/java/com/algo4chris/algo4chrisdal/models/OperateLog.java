/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.algo4chris.algo4chrisdal.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 *  @author chris
 */
@Entity
@Getter
@Setter
@Table(name = "operate_log")
@NoArgsConstructor
public class OperateLog implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 操作用户 */
    private String membername;

    /** 描述 */
    private String description;

    /** 方法名 */
    private String method;

    /** 参数 */
    private String params;

    /** 日志類型 */
    private String logType;

    /** 請求ip */
    private String requestIp;

    /** 地址 */
    private String address;

    /** 瀏覽器  */
    private String browser;

    /** 请求耗時 */
    private Long time;

    /** 異常详细  */
    private byte[] exceptionDetail;

    /** 創建日期 */
    @CreationTimestamp
    private Timestamp createTime;

    public OperateLog(String logType, Long time) {
        this.logType = logType;
        this.time = time;
    }
}
