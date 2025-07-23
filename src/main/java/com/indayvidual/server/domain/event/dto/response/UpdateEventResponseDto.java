package com.indayvidual.server.domain.event.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "이벤트 수정 응답 DTO")
public class UpdateEventResponseDto {

    @Schema(description = "일정 날짜", example = "2025-07-22")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @Schema(description = "이벤트 ID", example = "1")
    private Long eventId;

    @Schema(description = "이벤트 제목", example = "회의")
    private String title;

    @Schema(description = "시작 시간", example = "10:00")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @Schema(description = "종료 시간", example = "12:00")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;

    @Schema(description = "색상 코드", example = "#CD7AFB")
    private String color;
}
