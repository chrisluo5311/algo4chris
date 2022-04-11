package com.algo4chris.algo4chrisweb.security.services;

import com.algo4chris.algo4chrisdal.models.Member;
import com.algo4chris.algo4chrisdal.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = userRepository.findByMemberName(username).orElseThrow(()-> new UsernameNotFoundException("查無用戶:"+ username));
        return UserDetailsImpl.build(member);
    }
}
