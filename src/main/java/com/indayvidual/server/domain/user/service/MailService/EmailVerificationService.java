package com.indayvidual.server.domain.user.service.MailService;

import com.indayvidual.server.domain.user.entity.EmailVerification;
import com.indayvidual.server.domain.user.repository.EmailVerificationRepository;
import com.indayvidual.server.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Transactional
public class EmailVerificationService {

    private static final Duration EXPIRE = Duration.ofMinutes(10);
    private static final int CODE_LENGTH = 4;

    private final EmailVerificationRepository repo;
    private final UserRepository userRepository;
    private final MailService mailService;

    @Transactional(readOnly = true)
    public boolean isEmailAvailable(String email) {
        return !userRepository.existsByEmail(email);
    }

    public void sendVerificationCode(String email) {
        // 새 코드 생성
        String code = generateCode();
        repo.save(EmailVerification.create(email, code, EXPIRE));

        // 메일 전송
        String subject = "[Indayvidual] 이메일 인증번호 안내";
        String body = String.format("""
                안녕하세요!
                
                요청하신 인증번호는 %s 입니다.
                %d분 내에 입력해 주세요.
                
                감사합니다.
                """, code, EXPIRE.toMinutes());

        mailService.sendSimpleMessage(email, subject, body);
    }

    // 사용자가 입력한 인증번호 검증
    public void verifyCode(String email, String code) {
        EmailVerification ev = repo.findByEmailAndCode(email, code)
                .orElseThrow(() -> new IllegalArgumentException("인증번호가 일치하지 않습니다."));

        if (ev.isExpired()) {
            throw new IllegalStateException("인증번호가 만료되었습니다.");
        }
        ev.markVerified(); // verified = true
    }

    //util: n자리 숫자 난수
    private String generateCode() {
        int bound = (int) Math.pow(10, EmailVerificationService.CODE_LENGTH);
        return String.format("%0" + EmailVerificationService.CODE_LENGTH + "d", ThreadLocalRandom.current().nextInt(bound));
    }
}

