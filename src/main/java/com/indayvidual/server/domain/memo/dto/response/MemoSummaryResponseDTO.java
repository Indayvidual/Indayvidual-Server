package com.indayvidual.server.domain.memo.dto.response;

import java.time.LocalDateTime;

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
@Schema(description = "메모 요약 정보")
public class MemoSummaryResponseDTO {

	@Schema(description = "메모 ID")
	private Long id;

	@Schema(description = "메모 제목")
	private String title;

	@Schema(description = "메모 내용 (앞부분만)")
	private String contentPreview;

	@Schema(description = "생성일시")
	private LocalDateTime createdAt;

	@Schema(description = "수정일시")
	private LocalDateTime updatedAt;

	public static MemoSummaryResponseDTO from(Memo memo) {
		return MemoSummaryResponseDTO.builder()
			.id(memo.getId())
			.contentPreview(memo.getContent())
			.createdAt(memo.getCreatedAt())
			.updatedAt(memo.getUpdatedAt())
			.build();
	}
}
