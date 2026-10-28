package com.example.meetingsummary.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // 1. 어떤 소셜 로그인인지 알아내기 (google, naver, kakao)
        String registrationId = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();

        // 2. OAuthAttributes 클래스를 사용해 providerId를 포함한 사용자 정보 파싱
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, "sub", oAuth2User.getAttributes());
        String providerId = attributes.getProviderId();

        // 3. providerId를 사용해 JWT 토큰 생성
        String token = jwtUtil.createToken(providerId);

        // 4. 프론트엔드로 토큰과 함께 리디렉션
        String targetUrl = "http://localhost:5173/login/success?token=" + token;
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}