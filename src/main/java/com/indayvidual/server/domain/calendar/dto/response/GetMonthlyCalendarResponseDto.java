package com.indayvidual.server.domain.calendar.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetMonthlyCalendarResponseDto {

    @Schema(description = "날짜", example = "2025-07-23")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @Schema(description = "해당 날짜에 포함된 일정의 색상 코드 리스트", example = "[\"#FF0000\", \"#00FF00\"]")
    private List<String> colors;
}
