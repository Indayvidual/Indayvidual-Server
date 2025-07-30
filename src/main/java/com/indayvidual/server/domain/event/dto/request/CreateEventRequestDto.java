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
@Schema(description = "일정 생성 요청 DTO")
public class CreateEventRequestDto {

    @Schema(description = "일정 날짜", example = "2025-07-22", required = true)
    @NotNull(message = "날짜는 필수입니다.")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @Schema(description = "일정 제목", example = "회의", required = true)
    @NotBlank(message = "제목은 필수입니다.")
    @Size(max = 20, message = "제목은 20자 이내여야 합니다.")
    private String title;

    @Schema(description = "시작 시간", example = "10:00")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @Schema(description = "종료 시간", example = "12:00")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;

    @Schema(description = "색상 ID", example = "1")
    private Long colorId;

    @Schema(description = "하루종일 여부", example = "false")
    @Builder.Default
    private Boolean isAllDay = false;
}
