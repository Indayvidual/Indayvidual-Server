package com.indayvidual.server.domain.event.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "이벤트 수정 요청 DTO")
public class UpdateEventRequestDto {

    @Schema(description = "이벤트 제목", example = "회의")
    @Size(max = 20, message = "제목은 20자 이내여야 합니다.")
    private String title;

    @Schema(description = "시작 시간", example = "10:00")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @Schema(description = "종료 시간", example = "12:00")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;

    @Schema(description = "색상 코드", example = "#CD7AFB")
    @Pattern(regexp = "^#[0-9A-F]{6}$", message = "색상은 #RRGGBB 형식이어야 합니다.")
    private String color;
}
