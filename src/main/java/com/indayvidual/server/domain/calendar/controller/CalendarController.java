package com.indayvidual.server.domain.calendar.controller;

import java.time.LocalDate;
import java.util.List;

import com.indayvidual.server.domain.calendar.service.CalendarQueryService;
import com.indayvidual.server.global.util.Utils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.indayvidual.server.domain.event.converter.EventConverter;
import com.indayvidual.server.domain.calendar.dto.response.GetDayEventResponseDto;
import com.indayvidual.server.domain.calendar.dto.response.GetMonthlyCalendarResponseDto;
import com.indayvidual.server.domain.event.service.EventCommandService;
import com.indayvidual.server.domain.event.service.EventQueryService;
import com.indayvidual.server.global.api.code.status.ErrorStatus;
import com.indayvidual.server.global.api.code.status.SuccessStatus;
import com.indayvidual.server.global.api.response.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/calendar")
@RequiredArgsConstructor
@Slf4j
public class CalendarController {

	private final CalendarQueryService calendarQueryService;

	@GetMapping("/{year}/{month}")
	public ResponseEntity<ApiResponse<List<GetMonthlyCalendarResponseDto>>> getMonthlyCalendar(
		@PathVariable int year,
		@PathVariable int month) {

		Long userId = Utils.getUserId();; // TODO: JWT에서 사용자 ID 추출

		try {
			List<GetMonthlyCalendarResponseDto> calendar = calendarQueryService.getMonthlyCalendar(year, month, userId);

			return ResponseEntity.ok(ApiResponse.onSuccess(
				calendar,
				SuccessStatus.GET_CALENDAR_SUCCESS.getCode(),
				SuccessStatus.GET_CALENDAR_SUCCESS.getMessage())
			);
		} catch (Exception e) {
			log.error("월별 캘린더 조회 실패", e);

			ApiResponse<List<GetMonthlyCalendarResponseDto>> apiResponse = ApiResponse.onFailure(
				ErrorStatus.CALENDAR_FETCH_FAILED.getCode(),
				ErrorStatus.CALENDAR_FETCH_FAILED.getMessage() + ": " + e.getMessage(),
				null
			);
			return ResponseEntity.badRequest().body(apiResponse);
		}
	}
}
