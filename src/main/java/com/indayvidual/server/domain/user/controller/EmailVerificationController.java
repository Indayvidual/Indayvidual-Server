package com.indayvidual.server.domain.user.controller;

import com.indayvidual.server.domain.user.dto.request.EmailRequestDTO;
import com.indayvidual.server.domain.user.dto.request.EmailVerifyRequestDTO;
import com.indayvidual.server.domain.user.service.MailService.EmailVerificationService;
import com.indayvidual.server.global.api.response.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "이메일 인증 API",
        description = "이메일 중복 확인, 인증 코드 발송 및 검증 기능을 제공."
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/email")
public class EmailVerificationController {

    private final EmailVerificationService service;

    @PostMapping("/send")
    public ResponseEntity<ApiResponse<String>> send(@RequestBody EmailRequestDTO dto) {
        service.sendVerificationCode(dto.getEmail());
        return ResponseEntity.ok(ApiResponse.onSuccess("인증번호가 전송되었습니다."));
    }

    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<String>> verify(@RequestBody EmailVerifyRequestDTO dto) {
        service.verifyCode(dto.getEmail(), dto.getCode());
        return ResponseEntity.ok(ApiResponse.onSuccess("이메일 인증 성공"));
    }
}