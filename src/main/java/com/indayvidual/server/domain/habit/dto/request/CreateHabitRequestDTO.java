package com.indayvidual.server.domain.habit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@Schema(description = "습관 생성 요청 DTO")
public class CreateHabitRequestDTO {

	@Schema(description = "습관 이름", example = "7시간 수면")
	@NotBlank(message = "습관 이름을 입력해야합니다.")
	private String title;

	@Schema(description = "색상 코드", example = "#FF0000")
	@NotNull(message = "색상 코드를 선택해야합니다.")
	private String color;

}
