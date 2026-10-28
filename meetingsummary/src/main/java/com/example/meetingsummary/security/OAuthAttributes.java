package com.example.meetingsummary.security;

import com.example.meetingsummary.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;
    private String providerId;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String picture, String providerId) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.providerId = providerId;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        if ("naver".equals(registrationId)) {
            return ofNaver("id", attributes);
        }
        if ("kakao".equals(registrationId)) {
            return ofKakao("id", attributes);
        }
        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .providerId("google_" + attributes.get("sub"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    // 👇 이 부분이 중요합니다.
    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        // Naver 응답의 최상위 레벨에 "response" 키가 있는지 확인
        Map<String, Object> response = attributes;
        if (attributes.containsKey("response")) {
            response = (Map<String, Object>) attributes.get("response");
        }

        return OAuthAttributes.builder()
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .picture((String) response.get("profile_image"))
                .providerId("naver_" + response.get("id"))
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    // 👇 이 부분도 안정성을 위해 수정되었습니다.
    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> kakaoProfile = (kakaoAccount != null) ? (Map<String, Object>) kakaoAccount.get("profile") : null;

        return OAuthAttributes.builder()
                .name(kakaoProfile != null ? (String) kakaoProfile.get("nickname") : null)
                .email(kakaoAccount != null ? (String) kakaoAccount.get("email") : null)
                .picture(kakaoProfile != null ? (String) kakaoProfile.get("profile_image_url") : null)
                .providerId("kakao_" + attributes.get("id"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public User toEntity() {
        return User.builder()
                .providerId(providerId)
                .name(name)
                .email(email)
                .picture(picture)
                .build();
    }
}