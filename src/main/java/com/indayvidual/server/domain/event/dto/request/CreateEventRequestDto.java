package com.indayvidual.server.domain.event.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "이벤트 생성 요청 DTO")
public class CreateEventRequestDto {

    @Schema(description = "이벤트 날짜", example = "2025-07-22", required = true)
    @NotNull(message = "날짜는 필수입니다.")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @Schema(description = "이벤트 제목", example = "회의", required = true)
    @NotBlank(message = "제목은 필수입니다.")
    @Size(max = 20, message = "제목은 20자 이내여야 합니다.")
    private String title;

    @Schema(description = "시작 시간", example = "10:00", required = true)
    @JsonFormat(pattern = "HH:mm")
    @NotNull(message = "시작 시간은 필수입니다.")
    private LocalTime startTime;

    @Schema(description = "종료 시간", example = "12:00")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;

    @Schema(description = "색상 코드", example = "#CD7AFB")
    @Pattern(regexp = "^#[0-9A-F]{6}$", message = "색상은 #RRGGBB 형식이어야 합니다.")
    private String color;
}
