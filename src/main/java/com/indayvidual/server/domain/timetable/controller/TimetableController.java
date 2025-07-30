package com.indayvidual.server.domain.timetable.controller;

import com.indayvidual.server.domain.timetable.converter.TimetableConverter;
import com.indayvidual.server.domain.timetable.dto.response.CreateTimetableResponseDto;
import com.indayvidual.server.domain.timetable.dto.response.GetTimetableResponseDto;
import com.indayvidual.server.domain.timetable.entity.Timetable;
import com.indayvidual.server.domain.timetable.entity.enums.Semester;
import com.indayvidual.server.domain.timetable.service.TimetableCommandService;
import com.indayvidual.server.domain.timetable.service.TimetableQueryService;
import com.indayvidual.server.global.api.code.status.SuccessStatus;
import com.indayvidual.server.global.api.response.ApiResponse;
import com.indayvidual.server.global.util.Utils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/timetable")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Timetable", description = "시간표 관련 API")
public class TimetableController {

    private final TimetableCommandService timetableCommandService;
    private final TimetableConverter timetableConverter;
    private final TimetableQueryService timetableQueryService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "시간표 등록", description = "새로운 시간표를 등록합니다. 중복 학기는 허용되지 않습니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "시간표 등록 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "이미 해당 학기의 시간표가 존재함")
    })
    public ApiResponse<CreateTimetableResponseDto> createTimetable(
            @RequestParam("schoolId")
            @Parameter(description = "학교 ID (커리어넷 학교 정보 오픈API의 seq 값, 예: '767', '14')", required = true)
            String schoolId,

            @RequestParam("semester")
            @Parameter(
                    description = "학기",
                    required = true,
                    schema = @Schema(type = "string", allowableValues = {
                            "1학년 1학기",
                            "1학년 2학기",
                            "2학년 1학기",
                            "2학년 2학기",
                            "3학년 1학기",
                            "3학년 2학기",
                            "4학년 1학기",
                            "4학년 2학기"
                    })
            )
            String semesterLabel,

            @RequestPart("image")
            @Parameter(description = "시간표 이미지 파일", required = true)
            MultipartFile image) {

        Semester semester = Semester.from(semesterLabel);
        Long userId = Utils.getUserId();
        Timetable createdTimetable = timetableCommandService.createTimetableWithImage(schoolId, semester, image, userId);
        CreateTimetableResponseDto response = timetableConverter.toCreateResponse(createdTimetable);

        return ApiResponse.onSuccess(response,
                SuccessStatus.CREATE_TIMETABLE_SUCCESS.getCode(),
                SuccessStatus.CREATE_TIMETABLE_SUCCESS.getMessage());
    }

    @GetMapping
    @Operation(summary = "시간표 전체 조회", description = "시간표 목록을 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "시간표 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "시간표 조회 실패")
    })
    public ApiResponse<List<GetTimetableResponseDto>> getTimetables() {

        Long userId = Utils.getUserId();
        List<GetTimetableResponseDto> timetables = timetableQueryService.getTimetables(userId);

        return ApiResponse.onSuccess(timetables,
                SuccessStatus.GET_TIMETABLE_SUCCESS.getCode(),
                SuccessStatus.GET_TIMETABLE_SUCCESS.getMessage());
    }

    @DeleteMapping("/{timetableId}")
    @Operation(summary = "시간표 삭제", description = "시간표를 삭제합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "시간표 삭제 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "존재하지 않는 시간표"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "삭제 권한 없음")
    })
    public ApiResponse<Void> deleteTimetable(
            @PathVariable Long timetableId) {

        Long userId = Utils.getUserId();
        timetableCommandService.deleteTimetable(userId, timetableId);

        return ApiResponse.onSuccess(
                null,
                SuccessStatus.DELETE_TIMETABLE_SUCCESS.getCode(),
                SuccessStatus.DELETE_TIMETABLE_SUCCESS.getMessage());
    }
}
