package com.indayvidual.server.domain.user.repository;

import com.indayvidual.server.domain.user.entity.User;
import com.indayvidual.server.domain.user.entity.UserProvider;
import com.indayvidual.server.domain.user.entity.enums.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserProviderRepository extends JpaRepository<UserProvider, Long> {

    // Provider와 providerId로 UserProvider 조회
    Optional<UserProvider> findByProviderAndProviderId(Provider provider, String providerId);

    // 특정 사용자의 활성화된 Provider 목록 조회
    List<UserProvider> findByUserAndIsActive(User user, Boolean isActive);

    // 특정 사용자의 특정 Provider 조회
    Optional<UserProvider> findByUserAndProvider(User user, Provider provider);

    // Provider와 providerId 존재 여부 확인
    boolean existsByProviderAndProviderId(Provider provider, String providerId);

    // 특정 사용자의 모든 Provider 조회
    List<UserProvider> findByUser(User user);

    // 특정 Provider의 모든 사용자 조회 (관리자용)
    List<UserProvider> findByProvider(Provider provider);

    // 특정 사용자의 활성화된 Provider 개수
    long countByUserAndIsActive(User user, Boolean isActive);
}
