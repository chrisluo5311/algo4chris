package com.algo4chris.algo4chrisweb.payLoad.response;

import com.algo4chris.algo4chrisweb.security.services.UserDetailsImpl;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoResponse {

    private Long id;
    private String username;
    private String email;
    private List<String> roles;

    public UserInfoResponse(UserDetailsImpl userDetails, List<String> roles){
        this.id       = userDetails.getId();
        this.username = userDetails.getUsername();
        this.email    = userDetails.getEmail();
        this.roles    = roles;
    }

}
