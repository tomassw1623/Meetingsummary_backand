package com.example.meetingsummary.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    private final SecretKey key;
    private final long accessTokenValidityInMs;

    public JwtUtil(@Value("${jwt.secret}") String secret, @Value("${jwt.access-token-validity-in-seconds}") long validityInSeconds) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenValidityInMs = validityInSeconds * 1000;
    }

    // 토큰 생성
    public String createToken(String providerId) { // 파라미터는 providerId
        Date now = new Date();
        Date validity = new Date(now.getTime() + this.accessTokenValidityInMs);
        return Jwts.builder()
                .subject(providerId) // [수정] email 대신 providerId를 subject로 설정
                .issuedAt(now)
                .expiration(validity)
                .signWith(key)
                .compact();
    }

    // Request Header에서 토큰 정보 추출
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return !claims.getPayload().getExpiration().before(new Date());
        } catch (Exception e) {
            log.info("유효하지 않은 JWT 토큰입니다.");
            return false;
        }
    }

    // [수정] 토큰에서 사용자 정보(providerId) 추출
    public String getProviderIdFromToken(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();
    }
}