package com.indayvidual.server.domain.timetable.controller;

import com.indayvidual.server.domain.timetable.converter.TimetableConverter;
import com.indayvidual.server.domain.timetable.dto.request.CreateTimetableRequestDto;
import com.indayvidual.server.domain.timetable.dto.response.CreateTimetableResponseDto;
import com.indayvidual.server.domain.timetable.entity.Timetable;
import com.indayvidual.server.domain.timetable.service.TimetableCommandService;
import com.indayvidual.server.global.api.code.status.ErrorStatus;
import com.indayvidual.server.global.api.code.status.SuccessStatus;
import com.indayvidual.server.global.api.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/timetable")
@RequiredArgsConstructor
@Slf4j
public class TimetableController {

    private final TimetableCommandService timetableCommandService;
    private final TimetableConverter timetableConverter;

    @PostMapping
    public ResponseEntity<ApiResponse<CreateTimetableResponseDto>> createTimetable(
            @Valid @RequestBody CreateTimetableRequestDto request) {

        Long userId = 1L; // TODO: JWT에서 사용자 ID 추출

        try {
            Timetable createdTimetable = timetableCommandService.createTimetable(request, userId);
            CreateTimetableResponseDto response = timetableConverter.toCreateResponse(createdTimetable);

            return ResponseEntity.ok(
                    ApiResponse.onSuccess(response,
                            SuccessStatus.CREATE_TIMETABLE_SUCCESS.getCode(),
                            SuccessStatus.CREATE_TIMETABLE_SUCCESS.getMessage())
            );
        } catch (IllegalArgumentException e) {
            log.error("시간표 등록 실패 - 중복 학기: {}", e.getMessage());
            return ResponseEntity.status(409).body(
                    ApiResponse.onFailure(
                            ErrorStatus.TIMETABLE_DUPLICATE_SEMESTER.getCode(),
                            ErrorStatus.TIMETABLE_DUPLICATE_SEMESTER.getMessage(),
                            null)
            );
        } catch (Exception e) {
            log.error("시간표 등록 실패", e);
            return ResponseEntity.badRequest().body(
                    ApiResponse.onFailure(
                            ErrorStatus.TIMETABLE_CREATE_FAILED.getCode(),
                            ErrorStatus.TIMETABLE_CREATE_FAILED.getMessage() + ": " + e.getMessage(),
                            null)
            );
        }
    }
}
