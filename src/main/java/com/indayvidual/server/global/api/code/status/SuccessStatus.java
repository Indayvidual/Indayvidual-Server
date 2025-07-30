package com.indayvidual.server.global.api.code.status;

import com.indayvidual.server.global.api.code.BaseCode;
import com.indayvidual.server.global.api.response.ReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseCode {

    _OK(HttpStatus.OK, "COMMON200", "성공입니다."),

    // Temp 관련 응답
    TEMP_OK(HttpStatus.OK, "TEMP200", "임시 데이터 조회 성공"),

    // 일정 관련 응답
    CREATE_EVENT_SUCCESS(HttpStatus.OK, "CREATE_EVENT_SUCCESS", "일정 등록 성공"),
    UPDATE_EVENT_SUCCESS(HttpStatus.OK, "UPDATE_EVENT_SUCCESS", "일정 수정 성공"),
    DELETE_EVENT_SUCCESS(HttpStatus.OK, "DELETE_EVENT_SUCCESS", "일정 삭제 성공"),

    // 캘린더 관련 응답
    GET_CALENDAR_SUCCESS(HttpStatus.OK, "GET_CALENDAR_SUCCESS", "월별 캘린더 조회 성공"),
    GET_DAY_EVENTS_SUCCESS(HttpStatus.OK, "GET_DAY_EVENTS_SUCCESS", "특정 날짜 일정 조회 성공"),

    // 시간표 관련 응답
    CREATE_TIMETABLE_SUCCESS(HttpStatus.OK, "CREATE_TIMETABLE_SUCCESS", "시간표 등록 성공"),
    GET_TIMETABLE_SUCCESS(HttpStatus.OK, "GET_TIMETABLE_SUCCESS", "시간표 조회 성공"),
    DELETE_TIMETABLE_SUCCESS(HttpStatus.OK,"DELETE_TIMETABLE_SUCCESS", "시간표 삭제 성공"),

    // 할 일 관련 응답
    CREATE_CATEGORY_SUCCESS(HttpStatus.CREATED, "CREATE_CATEGORY_SUCCESS", "카테고리 등록 성공"),
    GET_CATEGORIES_SUCCESS(HttpStatus.OK, "GET_CATEGORIES_SUCCESS", "카테고리 목록 조회 성공"),
    CREATE_TASK_SUCCESS(HttpStatus.CREATED, "CREATE_TASK_SUCCESS", "할일 등록 성공"),
    GET_TASKS_SUCCESS(HttpStatus.OK, "GET_TASKS_SUCCESS", "할일 목록 조회 성공"),
    UPDATE_TASK_TITLE_SUCCESS(HttpStatus.OK, "UPDATE_TASK_TITLE_SUCCESS", "할일 제목 수정 성공"),
    UPDATE_TASK_DUE_DATE_SUCCESS(HttpStatus.OK, "UPDATE_TASK_DUEDATE_SUCCESS", "할일 날짜 수정 성공"),
    DELETE_TASK_SUCCESS(HttpStatus.OK, "DELETE_TASK_SUCCESS", "할일 삭제 성공"),
    UPDATE_TASK_CHECK_SUCCESS(HttpStatus.OK, "UPDATE_TASK_CHECK_SUCCESS", "할일 체크 상태 변경 성공"),
    UPDATE_TASK_ORDER_SUCCESS(HttpStatus.OK, "UPDATE_TASK_ORDER_SUCCESS", "할일 순서 변경 성공"),

    // 색상 관련 응답
    GET_COLORS_SUCCESS(HttpStatus.OK, "GET_COLORS_SUCCESS", "색상 목록 조회 성공")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ReasonDTO getReason() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(true)
                .build();
    }

    @Override
    public ReasonDTO getReasonHttpStatus() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(true)
                .httpStatus(httpStatus)
                .build()
                ;
    }
}
