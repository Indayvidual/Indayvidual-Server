package com.indayvidual.server.domain.memo.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.indayvidual.server.domain.memo.dto.request.CreateMemoRequestDTO;
import com.indayvidual.server.domain.memo.dto.response.MemoDetailResponseDTO;
import com.indayvidual.server.domain.memo.dto.response.MemoSliceResponseDTO;
import com.indayvidual.server.domain.memo.service.command.MemoCommandService;
import com.indayvidual.server.domain.memo.service.query.MemoQueryService;
import com.indayvidual.server.global.api.response.ApiResponse;
import com.indayvidual.server.global.util.Utils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
		description = "생성일자 또는 수정일자 기준으로 내림차순 정렬된 메모 목록을 페이지네이션으로 조회합니다."
	)
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "200",
			description = "메모 목록 조회 성공",
			content = @Content(schema = @Schema(implementation = MemoSliceResponseDTO.class))
		),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "400",
			description = "잘못된 요청 파라미터"
		),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "401",
			description = "인증 실패"
		),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "500",
			description = "서버 내부 오류"
		)
	})
	public ApiResponse<MemoSliceResponseDTO> getMemos(
		@Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
		@RequestParam(required = false, defaultValue = "0") Integer page,

		@Parameter(description = "페이지 크기 (기본: 20, 최대: 100)", example = "20")
		@RequestParam(required = false, defaultValue = "20") Integer size
	) {
		Long userId = Utils.getUserId();
		return ApiResponse.onSuccess(memoQueryService.getMemosWithSlice(userId, page, size));
	}

	@GetMapping("/{memoId}")
	@Operation(
		summary = "메모 상세 조회",
		description = "특정 메모의 상세 정보를 조회합니다. 본인이 작성한 메모만 조회 가능합니다."
	)
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "200",
			description = "메모 상세 조회 성공",
			content = @Content(schema = @Schema(implementation = MemoDetailResponseDTO.class))
		),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "401",
			description = "인증 실패"
		),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "403",
			description = "권한 없음 (다른 사용자의 메모에 접근 시도)"
		),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "404",
			description = "메모를 찾을 수 없음"
		),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "500",
			description = "서버 내부 오류"
		)
	})
	public ApiResponse<MemoDetailResponseDTO> getMemoDetail(
		@Parameter(description = "메모 ID", required = true, example = "1")
		@PathVariable Long memoId
	) {
		Long userId = Utils.getUserId();
		return ApiResponse.onSuccess(memoQueryService.getMemoDetail(userId, memoId));
	}

	@DeleteMapping("/{memoId}")
	@Operation(
		summary = "메모 삭제",
		description = "특정 메모를 삭제합니다. 본인이 작성한 메모만 삭제 가능합니다."
	)
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "200",
			description = "메모 삭제 성공",
			content = @Content(schema = @Schema(implementation = Void.class))
		),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "401",
			description = "인증 실패"
		),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "403",
			description = "권한 없음 (다른 사용자의 메모 삭제 시도)"
		),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "404",
			description = "메모를 찾을 수 없음"
		),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "500",
			description = "서버 내부 오류"
		)
	})
	public ApiResponse<Void> deleteMemo(
		@Parameter(description = "메모 ID", required = true, example = "1")
		@PathVariable Long memoId
	) {
		Long userId = Utils.getUserId();
		return ApiResponse.onSuccess(memoCommandService.deleteMemo(userId, memoId));
	}

	@PostMapping
	@Operation(
		summary = "메모 생성",
		description = "새로운 메모를 생성합니다. 제목과 내용을 입력받아 메모를 저장합니다."
	)
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "200",
			description = "메모 생성 성공",
			content = @Content(schema = @Schema(implementation = Void.class))
		),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "400",
			description = "잘못된 요청 데이터 (필수 필드 누락, 유효성 검증 실패 등)"
		),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "401",
			description = "인증 실패"
		),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "500",
			description = "서버 내부 오류"
		)
	})
	public ApiResponse<MemoDetailResponseDTO> createMemo(
		@RequestBody @Valid CreateMemoRequestDTO request
	) {
		Long userId = Utils.getUserId();
		return ApiResponse.onSuccess(memoCommandService.createMemo(userId, request));
	}
}