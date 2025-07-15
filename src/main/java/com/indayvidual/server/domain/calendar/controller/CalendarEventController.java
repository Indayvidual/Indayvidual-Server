package com.indayvidual.server.domain.calendar.controller;

import com.indayvidual.server.domain.calendar.converter.EventConverter;
import com.indayvidual.server.domain.calendar.dto.request.CreateEventRequestDto;
import com.indayvidual.server.domain.calendar.dto.request.UpdateEventRequestDto;
import com.indayvidual.server.domain.calendar.dto.response.CreateEventResponseDto;
import com.indayvidual.server.domain.calendar.dto.response.UpdateEventResponseDto;
import com.indayvidual.server.domain.calendar.entity.Event;
import com.indayvidual.server.domain.calendar.service.EventCommandService;
import com.indayvidual.server.global.api.code.status.ErrorStatus;
import com.indayvidual.server.global.api.code.status.SuccessStatus;
import com.indayvidual.server.global.api.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/calendar/events")
@RequiredArgsConstructor
@Slf4j
public class CalendarEventController {

    private final EventCommandService eventCommandService;
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
}
