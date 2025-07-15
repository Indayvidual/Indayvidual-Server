package com.indayvidual.server.global.api.code.status;

import org.springframework.http.HttpStatus;

import com.indayvidual.server.global.api.code.BaseErrorCode;
import com.indayvidual.server.global.api.response.ErrorReasonDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 시스템 전체의 에러 상태 코드를 관리하는 enum
 *
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

	// ===== 레이트 리미팅 관련 에러 (RATE_LIMIT) =====
	RATE_LIMIT_EXCEEDED(HttpStatus.TOO_MANY_REQUESTS, "RATE4291", "요청 한도를 초과했습니다."),
	RATE_LIMIT_USER_EXCEEDED(HttpStatus.TOO_MANY_REQUESTS, "RATE4292", "사용자별 요청 한도를 초과했습니다."),
	RATE_LIMIT_IP_EXCEEDED(HttpStatus.TOO_MANY_REQUESTS, "RATE4293", "IP별 요청 한도를 초과했습니다.");

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