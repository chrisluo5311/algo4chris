package com.algo4chris.algo4chrisdal.models;

import com.algo4chris.algo4chrisdal.models.enums.MemberStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Component
@Entity
@Table(name = "member",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "membername"),
                @UniqueConstraint(columnNames = "email")
        })
public class Member {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 20)
    @Column(name = "membername")
    private String memberName;

    @NotNull
    @Size(max = 50)
    @Email
    @Column(name = "email")
    private String email;

    @JsonIgnore
    @Size(max = 120)
    @Column(name = "password")
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "member_roles",
            joinColumns = @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @Column(name = "status")
    @Builder.Default
    private Integer status = MemberStatus.ENABLE.getCode();

    @Column(name = "provider")
    private Provider provider;

    @Column(name = "ip")
    private String ip;

    @JsonIgnore
    @Column(name = "create_time")
    private Date createTime;

    @JsonIgnore
    @Column(name = "update_time")
    private Date updateTime;


}
