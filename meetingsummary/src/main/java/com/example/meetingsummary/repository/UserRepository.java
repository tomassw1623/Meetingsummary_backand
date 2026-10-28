// com/example/meetingsummary/repository/UserRepository.java

package com.example.meetingsummary.repository;

import com.example.meetingsummary.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // 이메일 대신 providerId로 사용자를 찾는 메소드
    Optional<User> findByProviderId(String providerId);
}