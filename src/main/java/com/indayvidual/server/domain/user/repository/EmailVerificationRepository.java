package com.indayvidual.server.domain.user.repository;

import com.indayvidual.server.domain.user.entity.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface EmailVerificationRepository
        extends JpaRepository<EmailVerification, Long> {

    /** 최신 발송건 조회 (재전송 제한 등 필요 시) */
    Optional<EmailVerification> findTopByEmailOrderByIdDesc(String email);

    /** 검증용 */
    Optional<EmailVerification> findByEmailAndCode(String email, String code);

    /** 만료 코드 일괄 삭제 */
    void deleteByExpiredAtBefore(LocalDateTime dateTime);
}
