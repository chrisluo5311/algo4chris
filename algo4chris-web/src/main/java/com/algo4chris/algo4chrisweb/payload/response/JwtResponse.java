package com.algo4chris.algo4chrisweb.payload.response;

import lombok.*;

import java.util.List;

/**
 * 含jwtToken及refreshToken響應類
 *
 * @author chris
 * */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {

    private String token;
    @Builder.Default
    private String type = "Bearer ";
    private String refreshToken;
    private Long id;
    private String memberName;
    private String email;
    private List<String> roles;

}
