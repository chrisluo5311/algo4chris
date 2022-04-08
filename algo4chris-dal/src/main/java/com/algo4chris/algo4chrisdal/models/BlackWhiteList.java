package com.algo4chris.algo4chrisdal.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Component
@Entity
@Table(name = "blackwhitelist")
public class BlackWhiteList {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** ip */
    @Column(name="ip")
    private String ip;

    /** 1:白名單 -1:黑名單 */
    @Column(name="type")
    private int type;

}
