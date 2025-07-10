package com.indayvidual.server.domain.memo.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.indayvidual.server.domain.memo.dto.request.CreateMemoRequestDTO;
import com.indayvidual.server.domain.memo.service.command.MemoCommandService;
import com.indayvidual.server.global.api.response.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/custom/memos")
@RequiredArgsConstructor
@Tag(name = "Memo", description = "메모 관리 API")
public class MemoController {

	private final MemoCommandService memoCommandService;

	@PostMapping
	@Operation(
		summary = "메모 생성",
		description = "새로운 메모를 생성합니다."
	)
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "200",
			description = "메모 생성 성공",
			content = @Content(schema = @Schema(implementation = ApiResponse.class))
		),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "400",
			description = "잘못된 요청 데이터"
		),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "500",
			description = "서버 내부 오류"
		)
	})
	public ApiResponse<Void> createMemo(
		// TODO: @AuthenticationPrincipal를 추가해서 등록자가 누군지 구분하도록 함
		@RequestBody CreateMemoRequestDTO request
	) {
		return ApiResponse.onSuccess(memoCommandService.createMemo(request));
	}
}
