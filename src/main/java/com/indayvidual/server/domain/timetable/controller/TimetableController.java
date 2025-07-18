package com.indayvidual.server.domain.timetable.controller;

import com.indayvidual.server.domain.timetable.converter.TimetableConverter;
import com.indayvidual.server.domain.timetable.dto.request.CreateTimetableRequestDto;
import com.indayvidual.server.domain.timetable.dto.response.CreateTimetableResponseDto;
import com.indayvidual.server.domain.timetable.dto.response.GetTimetableResponseDto;
import com.indayvidual.server.domain.timetable.entity.Timetable;
import com.indayvidual.server.domain.timetable.service.TimetableCommandService;
import com.indayvidual.server.domain.timetable.service.TimetableQueryService;
import com.indayvidual.server.global.api.code.status.ErrorStatus;
import com.indayvidual.server.global.api.code.status.SuccessStatus;
import com.indayvidual.server.global.api.response.ApiResponse;
import com.indayvidual.server.global.util.Utils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/timetable")
@RequiredArgsConstructor
@Slf4j
public class TimetableController {

    private final TimetableCommandService timetableCommandService;
    private final TimetableConverter timetableConverter;
    private final TimetableQueryService timetableQueryService;

    @PostMapping
    public ResponseEntity<ApiResponse<CreateTimetableResponseDto>> createTimetable(
            @Valid @RequestBody CreateTimetableRequestDto request) {

        Long userId = Utils.getUserId();; // TODO: JWT에서 사용자 ID 추출

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

    @GetMapping
    public ResponseEntity<ApiResponse<List<GetTimetableResponseDto>>> getTimetables() {

        Long userId = Utils.getUserId();; // TODO: JWT에서 사용자 ID 추출

        try {
            List<GetTimetableResponseDto> timetables = timetableQueryService.getTimetables(userId);

            return ResponseEntity.ok(
                    ApiResponse.onSuccess(timetables,
                            SuccessStatus.GET_TIMETABLE_SUCCESS.getCode(),
                            SuccessStatus.GET_TIMETABLE_SUCCESS.getMessage())
            );
        } catch (Exception e) {
            log.error("시간표 조회 실패", e);
            return ResponseEntity.badRequest().body(
                    ApiResponse.onFailure(
                            ErrorStatus.TIMETABLE_FETCH_FAILED.getCode(),
                            ErrorStatus.TIMETABLE_FETCH_FAILED.getMessage() + ": " + e.getMessage(),
                            null)
            );
        }
    }
}
