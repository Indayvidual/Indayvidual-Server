package com.indayvidual.server.domain.calendar.controller;

import com.indayvidual.server.domain.calendar.converter.EventConverter;
import com.indayvidual.server.domain.calendar.dto.request.CreateEventRequestDto;
import com.indayvidual.server.domain.calendar.dto.request.UpdateEventRequestDto;
import com.indayvidual.server.domain.calendar.dto.response.CreateEventResponseDto;
import com.indayvidual.server.domain.calendar.dto.response.UpdateEventResponseDto;
import com.indayvidual.server.domain.calendar.entity.Event;
import com.indayvidual.server.domain.calendar.service.EventCommandService;
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
                    ApiResponse.onSuccess(response, "CREATE_EVENT_SUCCESS", "일정 등록 성공")
            );
        } catch (Exception e) {
            log.error("일정 등록 실패", e);
            return ResponseEntity.badRequest().body(
                    ApiResponse.onFailure("CREATE_EVENT_FAILED", "일정 등록에 실패했습니다: " + e.getMessage(), null)
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
                    "UPDATE_EVENT_SUCCESS", "일정 수정 성공"
            );

            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            log.error("일정 수정 실패", e);
            ApiResponse<UpdateEventResponseDto> apiResponse = ApiResponse.onFailure(
                    "UPDATE_EVENT_FAILED", "일정 수정에 실패했습니다: " + e.getMessage(), null
            );
            return ResponseEntity.badRequest().body(apiResponse);
        }
    }

}
