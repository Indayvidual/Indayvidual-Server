package com.indayvidual.server.domain.habit.dto.request;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;

@Getter
@Schema(description = "습관 체크 관리 DTO")
public class ToggleCheckRequestDTO {

	@Schema(description = "습관 날짜", example = "2025-01-01")
	@NotNull(message = "날짜는 필수입니다")
	@PastOrPresent(message = "현재, 혹은 과거의 날짜만 선택 가능합니다.")
	private LocalDate date;

	@Schema(description = "습관 체크 여부", example = "true")
	@NotNull(message = "체크 여부는 필수입니다")
	private Boolean checked;

}
