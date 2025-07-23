package com.indayvidual.server.domain.event.controller;

import com.indayvidual.server.domain.calendar.dto.response.GetDayEventResponseDto;
import com.indayvidual.server.domain.event.converter.EventConverter;
import com.indayvidual.server.domain.event.dto.request.CreateEventRequestDto;
import com.indayvidual.server.domain.event.dto.request.UpdateEventRequestDto;
import com.indayvidual.server.domain.event.dto.response.CreateEventResponseDto;
import com.indayvidual.server.domain.event.dto.response.UpdateEventResponseDto;
import com.indayvidual.server.domain.event.entity.Event;
import com.indayvidual.server.domain.event.exception.EventException;
import com.indayvidual.server.domain.event.service.EventCommandService;
import com.indayvidual.server.domain.event.service.EventQueryService;
import com.indayvidual.server.global.api.code.status.ErrorStatus;
import com.indayvidual.server.global.api.code.status.SuccessStatus;
import com.indayvidual.server.global.api.response.ApiResponse;
import com.indayvidual.server.global.util.Utils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
@Tag(name = "Event", description = "일정 관리 API")
public class EventController {

    private final EventCommandService eventCommandService;
    private final EventQueryService eventQueryService;
    private final EventConverter eventConverter;

    @PostMapping
    @Operation(summary = "일정 생성", description = "새로운 일정을 생성합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "일정 생성 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패")
    })
    public ApiResponse<CreateEventResponseDto> createEvent(
            @Valid @RequestBody CreateEventRequestDto request) {

        Long userId = Utils.getUserId();
        Event createdEvent = eventCommandService.createEvent(request, userId);
        CreateEventResponseDto response = eventConverter.toCreateResponse(createdEvent);

        return ApiResponse.onSuccess(
                response,
                SuccessStatus.CREATE_EVENT_SUCCESS.getCode(),
                SuccessStatus.CREATE_EVENT_SUCCESS.getMessage()
        );
    }

    @PatchMapping("/{eventId}")
    @Operation(summary = "일정 수정", description = "기존 일정을 수정합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "일정 수정 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "일정을 찾을 수 없음")
    })
    public ApiResponse<UpdateEventResponseDto> updateEvent(
            @Parameter(description = "일정 ID", required = true) @PathVariable Long eventId,
            @Valid @RequestBody UpdateEventRequestDto request) {

        Long userId = Utils.getUserId();
        Event updatedEvent = eventCommandService.updateEvent(eventId, request, userId);
        UpdateEventResponseDto response = eventConverter.toUpdateResponse(updatedEvent);

        return ApiResponse.onSuccess(
                response,
                SuccessStatus.UPDATE_EVENT_SUCCESS.getCode(),
                SuccessStatus.UPDATE_EVENT_SUCCESS.getMessage()
        );
    }

    @DeleteMapping("/{eventId}")
    @Operation(summary = "일정 삭제", description = "일정을 삭제합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "일정 삭제 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "일정을 찾을 수 없음")
    })
    public ApiResponse<Void> deleteEvent(
            @Parameter(description = "일정 ID", required = true) @PathVariable Long eventId) {

        Long userId = Utils.getUserId();
        eventCommandService.deleteEvent(eventId, userId);

        return ApiResponse.onSuccess(
                null,
                SuccessStatus.DELETE_EVENT_SUCCESS.getCode(),
                SuccessStatus.DELETE_EVENT_SUCCESS.getMessage()
        );
    }

    @GetMapping("/{date}")
    @Operation(summary = "특정 날짜 일정 조회", description = "특정 날짜의 모든 일정을 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "일정 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 날짜 형식"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패")
    })
    public ApiResponse<List<GetDayEventResponseDto>> getDayEvents(
            @Parameter(description = "날짜 (yyyy-MM-dd 형식)", required = true, example = "2025-07-22")
            @PathVariable String date) {

        Long userId = Utils.getUserId();
        LocalDate eventDate;

        try {
            eventDate = LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            throw new EventException(ErrorStatus.EVENT_INVALID_DATE_FORMAT);
        }

        List<GetDayEventResponseDto> events = eventQueryService.getDayEvents(eventDate, userId);

        return ApiResponse.onSuccess(
                events,
                SuccessStatus.GET_DAY_EVENTS_SUCCESS.getCode(),
                SuccessStatus.GET_DAY_EVENTS_SUCCESS.getMessage());
    }
}