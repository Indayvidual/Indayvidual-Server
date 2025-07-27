package com.indayvidual.server.domain.memo.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
@Schema(description = "메모 생성 요청 DTO")
public class CreateMemoRequestDTO {

	@Schema(description = "메모 제목", example = "오늘 장 볼 거")
	@NotBlank(message = "메모의 제목을 입력해야합니다.")
	private String title;

	@Schema(description = "메모 내용", example = "장보기, 운동하기, 책 읽기")
	@NotBlank(message = "메모의 내용을 입력해야합니다.")
	private String content;

}
