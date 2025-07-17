package com.indayvidual.server.domain.event.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class UpdateEventRequestDto {

    @Size(max = 20, message = "제목은 20자 이내여야 합니다.")
    private String title;

    @Schema(type = "string", example = "10:00", description = "시작 시간 (HH:mm)")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @Schema(type = "string", example = "12:00", description = "종료 시간 (HH:mm)")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;

    @Pattern(regexp = "^#[0-9A-F]{6}$", message = "색상은 #RRGGBB 형식이어야 합니다.")
    private String color;
}
