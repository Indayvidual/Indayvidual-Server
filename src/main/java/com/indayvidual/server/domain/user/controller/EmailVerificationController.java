package com.indayvidual.server.domain.user.controller;

import com.indayvidual.server.domain.user.dto.request.EmailRequestDTO;
import com.indayvidual.server.domain.user.dto.request.EmailVerifyRequestDTO;
import com.indayvidual.server.domain.user.service.MailService.EmailVerificationService;
import com.indayvidual.server.global.api.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Email API",
        description = "이메일 중복 확인, 인증 코드 발송 및 검증 기능을 제공"
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/email")
@Validated
public class EmailVerificationController {

    private final EmailVerificationService service;

    @Operation(
            summary = "이메일 중복 확인",
            description = "입력한 이메일이 회원가입에 사용 가능한지 확인합니다."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "사용 가능 여부 반환"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 이메일 형식")
    })
    @GetMapping("/check")
    public ResponseEntity<ApiResponse<Boolean>> checkEmail(
            @Parameter(description = "확인할 이메일", example = "user@example.com")
            @RequestParam @Email(message = "올바른 이메일 형식이 아닙니다.") @NotBlank(message = "이메일은 필수입니다.")
            String email) {
        boolean isAvailable = service.isEmailAvailable(email);
        return ResponseEntity.ok(ApiResponse.onSuccess(isAvailable));
    }

    @Operation(
            summary = "인증 코드 발송",
            description = "이메일로 4자리 인증 코드를 발송합니다. 과도한 요청은 제한됩니다."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "발송 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "429", description = "재발송 쿨다운"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "502", description = "메일 전송 실패")
    })
    @PostMapping("/send")
    public ResponseEntity<ApiResponse<String>> send(@RequestBody @Valid EmailRequestDTO dto) {
        service.sendVerificationCode(dto.getEmail());
        return ResponseEntity.ok(ApiResponse.onSuccess("인증번호가 전송되었습니다."));
    }

    @Operation(
            summary = "인증 코드 검증",
            description = "이메일로 발송된 4자리 인증 코드를 검증합니다."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "인증 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "코드 불일치"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "410", description = "코드 만료")
    })
    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<String>> verify(@RequestBody @Valid EmailVerifyRequestDTO dto) {
        service.verifyCode(dto.getEmail(), dto.getCode());
        return ResponseEntity.ok(ApiResponse.onSuccess("이메일 인증 성공"));
    }
}