package com.indayvidual.server.domain.habit.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.indayvidual.server.domain.habit.dto.request.CreateHabitRequestDTO;
import com.indayvidual.server.domain.habit.dto.request.ToggleCheckRequestDTO;
import com.indayvidual.server.domain.habit.dto.request.UpdateHabitRequestDTO;
import com.indayvidual.server.domain.habit.dto.response.HabitResponseDTO;
import com.indayvidual.server.domain.habit.dto.response.HabitSliceResponseDTO;
import com.indayvidual.server.domain.habit.service.command.HabitCommandService;
import com.indayvidual.server.domain.habit.service.query.HabitQueryService;
import com.indayvidual.server.global.api.response.ApiResponse;
import com.indayvidual.server.global.util.Utils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PastOrPresent;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/custom/habits")
@RequiredArgsConstructor
@Validated
@Tag(name = "Habit", description = "습관 관리 API")
public class HabitController {

	private final HabitQueryService habitQueryService;
	private final HabitCommandService habitCommandService;

	@GetMapping
	@Operation(
		summary = "습관 목록 조회 (무한 스크롤)",
		description = "생성일자 기준으로 내림차순 정렬된 습관 목록을 페이지네이션으로 조회합니다. 각 습관의 오늘 체크 여부도 함께 반환됩니다."
	)
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "200",
			description = "습관 목록 조회 성공",
			content = @Content(schema = @Schema(implementation = HabitSliceResponseDTO.class))
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
	public ApiResponse<HabitSliceResponseDTO> getHabits(
		@Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
		@RequestParam(required = false, defaultValue = "0") Integer page,

		@Parameter(description = "페이지 크기 (기본: 20, 최대: 100)", example = "20")
		@RequestParam(required = false, defaultValue = "20") Integer size
	) {
		Long userId = Utils.getUserId();
		return ApiResponse.onSuccess(habitQueryService.getHabits(userId, page, size));
	}

	@PostMapping
	@Operation(
		summary = "습관 생성",
		description = "새로운 습관을 생성합니다. 습관 이름과 색상 코드를 입력받아 저장합니다."
	)
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "200",
			description = "습관 생성 성공",
			content = @Content(schema = @Schema(implementation = HabitResponseDTO.class))
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
			responseCode = "409",
			description = "동일한 이름의 습관이 이미 존재함"
		),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "500",
			description = "서버 내부 오류"
		)
	})
	public ApiResponse<HabitResponseDTO> createHabit(
		@Valid @RequestBody CreateHabitRequestDTO request
	) {
		Long userId = Utils.getUserId();
		return ApiResponse.onSuccess(habitCommandService.createHabit(userId, request));
	}

	@PatchMapping("/{habitId}")
	@Operation(
		summary = "습관 수정",
		description = "기존 습관의 정보를 수정합니다. 본인이 생성한 습관만 수정 가능합니다."
	)
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "200",
			description = "습관 수정 성공",
			content = @Content(schema = @Schema(implementation = HabitResponseDTO.class))
		),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "400",
			description = "잘못된 요청 데이터 (유효성 검증 실패 등)"
		),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "401",
			description = "인증 실패"
		),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "403",
			description = "권한 없음 (다른 사용자의 습관 수정 시도)"
		),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "404",
			description = "습관을 찾을 수 없음"
		),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "409",
			description = "변경하려는 이름의 습관이 이미 존재함"
		),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "500",
			description = "서버 내부 오류"
		)
	})
	public ApiResponse<HabitResponseDTO> updateHabit(

		@Parameter(description = "습관 ID", required = true, example = "1")
		@PathVariable Long habitId,

		@io.swagger.v3.oas.annotations.parameters.RequestBody(
			description = "습관 수정 요청 데이터",
			required = true,
			content = @Content(schema = @Schema(implementation = UpdateHabitRequestDTO.class))
		)
		@RequestBody UpdateHabitRequestDTO request
	) {
		Long userId = Utils.getUserId();
		return ApiResponse.onSuccess(habitCommandService.updateHabit(userId, habitId, request));
	}

	@DeleteMapping("/{habitId}")
	@Operation(
		summary = "습관 삭제",
		description = "특정 습관을 삭제합니다. 본인이 생성한 습관만 삭제 가능하며, 관련된 모든 체크 기록도 함께 삭제됩니다."
	)
	@ApiResponses(value = {
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "200",
			description = "습관 삭제 성공",
			content = @Content(schema = @Schema(implementation = Void.class))
		),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "401",
			description = "인증 실패"
		),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "403",
			description = "권한 없음 (다른 사용자의 습관 삭제 시도)"
		),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "404",
			description = "습관을 찾을 수 없음"
		),
		@io.swagger.v3.oas.annotations.responses.ApiResponse(
			responseCode = "500",
			description = "서버 내부 오류"
		)
	})
	public ApiResponse<Void> deleteHabit(
		@Parameter(description = "습관 ID", required = true, example = "1")
		@PathVariable Long habitId
	) {
		Long userId = Utils.getUserId();
		return ApiResponse.onSuccess(habitCommandService.deleteHabit(userId, habitId));
	}

	@PatchMapping("/{habitId}/check")
	public ApiResponse<HabitResponseDTO> updateHabitCheck(
		@PathVariable Long habitId,

		@Valid @RequestBody ToggleCheckRequestDTO request
	) {
		Long userId = Utils.getUserId();
		return ApiResponse.onSuccess(habitCommandService.updateHabitCheck(userId, habitId, request));
	}

	@GetMapping("/checks/daily")
	public ApiResponse<List<HabitResponseDTO>> getDailyHabitCheckedState(
		@Parameter(description = "습관 날짜", required = true, example = "2025-01-01")
		@RequestParam @PastOrPresent LocalDate date
	) {
		Long userId = Utils.getUserId();
		return ApiResponse.onSuccess(habitQueryService.getDailyHabitCheckedState(userId, date));
	}
}