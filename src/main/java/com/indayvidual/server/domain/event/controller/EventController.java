package com.indayvidual.server.domain.event.controller;

import com.indayvidual.server.domain.calendar.dto.response.GetDayEventResponseDto;
import com.indayvidual.server.domain.event.converter.EventConverter;
import com.indayvidual.server.domain.event.dto.request.CreateEventRequestDto;
import com.indayvidual.server.domain.event.dto.request.UpdateEventRequestDto;
import com.indayvidual.server.domain.event.dto.response.CreateEventResponseDto;
import com.indayvidual.server.domain.event.dto.response.UpdateEventResponseDto;
import com.indayvidual.server.domain.event.entity.Event;
import com.indayvidual.server.domain.event.service.EventCommandService;
import com.indayvidual.server.domain.event.service.EventQueryService;
import com.indayvidual.server.global.api.code.status.ErrorStatus;
import com.indayvidual.server.global.api.code.status.SuccessStatus;
import com.indayvidual.server.global.api.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
@Slf4j
public class EventController {

    private final EventCommandService eventCommandService;
    private final EventQueryService eventQueryService;
    private final EventConverter eventConverter;

    @PostMapping
    public ResponseEntity<ApiResponse<CreateEventResponseDto>> createEvent(
            @Valid @RequestBody CreateEventRequestDto request) {

        Long userId = 1L; // TODO: JWT에서 사용자 ID 추출

        try {
            Event createdEvent = eventCommandService.createEvent(request, userId);
            CreateEventResponseDto response = eventConverter.toCreateResponse(createdEvent);

            return ResponseEntity.ok(
                    ApiResponse.onSuccess(response,
                            SuccessStatus.CREATE_EVENT_SUCCESS.getCode(),
                            SuccessStatus.CREATE_EVENT_SUCCESS.getMessage())
            );
        } catch (Exception e) {
            log.error("일정 등록 실패", e);
            return ResponseEntity.badRequest().body(
                    ApiResponse.onFailure(
                            ErrorStatus.EVENT_CREATE_FAILED.getCode(),
                            ErrorStatus.EVENT_CREATE_FAILED.getMessage() + ": " + e.getMessage(),
                            null)
            );
        }
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<ApiResponse<UpdateEventResponseDto>> updateEvent(
            @PathVariable Long eventId,
            @Valid @RequestBody UpdateEventRequestDto request) {

        Long userId = 1L; // TODO: JWT에서 사용자 ID 추출

        try {
            Event updatedEvent = eventCommandService.updateEvent(eventId, request, userId);
            UpdateEventResponseDto response = eventConverter.toUpdateResponse(updatedEvent);

            ApiResponse<UpdateEventResponseDto> apiResponse = ApiResponse.onSuccess(
                    response,
                    SuccessStatus.UPDATE_EVENT_SUCCESS.getCode(),
                    SuccessStatus.UPDATE_EVENT_SUCCESS.getMessage()
            );

            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            log.error("일정 수정 실패", e);
            ApiResponse<UpdateEventResponseDto> apiResponse = ApiResponse.onFailure(
                    ErrorStatus.EVENT_UPDATE_FAILED.getCode(),
                    ErrorStatus.EVENT_UPDATE_FAILED.getMessage() + ": " + e.getMessage(),
                    null
            );
            return ResponseEntity.badRequest().body(apiResponse);
        }
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<ApiResponse<Void>> deleteEvent(@PathVariable Long eventId) {

        Long userId = 1L; // TODO: JWT에서 사용자 ID 추출

        try {
            eventCommandService.deleteEvent(eventId, userId);

            ApiResponse<Void> apiResponse = ApiResponse.onSuccess(
                    null,
                    SuccessStatus.DELETE_EVENT_SUCCESS.getCode(),
                    SuccessStatus.DELETE_EVENT_SUCCESS.getMessage()
            );
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            log.error("일정 삭제 실패", e);

            ApiResponse<Void> apiResponse = ApiResponse.onFailure(
                    ErrorStatus.EVENT_DELETE_FAILED.getCode(),
                    ErrorStatus.EVENT_DELETE_FAILED.getMessage() + ": " + e.getMessage(),
                    null
            );
            return ResponseEntity.badRequest().body(apiResponse);
        }
    }

    @GetMapping("/{date}")
    public ResponseEntity<ApiResponse<List<GetDayEventResponseDto>>> getDayEvents(@PathVariable String date) {

        Long userId = 1L; // TODO: JWT에서 사용자 ID 추출

        try {
            LocalDate eventDate = LocalDate.parse(date);
            List<GetDayEventResponseDto> events = eventQueryService.getDayEvents(eventDate, userId);

            return ResponseEntity.ok(
                    ApiResponse.onSuccess(events,
                            SuccessStatus.GET_DAY_EVENTS_SUCCESS.getCode(),
                            SuccessStatus.GET_DAY_EVENTS_SUCCESS.getMessage())
            );
        } catch (Exception e) {
            log.error("특정 날짜 일정 조회 실패", e);

            ApiResponse<List<GetDayEventResponseDto>> apiResponse = ApiResponse.onFailure(
                    ErrorStatus.EVENT_GET_BY_DATE_FAILED.getCode(),
                    ErrorStatus.EVENT_GET_BY_DATE_FAILED.getMessage() + ": " + e.getMessage(),
                    null
            );
            return ResponseEntity.badRequest().body(apiResponse);
        }
    }
}
