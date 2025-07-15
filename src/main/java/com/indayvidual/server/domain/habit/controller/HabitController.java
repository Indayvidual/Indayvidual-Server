package com.indayvidual.server.domain.habit.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.indayvidual.server.domain.habit.dto.request.CreateHabitRequestDTO;
import com.indayvidual.server.domain.habit.dto.response.HabitResponseDTO;
import com.indayvidual.server.domain.habit.dto.response.HabitSliceResponseDTO;
import com.indayvidual.server.domain.habit.service.command.HabitCommandService;
import com.indayvidual.server.domain.habit.service.query.HabitQueryService;
import com.indayvidual.server.global.api.response.ApiResponse;
import com.indayvidual.server.global.config.security.UserAuthentication;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/custom/habits")
@RequiredArgsConstructor
@Tag(name = "Habit", description = "습관 관리 API")
public class HabitController {

	private HabitQueryService habitQueryService;
	private HabitCommandService habitCommandService;

	@GetMapping
	public ApiResponse<HabitSliceResponseDTO> getHabits(
		@AuthenticationPrincipal UserAuthentication userAuthentication,

		@Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
		@RequestParam(required = false, defaultValue = "0") Integer page,

		@Parameter(description = "페이지 크기 (기본: 20, 최대: 100)", example = "20")
		@RequestParam(required = false, defaultValue = "20") Integer size
	) {
		Long userId = Long.valueOf((String)userAuthentication.getPrincipal());
		return ApiResponse.onSuccess(habitQueryService.getHabits(userId, page, size));
	}

	@PostMapping
	public ApiResponse<HabitResponseDTO> createHabit(
		@AuthenticationPrincipal UserAuthentication userAuthentication,

		@RequestBody CreateHabitRequestDTO request

	) {
		Long userId = Long.valueOf((String)userAuthentication.getPrincipal());
		return ApiResponse.onSuccess(habitCommandService.createHabit(userId, request));
	}

}
