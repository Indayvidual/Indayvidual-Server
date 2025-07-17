package com.indayvidual.server.domain.calendar.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetMonthlyCalendarResponseDto {

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private Boolean hasEvent;

    private Boolean hasTodo;
}
