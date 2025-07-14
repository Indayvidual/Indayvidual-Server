package com.indayvidual.server.domain.memo.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "메모 생성 요청 DTO")
public class CreateMemoRequestDTO {

	@Schema(description = "메모 제목", example = "오늘 할 일", required = true)
	private String title;

	@Schema(description = "메모 내용", example = "장보기, 운동하기, 책 읽기", required = true)
	private String content;

}
