package com.algo4chris.algo4chrisweb.payload.request;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

/**
 * 自訂義OAuth2User類
 * 獲取名字與Email
 *
 * @author chris
 * */
public class AlgoOAuth2User implements OAuth2User {

    private OAuth2User oauth2User;

    public AlgoOAuth2User(OAuth2User oauth2User) {
        this.oauth2User = oauth2User;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oauth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oauth2User.getAuthorities();
    }

    @Override
    public String getName() {
        return oauth2User.getAttribute("name");
    }

    public String getEmail() {
        return oauth2User.getAttribute("email");
    }

}
