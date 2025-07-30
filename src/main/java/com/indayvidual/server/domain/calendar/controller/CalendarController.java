package com.indayvidual.server.domain.calendar.controller;

import com.indayvidual.server.domain.calendar.dto.response.GetMonthlyCalendarResponseDto;
import com.indayvidual.server.domain.calendar.service.CalendarQueryService;
import com.indayvidual.server.global.api.code.status.SuccessStatus;
import com.indayvidual.server.global.api.response.ApiResponse;
import com.indayvidual.server.global.util.Utils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/calendar")
@RequiredArgsConstructor
@Tag(name = "Calendar", description = "캘린더 관련 API")
public class CalendarController {

	private final CalendarQueryService calendarQueryService;

	@GetMapping("/{year}/{month}")
	@Operation(summary = "월별 캘린더 조회",
			description = "지정된 연월의 캘린더를 조회합니다. 각 날짜에 포함된 일정들의 색상 코드를 반환합니다."
	)
	@ApiResponses(value = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "캘린더 조회 성공"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 연월 형식"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패")
	})
	public ApiResponse<List<GetMonthlyCalendarResponseDto>> getMonthlyCalendar(
			@Parameter(description = "조회할 연도", required = true, example = "2025")
			@PathVariable int year,
			@Parameter(description = "조회할 월", required = true, example = "07")
			@PathVariable int month) {

		Long userId = Utils.getUserId();
		List<GetMonthlyCalendarResponseDto> calendar = calendarQueryService.getMonthlyCalendar(year, month, userId);

		return ApiResponse.onSuccess(
				calendar,
				SuccessStatus.GET_CALENDAR_SUCCESS.getCode(),
				SuccessStatus.GET_CALENDAR_SUCCESS.getMessage()
		);
	}
}
