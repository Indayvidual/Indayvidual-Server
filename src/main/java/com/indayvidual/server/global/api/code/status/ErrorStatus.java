package com.indayvidual.server.global.api.code.status;

import org.springframework.http.HttpStatus;

import com.indayvidual.server.global.api.code.BaseErrorCode;
import com.indayvidual.server.global.api.response.ErrorReasonDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 시스템 전체의 에러 상태 코드를 관리하는 enum
 * <p>
 * 네이밍 컨벤션: [도메인]_[상황]_[상세]
 * 코드 컨벤션: [도메인][HTTP상태코드][순번]
 */
@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    // ===== 공통 에러 (COMMON) =====
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    _BAD_REQUEST_SAME_STATE(HttpStatus.BAD_REQUEST, "COMMON4002", "수정하려는 데이터가 현재 상태와 동일합니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),
    _NOT_FOUND(HttpStatus.NOT_FOUND, "COMMON404", "요청한 리소스를 찾을 수 없습니다."),
    _METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "COMMON405", "지원하지 않는 HTTP 메서드입니다."),
    _CONFLICT(HttpStatus.CONFLICT, "COMMON409", "리소스 충돌이 발생했습니다."),

    // ===== 데이터 검증 에러 (VALIDATION) =====
    VALIDATION_INVALID_INPUT(HttpStatus.BAD_REQUEST, "VALID4001", "입력값이 유효하지 않습니다."),
    VALIDATION_MISSING_PARAMETER(HttpStatus.BAD_REQUEST, "VALID4002", "필수 파라미터가 누락되었습니다."),
    VALIDATION_INVALID_FORMAT(HttpStatus.BAD_REQUEST, "VALID4003", "입력 형식이 올바르지 않습니다."),
    VALIDATION_OUT_OF_RANGE(HttpStatus.BAD_REQUEST, "VALID4004", "입력값이 허용 범위를 벗어났습니다."),

    // ===== 사용자 관련 에러 (USER) =====
    // 인증 관련
    USER_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "USER4011", "사용자 인증에 실패했습니다."),
    USER_INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "USER4012", "유효하지 않은 토큰입니다."),
    USER_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "USER4013", "토큰이 만료되었습니다."),
    USER_INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "USER4014", "아이디 또는 비밀번호가 올바르지 않습니다."),

    // ===== AUTH =====
    AUTH_EMAIL_DUPLICATED(HttpStatus.CONFLICT, "AUTH4091", "이미 가입된 이메일입니다."),
    AUTH_INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "AUTH4010", "이메일 또는 비밀번호가 올바르지 않습니다."),
    AUTH_LOCAL_NOT_AVAILABLE(HttpStatus.BAD_REQUEST, "AUTH4001", "소셜 전용 계정으로 이메일 로그인이 불가합니다."),
    AUTH_REFRESH_MISSING(HttpStatus.BAD_REQUEST, "AUTH4002", "Refresh-Token 헤더가 없습니다."),
    AUTH_REFRESH_INVALID(HttpStatus.UNAUTHORIZED, "AUTH4011", "유효하지 않은 리프레시 토큰입니다."),
    AUTH_REFRESH_EXPIRED(HttpStatus.UNAUTHORIZED, "AUTH4012", "만료된 리프레시 토큰입니다."),
    AUTH_REFRESH_NOT_FOUND(HttpStatus.UNAUTHORIZED, "AUTH4013", "저장된 리프레시 토큰이 없습니다."),
    AUTH_REFRESH_REVOKED(HttpStatus.UNAUTHORIZED, "AUTH4014", "이미 사용되었거나 취소된 리프레시 토큰입니다."),
    AUTH_USER_NOT_FOUND(HttpStatus.UNAUTHORIZED, "AUTH4015", "존재하지 않는 사용자입니다."),
    AUTH_OAUTH_INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH4016", "소셜 액세스 토큰이 유효하지 않습니다."),
    AUTH_OAUTH_PROVIDER_ERROR(HttpStatus.BAD_GATEWAY, "AUTH5021", "소셜 인증 제공자와의 통신에 실패했습니다."),

    // 계정 관련
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER4041", "사용자를 찾을 수 없습니다."),
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "USER4091", "이미 존재하는 사용자입니다."),
    USER_EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "USER4092", "이미 사용 중인 이메일입니다."),
    USER_NICKNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "USER4093", "이미 사용 중인 닉네임입니다."),

    // 권한 관련
    USER_ACCESS_DENIED(HttpStatus.FORBIDDEN, "USER4031", "해당 리소스에 접근할 권한이 없습니다."),
    USER_ACCOUNT_LOCKED(HttpStatus.FORBIDDEN, "USER4032", "계정이 잠겨있습니다."),
    USER_ACCOUNT_DISABLED(HttpStatus.FORBIDDEN, "USER4033", "비활성화된 계정입니다."),
    USER_PERMISSION_DENIED(HttpStatus.FORBIDDEN, "USER4034", "해당 작업을 수행할 권한이 없습니다."),

    // 프로필 관련
    USER_PROFILE_UPDATE_FAILED(HttpStatus.BAD_REQUEST, "USER4001", "프로필 업데이트에 실패했습니다."),
    USER_PASSWORD_CHANGE_FAILED(HttpStatus.BAD_REQUEST, "USER4002", "비밀번호 변경에 실패했습니다."),
    USER_INVALID_PASSWORD_FORMAT(HttpStatus.BAD_REQUEST, "USER4003", "비밀번호 형식이 올바르지 않습니다."),

    // ===== 캘린더 관련 에러 (CALENDAR) =====
    CALENDAR_FETCH_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "CAL4001", "월별 캘린더 정보 조회에 실패했습니다."),
    CALENDAR_INVALID_DATE_INPUT(HttpStatus.BAD_REQUEST, "CAL4002", "올바르지 않은 연월 입력입니다."),

    // ===== 일정 관련 에러 (EVENT) =====
    EVENT_NOT_FOUND(HttpStatus.NOT_FOUND, "EVENT4041", "일정을 찾을 수 없습니다."),
    EVENT_ACCESS_DENIED(HttpStatus.FORBIDDEN, "EVENT4031", "일정에 접근할 권한이 없습니다."),
    EVENT_ALREADY_EXISTS(HttpStatus.CONFLICT, "EVENT4091", "동일한 시간대에 이미 일정이 존재합니다."),
    EVENT_TIME_CONFLICT(HttpStatus.CONFLICT, "EVENT4092", "일정 시간이 충돌합니다."),

    EVENT_CREATE_FAILED(HttpStatus.BAD_REQUEST, "EVENT4001", "일정 등록에 실패했습니다."),
    EVENT_UPDATE_FAILED(HttpStatus.BAD_REQUEST, "EVENT4002", "일정 수정에 실패했습니다."),
    EVENT_DELETE_FAILED(HttpStatus.BAD_REQUEST, "EVENT4003", "일정 삭제에 실패했습니다."),
    EVENT_INVALID_DATE_RANGE(HttpStatus.BAD_REQUEST, "EVENT4004", "유효하지 않은 날짜 범위입니다."),
    EVENT_PAST_DATE_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "EVENT4005", "과거 날짜에는 일정을 생성할 수 없습니다."),
    EVENT_GET_BY_DATE_FAILED(HttpStatus.BAD_REQUEST, "EVENT4006", "특정 날짜 일정 조회에 실패했습니다."),
    EVENT_INVALID_TIME_ORDER(HttpStatus.BAD_REQUEST, "EVENT4007", "시작 시간은 종료 시간보다 빨라야 합니다."),
    EVENT_INVALID_DATE_FORMAT(HttpStatus.BAD_REQUEST, "EVENT4008", "날짜 형식이 올바르지 않습니다."),

    // ===== 시간표 관련 에러 (TIMETABLE) =====
    TIMETABLE_CREATE_FAILED(HttpStatus.BAD_REQUEST, "TIMETABLE4001", "시간표 등록에 실패했습니다."),
    TIMETABLE_DUPLICATE_SEMESTER(HttpStatus.CONFLICT, "TIMETABLE4091", "이미 해당 학기의 시간표가 존재합니다."),
    TIMETABLE_FETCH_FAILED(HttpStatus.BAD_REQUEST, "TIMETABLE4002", "시간표 조회에 실패했습니다."),
    TIMETABLE_NOT_FOUND(HttpStatus.NOT_FOUND, "TIMETABLE4041", "존재하지 않는 시간표입니다."),
    TIMETABLE_FORBIDDEN(HttpStatus.FORBIDDEN, "TIMETABLE4031", "시간표를 삭제할 권한이 없습니다."),

    // ===== 파일 관련 에러 (FILE) =====
    FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "FILE4041", "파일을 찾을 수 없습니다."),
    FILE_UPLOAD_FAILED(HttpStatus.BAD_REQUEST, "FILE4001", "파일 업로드에 실패했습니다."),
    FILE_DELETE_FAILED(HttpStatus.BAD_REQUEST, "FILE4002", "파일 삭제에 실패했습니다."),
    FILE_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST, "FILE4003", "파일 크기가 제한을 초과했습니다."),
    FILE_INVALID_TYPE(HttpStatus.BAD_REQUEST, "FILE4004", "지원하지 않는 파일 형식입니다."),
    FILE_EMPTY(HttpStatus.BAD_REQUEST, "FILE4005", "빈 파일은 업로드할 수 없습니다."),
    FILE_NAME_TOO_LONG(HttpStatus.BAD_REQUEST, "FILE4006", "파일명이 너무 깁니다."),

    // ===== 데이터베이스 관련 에러 (DATABASE) =====
    DATABASE_CONNECTION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "DB5001", "데이터베이스 연결에 실패했습니다."),
    DATABASE_CONSTRAINT_VIOLATION(HttpStatus.CONFLICT, "DB4091", "데이터베이스 제약조건을 위반했습니다."),
    DATABASE_TRANSACTION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "DB5002", "데이터베이스 트랜잭션에 실패했습니다."),
    DATABASE_TIMEOUT(HttpStatus.REQUEST_TIMEOUT, "DB4081", "데이터베이스 요청 시간이 초과되었습니다."),

    // ===== 외부 API 관련 에러 (EXTERNAL) =====
    EXTERNAL_API_ERROR(HttpStatus.BAD_GATEWAY, "EXT5021", "외부 API 호출에 실패했습니다."),
    EXTERNAL_API_TIMEOUT(HttpStatus.GATEWAY_TIMEOUT, "EXT5041", "외부 API 응답 시간이 초과되었습니다."),
    EXTERNAL_API_INVALID_RESPONSE(HttpStatus.BAD_GATEWAY, "EXT5022", "외부 API 응답이 올바르지 않습니다."),
    EXTERNAL_API_RATE_LIMIT(HttpStatus.TOO_MANY_REQUESTS, "EXT4291", "외부 API 요청 한도를 초과했습니다."),

    // ===== 비즈니스 로직 관련 에러 (BUSINESS) =====
    BUSINESS_RULE_VIOLATION(HttpStatus.BAD_REQUEST, "BIZ4001", "비즈니스 규칙을 위반했습니다."),
    BUSINESS_LOGIC_ERROR(HttpStatus.BAD_REQUEST, "BIZ4002", "비즈니스 로직 처리 중 오류가 발생했습니다."),
    BUSINESS_STATE_INVALID(HttpStatus.CONFLICT, "BIZ4091", "현재 상태에서는 해당 작업을 수행할 수 없습니다."),

    // ===== 임시 데이터 관련 에러 (TEMP) =====
    TEMP_NOT_FOUND(HttpStatus.NOT_FOUND, "TEMP4041", "임시 데이터가 존재하지 않습니다."),
    TEMP_EXPIRED(HttpStatus.BAD_REQUEST, "TEMP4001", "임시 데이터가 만료되었습니다."),
    TEMP_INVALID_TOKEN(HttpStatus.BAD_REQUEST, "TEMP4002", "유효하지 않은 임시 토큰입니다."),
    TEMP_EXCEPTION(HttpStatus.BAD_REQUEST, "TEMP4003", "테스트 에러입니다."),

    // ===== 메모 관련 에러 (MEMO) =====
    // 기본 CRUD 작업
    MEMO_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMO4041", "메모를 찾을 수 없습니다."),
    MEMO_CREATE_FAILED(HttpStatus.BAD_REQUEST, "MEMO4001", "메모 생성에 실패했습니다."),
    MEMO_UPDATE_FAILED(HttpStatus.BAD_REQUEST, "MEMO4002", "메모 수정에 실패했습니다."),
    MEMO_DELETE_FAILED(HttpStatus.BAD_REQUEST, "MEMO4003", "메모 삭제에 실패했습니다."),
    MEMO_LIST_FETCH_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "MEMO5001", "메모 목록 조회에 실패했습니다."),

    // 권한 관련
    MEMO_ACCESS_DENIED(HttpStatus.FORBIDDEN, "MEMO4031", "메모에 접근할 권한이 없습니다."),
    MEMO_OWNER_MISMATCH(HttpStatus.FORBIDDEN, "MEMO4032", "메모 소유자가 아닙니다."),
    MEMO_SHARE_PERMISSION_DENIED(HttpStatus.FORBIDDEN, "MEMO4033", "메모 공유 권한이 없습니다."),
    MEMO_PRIVATE_ACCESS_DENIED(HttpStatus.FORBIDDEN, "MEMO4034", "비공개 메모에 접근할 수 없습니다."),

    // 내용 검증 관련
    MEMO_TITLE_EMPTY(HttpStatus.BAD_REQUEST, "MEMO4004", "메모 제목이 비어있습니다."),
    MEMO_CONTENT_EMPTY(HttpStatus.BAD_REQUEST, "MEMO4005", "메모 내용이 비어있습니다."),
    MEMO_TITLE_TOO_LONG(HttpStatus.BAD_REQUEST, "MEMO4006", "메모 제목이 너무 깁니다."),
    MEMO_CONTENT_TOO_LONG(HttpStatus.BAD_REQUEST, "MEMO4007", "메모 내용이 너무 깁니다."),
    MEMO_INVALID_FORMAT(HttpStatus.BAD_REQUEST, "MEMO4008", "메모 형식이 올바르지 않습니다."),

    // 비즈니스 로직 관련
    MEMO_DUPLICATE_TITLE(HttpStatus.CONFLICT, "MEMO4091", "동일한 제목의 메모가 이미 존재합니다."),
    MEMO_COUNT_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "MEMO4009", "메모 개수 제한을 초과했습니다."),
    MEMO_ALREADY_SHARED(HttpStatus.CONFLICT, "MEMO4092", "이미 공유된 메모입니다."),
    MEMO_NOT_SHARED(HttpStatus.BAD_REQUEST, "MEMO4010", "공유되지 않은 메모입니다."),
    MEMO_SELF_SHARE_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "MEMO4011", "자신에게는 메모를 공유할 수 없습니다."),

    // 카테고리/태그 관련
    MEMO_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMO4042", "메모 카테고리를 찾을 수 없습니다."),
    MEMO_CATEGORY_ALREADY_EXISTS(HttpStatus.CONFLICT, "MEMO4093", "이미 존재하는 카테고리입니다."),
    MEMO_CATEGORY_IN_USE(HttpStatus.CONFLICT, "MEMO4094", "사용 중인 카테고리는 삭제할 수 없습니다."),
    MEMO_TAG_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMO4043", "메모 태그를 찾을 수 없습니다."),
    MEMO_TAG_ALREADY_EXISTS(HttpStatus.CONFLICT, "MEMO4095", "이미 존재하는 태그입니다."),
    MEMO_TOO_MANY_TAGS(HttpStatus.BAD_REQUEST, "MEMO4012", "태그 개수 제한을 초과했습니다."),
    MEMO_INVALID_TAG_FORMAT(HttpStatus.BAD_REQUEST, "MEMO4013", "태그 형식이 올바르지 않습니다."),

    // 즐겨찾기 관련
    MEMO_ALREADY_FAVORITED(HttpStatus.CONFLICT, "MEMO4096", "이미 즐겨찾기에 추가된 메모입니다."),
    MEMO_NOT_FAVORITED(HttpStatus.BAD_REQUEST, "MEMO4014", "즐겨찾기에 없는 메모입니다."),
    MEMO_FAVORITE_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "MEMO4015", "즐겨찾기 개수 제한을 초과했습니다."),

    // 첨부파일 관련
    MEMO_ATTACHMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMO4044", "메모 첨부파일을 찾을 수 없습니다."),
    MEMO_ATTACHMENT_UPLOAD_FAILED(HttpStatus.BAD_REQUEST, "MEMO4016", "첨부파일 업로드에 실패했습니다."),
    MEMO_ATTACHMENT_DELETE_FAILED(HttpStatus.BAD_REQUEST, "MEMO4017", "첨부파일 삭제에 실패했습니다."),
    MEMO_ATTACHMENT_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST, "MEMO4018", "첨부파일 크기 제한을 초과했습니다."),
    MEMO_ATTACHMENT_COUNT_EXCEEDED(HttpStatus.BAD_REQUEST, "MEMO4019", "첨부파일 개수 제한을 초과했습니다."),
    MEMO_ATTACHMENT_INVALID_TYPE(HttpStatus.BAD_REQUEST, "MEMO4020", "지원하지 않는 첨부파일 형식입니다."),

    // 검색 관련
    MEMO_SEARCH_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "MEMO5002", "메모 검색에 실패했습니다."),
    MEMO_SEARCH_KEYWORD_TOO_SHORT(HttpStatus.BAD_REQUEST, "MEMO4021", "검색 키워드가 너무 짧습니다."),
    MEMO_SEARCH_KEYWORD_INVALID(HttpStatus.BAD_REQUEST, "MEMO4022", "유효하지 않은 검색 키워드입니다."),
    MEMO_SEARCH_NO_RESULTS(HttpStatus.NOT_FOUND, "MEMO4045", "검색 결과가 없습니다."),

    // 버전 관리 관련
    MEMO_VERSION_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMO4046", "메모 버전을 찾을 수 없습니다."),
    MEMO_VERSION_RESTORE_FAILED(HttpStatus.BAD_REQUEST, "MEMO4023", "메모 버전 복원에 실패했습니다."),
    MEMO_VERSION_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "MEMO4024", "버전 히스토리 개수 제한을 초과했습니다."),
    MEMO_SAME_VERSION_CONTENT(HttpStatus.BAD_REQUEST, "MEMO4025", "이전 버전과 동일한 내용입니다."),

    // 암호화/보안 관련
    MEMO_ENCRYPTION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "MEMO5003", "메모 암호화에 실패했습니다."),
    MEMO_DECRYPTION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "MEMO5004", "메모 복호화에 실패했습니다."),
    MEMO_INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "MEMO4011", "메모 비밀번호가 올바르지 않습니다."),
    MEMO_PASSWORD_REQUIRED(HttpStatus.UNAUTHORIZED, "MEMO4012", "암호로 보호된 메모입니다."),

    // 공유 관련 상세
    MEMO_SHARE_TOKEN_INVALID(HttpStatus.BAD_REQUEST, "MEMO4026", "유효하지 않은 공유 토큰입니다."),
    MEMO_SHARE_TOKEN_EXPIRED(HttpStatus.BAD_REQUEST, "MEMO4027", "공유 토큰이 만료되었습니다."),
    MEMO_SHARE_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "MEMO4028", "공유 대상 수 제한을 초과했습니다."),
    MEMO_SHARE_CREATE_FAILED(HttpStatus.BAD_REQUEST, "MEMO4029", "메모 공유 생성에 실패했습니다."),
    MEMO_SHARE_DELETE_FAILED(HttpStatus.BAD_REQUEST, "MEMO4030", "메모 공유 삭제에 실패했습니다."),

    // ===== 습관 관련 에러 (HABIT) =====
    // 기본 CRUD 작업
    HABIT_NOT_FOUND(HttpStatus.NOT_FOUND, "HABIT4041", "습관을 찾을 수 없습니다."),
    HABIT_CREATE_FAILED(HttpStatus.BAD_REQUEST, "HABIT4001", "습관 생성에 실패했습니다."),
    HABIT_UPDATE_FAILED(HttpStatus.BAD_REQUEST, "HABIT4002", "습관 수정에 실패했습니다."),
    HABIT_DELETE_FAILED(HttpStatus.BAD_REQUEST, "HABIT4003", "습관 삭제에 실패했습니다."),
    HABIT_LIST_FETCH_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "HABIT5001", "습관 목록 조회에 실패했습니다."),

    // 권한 관련
    HABIT_ACCESS_DENIED(HttpStatus.FORBIDDEN, "HABIT4031", "습관에 접근할 권한이 없습니다."),
    HABIT_OWNER_MISMATCH(HttpStatus.FORBIDDEN, "HABIT4032", "습관 소유자가 아닙니다."),
    HABIT_MODIFICATION_DENIED(HttpStatus.FORBIDDEN, "HABIT4033", "습관을 수정할 권한이 없습니다."),
    HABIT_DELETION_DENIED(HttpStatus.FORBIDDEN, "HABIT4034", "습관을 삭제할 권한이 없습니다."),

    // 내용 검증 관련
    HABIT_TITLE_EMPTY(HttpStatus.BAD_REQUEST, "HABIT4004", "습관 이름이 비어있습니다."),
    HABIT_TITLE_TOO_LONG(HttpStatus.BAD_REQUEST, "HABIT4005", "습관 이름이 너무 깁니다."),
    HABIT_INVALID_COLOR_CODE(HttpStatus.BAD_REQUEST, "HABIT4006", "유효하지 않은 색상 코드입니다."),
    HABIT_INVALID_FORMAT(HttpStatus.BAD_REQUEST, "HABIT4007", "습관 형식이 올바르지 않습니다."),

    // 비즈니스 로직 관련
    HABIT_DUPLICATE_TITLE(HttpStatus.CONFLICT, "HABIT4091", "동일한 이름의 습관이 이미 존재합니다."),
    HABIT_COUNT_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "HABIT4008", "습관 개수 제한을 초과했습니다."),
    HABIT_ALREADY_ASSIGNED(HttpStatus.CONFLICT, "HABIT4092", "이미 등록된 습관입니다."),
    HABIT_NOT_ASSIGNED(HttpStatus.BAD_REQUEST, "HABIT4009", "등록되지 않은 습관입니다."),
    HABIT_ACTIVE_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "HABIT4010", "활성 습관 개수 제한을 초과했습니다."),

    // 습관 체크 관련 (UserHabitLog)
    HABIT_LOG_NOT_FOUND(HttpStatus.NOT_FOUND, "HABIT4042", "습관 기록을 찾을 수 없습니다."),
    HABIT_CHECK_FAILED(HttpStatus.BAD_REQUEST, "HABIT4011", "습관 체크에 실패했습니다."),
    HABIT_UNCHECK_FAILED(HttpStatus.BAD_REQUEST, "HABIT4012", "습관 체크 해제에 실패했습니다."),
    HABIT_ALREADY_CHECKED(HttpStatus.CONFLICT, "HABIT4093", "이미 체크된 습관입니다."),
    HABIT_NOT_CHECKED(HttpStatus.BAD_REQUEST, "HABIT4013", "체크되지 않은 습관입니다."),
    HABIT_CHECK_TIME_INVALID(HttpStatus.BAD_REQUEST, "HABIT4014", "유효하지 않은 체크 시간입니다."),
    HABIT_FUTURE_CHECK_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "HABIT4015", "미래 날짜에는 습관을 체크할 수 없습니다."),
    HABIT_PAST_CHECK_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "HABIT4016", "과거 날짜 체크 가능 기간을 초과했습니다."),

    // 습관 통계/분석 관련
    HABIT_STATISTICS_NOT_FOUND(HttpStatus.NOT_FOUND, "HABIT4043", "습관 통계를 찾을 수 없습니다."),
    HABIT_STATISTICS_CALCULATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "HABIT5002", "습관 통계 계산에 실패했습니다."),
    HABIT_STREAK_CALCULATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "HABIT5003", "연속 달성 계산에 실패했습니다."),
    HABIT_INSUFFICIENT_DATA(HttpStatus.BAD_REQUEST, "HABIT4017", "통계 계산을 위한 데이터가 부족합니다."),

    // 습관 카테고리/태그 관련
    HABIT_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "HABIT4044", "습관 카테고리를 찾을 수 없습니다."),
    HABIT_CATEGORY_ALREADY_EXISTS(HttpStatus.CONFLICT, "HABIT4094", "이미 존재하는 카테고리입니다."),
    HABIT_CATEGORY_IN_USE(HttpStatus.CONFLICT, "HABIT4095", "사용 중인 카테고리는 삭제할 수 없습니다."),
    HABIT_TAG_NOT_FOUND(HttpStatus.NOT_FOUND, "HABIT4045", "습관 태그를 찾을 수 없습니다."),
    HABIT_TAG_ALREADY_EXISTS(HttpStatus.CONFLICT, "HABIT4096", "이미 존재하는 태그입니다."),
    HABIT_TOO_MANY_TAGS(HttpStatus.BAD_REQUEST, "HABIT4018", "태그 개수 제한을 초과했습니다."),

    // 습관 알림/리마인더 관련
    HABIT_REMINDER_NOT_FOUND(HttpStatus.NOT_FOUND, "HABIT4046", "습관 알림을 찾을 수 없습니다."),
    HABIT_REMINDER_CREATE_FAILED(HttpStatus.BAD_REQUEST, "HABIT4019", "습관 알림 생성에 실패했습니다."),
    HABIT_REMINDER_UPDATE_FAILED(HttpStatus.BAD_REQUEST, "HABIT4020", "습관 알림 수정에 실패했습니다."),
    HABIT_REMINDER_DELETE_FAILED(HttpStatus.BAD_REQUEST, "HABIT4021", "습관 알림 삭제에 실패했습니다."),
    HABIT_REMINDER_TIME_INVALID(HttpStatus.BAD_REQUEST, "HABIT4022", "유효하지 않은 알림 시간입니다."),
    HABIT_REMINDER_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "HABIT4023", "알림 개수 제한을 초과했습니다."),

    // 습관 공유/소셜 관련
    HABIT_SHARE_NOT_ALLOWED(HttpStatus.FORBIDDEN, "HABIT4035", "습관 공유가 허용되지 않습니다."),
    HABIT_ALREADY_SHARED(HttpStatus.CONFLICT, "HABIT4097", "이미 공유된 습관입니다."),
    HABIT_NOT_SHARED(HttpStatus.BAD_REQUEST, "HABIT4024", "공유되지 않은 습관입니다."),
    HABIT_SHARE_TOKEN_INVALID(HttpStatus.BAD_REQUEST, "HABIT4025", "유효하지 않은 공유 토큰입니다."),
    HABIT_SHARE_TOKEN_EXPIRED(HttpStatus.BAD_REQUEST, "HABIT4026", "공유 토큰이 만료되었습니다."),

    // 습관 목표/도전 관련
    HABIT_GOAL_NOT_FOUND(HttpStatus.NOT_FOUND, "HABIT4047", "습관 목표를 찾을 수 없습니다."),
    HABIT_GOAL_ALREADY_EXISTS(HttpStatus.CONFLICT, "HABIT4098", "이미 설정된 목표가 있습니다."),
    HABIT_GOAL_INVALID_PERIOD(HttpStatus.BAD_REQUEST, "HABIT4027", "유효하지 않은 목표 기간입니다."),
    HABIT_GOAL_INVALID_TARGET(HttpStatus.BAD_REQUEST, "HABIT4028", "유효하지 않은 목표 수치입니다."),
    HABIT_CHALLENGE_NOT_FOUND(HttpStatus.NOT_FOUND, "HABIT4048", "습관 도전을 찾을 수 없습니다."),
    HABIT_CHALLENGE_ALREADY_JOINED(HttpStatus.CONFLICT, "HABIT4099", "이미 참여 중인 도전입니다."),
    HABIT_CHALLENGE_FULL(HttpStatus.BAD_REQUEST, "HABIT4029", "도전 참여 인원이 가득 찼습니다."),
    HABIT_CHALLENGE_ENDED(HttpStatus.BAD_REQUEST, "HABIT4030", "종료된 도전입니다."),

    // 습관 템플릿 관련
    HABIT_TEMPLATE_NOT_FOUND(HttpStatus.NOT_FOUND, "HABIT4049", "습관 템플릿을 찾을 수 없습니다."),
    HABIT_TEMPLATE_CREATE_FAILED(HttpStatus.BAD_REQUEST, "HABIT4031", "습관 템플릿 생성에 실패했습니다."),
    HABIT_TEMPLATE_APPLY_FAILED(HttpStatus.BAD_REQUEST, "HABIT4032", "습관 템플릿 적용에 실패했습니다."),
    HABIT_TEMPLATE_INVALID(HttpStatus.BAD_REQUEST, "HABIT4033", "유효하지 않은 습관 템플릿입니다."),

    // 습관 데이터 동기화 관련
    HABIT_SYNC_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "HABIT5004", "습관 데이터 동기화에 실패했습니다."),
    HABIT_SYNC_CONFLICT(HttpStatus.CONFLICT, "HABIT4100", "습관 데이터 동기화 충돌이 발생했습니다."),
    HABIT_SYNC_IN_PROGRESS(HttpStatus.CONFLICT, "HABIT4101", "습관 데이터 동기화가 진행 중입니다."),

    // ===== 레이트 리미팅 관련 에러 (RATE_LIMIT) =====
    RATE_LIMIT_EXCEEDED(HttpStatus.TOO_MANY_REQUESTS, "RATE4291", "요청 한도를 초과했습니다."),
    RATE_LIMIT_USER_EXCEEDED(HttpStatus.TOO_MANY_REQUESTS, "RATE4292", "사용자별 요청 한도를 초과했습니다."),
    RATE_LIMIT_IP_EXCEEDED(HttpStatus.TOO_MANY_REQUESTS, "RATE4293", "IP별 요청 한도를 초과했습니다."),

    // ===== 할 일 관련 에러 (TASK) =====
    // 할 일 카테고리
    TASK_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "TASK4041", "카테고리를 찾을 수 없습니다."),
    TASK_CATEGORY_TITLE_EMPTY(HttpStatus.BAD_REQUEST, "TASK4001", "카테고리 이름이 비어있습니다."),

    // 할 일
    TASK_NOT_FOUND(HttpStatus.NOT_FOUND, "TASK4042", "할 일을 찾을 수 없습니다."),
    TASK_FORBIDDEN(HttpStatus.FORBIDDEN, "TASK4031", "해당 할 일에 대한 권한이 없습니다."),
    TASK_INVALID_ORDER(HttpStatus.BAD_REQUEST, "TASK4002", "유효하지 않은 할 일 순서 요청입니다."),

    // ===== 색상 관련 에러 (COLOR) =====
    COLOR_INVALID(HttpStatus.BAD_REQUEST, "COLOR4001", "유효하지 않은 색상 코드 형식입니다.");

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
                .build();
    }
}
