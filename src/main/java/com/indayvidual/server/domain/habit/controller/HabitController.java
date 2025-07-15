package com.indayvidual.server.domain.habit.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.indayvidual.server.domain.habit.dto.CreateHabitRequestDTO;
import com.indayvidual.server.domain.habit.service.command.HabitCommandService;
import com.indayvidual.server.domain.habit.service.query.HabitQueryService;
import com.indayvidual.server.global.api.response.ApiResponse;
import com.indayvidual.server.global.config.security.UserAuthentication;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/custom/habits")
@RequiredArgsConstructor
@Tag(name = "Habit", description = "습관 관리 API")
public class HabitController {

	private HabitQueryService habitQueryService;
	private HabitCommandService habitCommandService;

	@PostMapping
	public ApiResponse<Void> createHabit(
		@AuthenticationPrincipal UserAuthentication userAuthentication,

		@RequestBody CreateHabitRequestDTO request

	) {
		Long userId = Long.valueOf((String)userAuthentication.getPrincipal());
		return ApiResponse.onSuccess(habitCommandService.createHabit(userId, request));
	}

}
