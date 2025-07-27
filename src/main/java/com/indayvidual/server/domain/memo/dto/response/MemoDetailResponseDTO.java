package com.indayvidual.server.domain.memo.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

import com.indayvidual.server.domain.memo.entity.Memo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "메모 상세 조회 정보")
public class MemoDetailResponseDTO {

	@Schema(description = "메모 ID", example = "3")
	private Long memoId;

	@Schema(description = "메모 내용", example = "메모 내용")
	private String content;

	@Schema(description = "생성 일자", example = "2021-01-01")
	private LocalDate createdDate;

	@Schema(description = "생성 시간", example = "10:00")
	private LocalTime createdTime;

	public static MemoDetailResponseDTO from(Memo memo) {
		return MemoDetailResponseDTO.builder()
			.memoId(memo.getId())
			.content(memo.getContent())
			.createdDate(memo.getCreatedAt().toLocalDate())
			.createdTime(memo.getCreatedAt().toLocalTime())
			.build();
	}
}
