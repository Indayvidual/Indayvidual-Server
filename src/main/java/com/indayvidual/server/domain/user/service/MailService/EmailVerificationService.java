package com.indayvidual.server.domain.user.service.MailService;

import com.indayvidual.server.domain.user.entity.EmailVerification;
import com.indayvidual.server.domain.user.repository.EmailVerificationRepository;
import com.indayvidual.server.domain.user.repository.UserRepository;
import com.indayvidual.server.global.api.code.status.ErrorStatus;
import com.indayvidual.server.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Duration;

@Service
@RequiredArgsConstructor
@Transactional
public class EmailVerificationService {

    private static final Duration EXPIRE = Duration.ofMinutes(10);
    private static final int CODE_LENGTH = 4;

    private final EmailVerificationRepository repo;
    private final UserRepository userRepository;
    private final MailService mailService;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    @Transactional(readOnly = true)
    public boolean isEmailAvailable(String email) {
        return !userRepository.existsByEmail(email);
    }

    public void sendVerificationCode(String email) {
        // 새 코드 생성
        String code = generateCode();
        repo.save(EmailVerification.create(email, code, EXPIRE));

        // 이미 가입된 이메일은 차단
         if (userRepository.existsByEmail(email)) {
             throw new GeneralException(ErrorStatus.AUTH_EMAIL_DUPLICATED);
         }

        // 메일 전송
        String subject = "[Indayvidual] 이메일 인증번호 안내";
        String prettyCode = code.replaceAll("(\\d)", "$1 ").trim();

        String html = """
<!doctype html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Email Verification</title>
</head>
<body style="margin:0;background:#f6f7fb;padding:24px;font-family: -apple-system,BlinkMacSystemFont,Segoe UI,Roboto,Helvetica,Arial,Apple SD Gothic Neo,sans-serif;">
  <table role="presentation" width="100%%" cellspacing="0" cellpadding="0" style="border-collapse:collapse;">
    <tr>
      <td align="center">
        <table width="560" cellpadding="0" cellspacing="0" style="background:#ffffff;border-radius:12px;padding:32px;box-shadow:0 6px 16px rgba(0,0,0,.08);">
          <tr><td style="font-size:20px;font-weight:700;color:#111;">이메일 인증</td></tr>
          <tr><td style="padding-top:12px;line-height:1.7;color:#333;">
            안녕하세요, Indayvidual입니다.<br/>
            아래 인증번호를 입력해 인증을 완료해 주세요.
          </td></tr>
          <tr>
            <td align="center" style="padding:24px 0 8px;">
              <div style="display:inline-block;letter-spacing:6px;font-size:28px;font-weight:800;background:#eef4ff;color:#1a56db;padding:12px 20px;border-radius:10px;">
                %s
              </div>
            </td>
          </tr>
          <tr><td style="color:#555;font-size:14px;">유효시간: <strong>%d분</strong></td></tr>
          <tr><td style="color:#888;font-size:12px;padding-top:16px;">※ 인증번호는 타인과 공유하지 마세요.</td></tr>
          <tr><td style="color:#888;font-size:12px;padding-top:4px;">※ 본 메일은 발신 전용입니다. 문의: support@indayvidual.com</td></tr>
        </table>
      </td>
    </tr>
  </table>
</body>
</html>
""".formatted(prettyCode, EXPIRE.toMinutes());

        try {
            //mailService.sendSimpleMessage(email, subject, body);
            mailService.sendHtmlMessage(email, subject, html);
        } catch (Exception e) {
            throw new GeneralException(ErrorStatus.EMAIL_SEND_FAILED);
        }
    }

    // 사용자가 입력한 인증번호 검증
    public void verifyCode(String email, String code) {
        EmailVerification ev = repo.findByEmailAndCode(email, code)
                .orElseThrow(() -> new GeneralException(ErrorStatus.EMAIL_CODE_MISMATCH));

        if (ev.isExpired()) {
            throw new GeneralException(ErrorStatus.EMAIL_CODE_EXPIRED);
        }
        ev.markVerified(); // verified = true
    }

    //util: n자리 숫자 난수
    private String generateCode() {
        int bound = (int) Math.pow(10, CODE_LENGTH);
        int n = SECURE_RANDOM.nextInt(bound);
        return String.format("%0" + CODE_LENGTH + "d", n);
    }
}

