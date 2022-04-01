package com.algo4chris.algo4chrisweb.security.services;

import com.algo4chris.algo4chriscommon.exception.responsecode.MgrResponseCode;
import com.algo4chris.algo4chriscommon.exception.tokenrefresh.TokenRefreshException;
import com.algo4chris.algo4chrisdal.models.RefreshToken;
import com.algo4chris.algo4chrisdal.models.User;
import com.algo4chris.algo4chrisdal.repository.RefreshTokenRepository;
import com.algo4chris.algo4chrisdal.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class RefreshTokenService {

    /** 24小時 */
    @Value("${algo4chris.app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    @Resource
    RefreshTokenRepository refreshTokenRepository;

    @Resource
    UserRepository userRepository;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    /**
     * 產生 refresh token 並存至 db
     *
     * @param userId 使用者id
     * */
    public RefreshToken createRefreshToken(Long userId) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(userRepository.findById(userId).get());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(MgrResponseCode.REFRESH_TOKEN_EXPIRED,token.getToken());
        }
        return token;
    }

    @Transactional(rollbackFor = Exception.class)
    public int deleteByUserId(User user) {
        return refreshTokenRepository.deleteByUser(user);
    }
}
