package com.indayvidual.server.global.api.code.status;

import com.indayvidual.server.global.api.code.BaseErrorCode;
import com.indayvidual.server.global.api.response.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    _BAD_REQUEST_SAME_STATE(HttpStatus.BAD_REQUEST, "COMMON4002", "수정하려는 데이터가 현재 상태와 동일합니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    // Temp 관련 에러
    TEMP_NOT_FOUND(HttpStatus.NOT_FOUND, "TEMP4041", "임시 데이터가 존재하지 않습니다."),
    TEMP_EXCEPTION(HttpStatus.BAD_REQUEST, "TEMP4001", "테스트 에러입니다."),

    // 일정 관련 응답
    CREATE_EVENT_FAILED(HttpStatus.BAD_REQUEST, "CREATE_EVENT_FAILED", "일정 등록에 실패했습니다."),
    UPDATE_EVENT_FAILED(HttpStatus.BAD_REQUEST, "UPDATE_EVENT_FAILED", "일정 수정에 실패했습니다."),
    DELETE_EVENT_FAILED(HttpStatus.BAD_REQUEST, "DELETE_EVENT_FAILED", "일정 삭제에 실패했습니다."),

    // To do 관련 에러
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "CATEGORY_NOT_FOUND", "카테고리를 찾을 수 없습니다."),
    TASK_NOT_FOUND(HttpStatus.NOT_FOUND, "TASK_NOT_FOUND", "할 일을 찾을 수 없습니다."),
    TASK_FORBIDDEN(HttpStatus.FORBIDDEN, "TASK_FORBIDDEN", "해당 할 일에 대한 권한이 없습니다."),
    INVALID_TASK_ORDER(HttpStatus.BAD_REQUEST, "INVALID_TASK_ORDER", "유효하지 않은 할 일 순서 요청입니다.")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build()
                ;
    }
}
