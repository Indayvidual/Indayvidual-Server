package com.indayvidual.server.domain.memo.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.indayvidual.server.domain.memo.dto.request.CreateMemoRequestDTO;
import com.indayvidual.server.domain.memo.dto.request.MemoSliceResponseDTO;
import com.indayvidual.server.domain.memo.dto.response.MemoDetailResponseDTO;
import com.indayvidual.server.domain.memo.service.command.MemoCommandService;
import com.indayvidual.server.domain.memo.service.query.MemoQueryService;
import com.indayvidual.server.global.api.response.ApiResponse;
import com.indayvidual.server.global.config.security.UserAuthentication;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

	private final MemoQueryService memoQueryService;
	private final MemoCommandService memoCommandService;

	@GetMapping
	@Operation(
		summary = "메모 목록 조회 (무한 스크롤)",
		description = "생성일자 또는 수정일자 기준으로 내림차순 정렬된 메모 목록 조회"
	)
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "메모 목록 조회 성공"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패"),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류")
	})
	public ApiResponse<MemoSliceResponseDTO> getMemos(
		@AuthenticationPrincipal UserAuthentication userAuthentication,

		@Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
		@RequestParam(required = false, defaultValue = "0") Integer page,

		@Parameter(description = "페이지 크기 (기본: 20, 최대: 100)", example = "20")
		@RequestParam(required = false) Integer size
	) {
		Long userId = Long.valueOf((String)userAuthentication.getPrincipal());
		return ApiResponse.onSuccess(memoQueryService.getMemosWithSlice(userId, page, size));

	}

	public ApiResponse<MemoDetailResponseDTO> getMemoDetail(
		@AuthenticationPrincipal UserAuthentication userAuthentication,

		@Parameter(description = "메모 ID", example = "1")
		@PathVariable(required = true) Long memoId
	) {
		Long userId = Long.valueOf((String)userAuthentication.getPrincipal());
		return ApiResponse.onSuccess(memoQueryService.getMemoDetail(userId, memoId));
	}

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
		@AuthenticationPrincipal UserAuthentication userAuthentication,
		@RequestBody CreateMemoRequestDTO request
	) {
		Long userId = Long.valueOf((String)userAuthentication.getPrincipal());
		return ApiResponse.onSuccess(memoCommandService.createMemo(userId, request));
	}
}
