package com.indayvidual.server.domain.habit.dto.response;

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
public class HabitSliceResponseDTO {

	@Schema(description = "습관 목록")
	private List<HabitResponseDTO> habits;

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

	public static HabitSliceResponseDTO from(Slice<HabitResponseDTO> slice) {
		return HabitSliceResponseDTO.builder()
			.habits(slice.getContent())
			.hasNext(slice.hasNext())
			.page(slice.getNumber())
			.size(slice.getSize())
			.numberOfElements(slice.getNumberOfElements())
			.first(slice.isFirst())
			.build();
	}

}
