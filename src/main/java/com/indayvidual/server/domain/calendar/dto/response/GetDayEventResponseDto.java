package com.indayvidual.server.domain.calendar.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetDayEventResponseDto {

    private Long eventId;

    private String type;

    private String title;

    private String startTime;

    private String endTime;

    private String color;

    private Boolean isAllDay;
}