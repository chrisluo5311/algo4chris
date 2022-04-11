package com.algo4chris.algo4chrisdal.repository;

import com.algo4chris.algo4chrisdal.models.RefreshToken;
import com.algo4chris.algo4chrisdal.models.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    @Override
    Optional<RefreshToken> findById(Long id);

    Optional<RefreshToken> findByToken(String token);

    int deleteByMember(Member member);
}