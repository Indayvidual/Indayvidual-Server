package com.indayvidual.server.domain.memo.dto.response;

import java.util.List;

import org.springframework.data.domain.Slice;

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
@Schema(description = "메모 목록 응답 (무한 스크롤)")
public class MemoSliceResponseDTO {

	@Schema(description = "메모 목록")
	private List<MemoSummaryResponseDTO> content;

	@Schema(description = "다음 페이지 존재 여부")
	private boolean hasNext;

	@Schema(description = "현재 페이지 번호")
	private int page;

	@Schema(description = "페이지 크기")
	private int size;

	@Schema(description = "현재 페이지 데이터 개수")
	private int numberOfElements;

	@Schema(description = "첫 페이지 여부")
	private boolean first;

	/**
	 * Slice에서 DTO로 변환
	 */
	public static MemoSliceResponseDTO from(Slice<MemoSummaryResponseDTO> slice) {
		return MemoSliceResponseDTO.builder()
			.content(slice.getContent())
			.hasNext(slice.hasNext())
			.page(slice.getNumber())
			.size(slice.getSize())
			.numberOfElements(slice.getNumberOfElements())
			.first(slice.isFirst())
			.build();
	}
}