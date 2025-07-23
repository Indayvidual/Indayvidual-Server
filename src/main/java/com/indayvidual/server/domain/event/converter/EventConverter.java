package com.indayvidual.server.domain.event.converter;

import com.indayvidual.server.domain.event.dto.request.CreateEventRequestDto;
import com.indayvidual.server.domain.event.dto.request.UpdateEventRequestDto;
import com.indayvidual.server.domain.event.dto.response.CreateEventResponseDto;
import com.indayvidual.server.domain.calendar.dto.response.GetDayEventResponseDto;
import com.indayvidual.server.domain.event.dto.response.UpdateEventResponseDto;
import com.indayvidual.server.domain.event.entity.Event;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.format.DateTimeFormatter;

@Component
public class EventConverter {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final String DEFAULT_COLOR = "#CD7AFB";

    public Event toEntity(CreateEventRequestDto request, Long userId) {
        return Event.builder()
                .userId(userId)
                .title(request.getTitle().trim())
                .eventDate(request.getDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .colorCode(StringUtils.hasText(request.getColor()) ? request.getColor() : DEFAULT_COLOR)
                .userEndTime(request.getEndTime() != null)
                .build();
    }

    public CreateEventResponseDto toCreateResponse(Event entity) {
        return CreateEventResponseDto.builder()
                .eventId(entity.getId())
                .build();
    }

    public UpdateEventResponseDto toUpdateResponse(Event entity) {
        return UpdateEventResponseDto.builder()
                .eventId(entity.getId())
                .date(entity.getEventDate())
                .title(entity.getTitle())
                .startTime(entity.getStartTime())
                .endTime(entity.getEndTime())
                .color(entity.getColorCode())
                .build();
    }

    public void updateEntityFromRequest(Event entity, UpdateEventRequestDto request) {
        if (request.getDate() != null) {
            entity.setEventDate(request.getDate());
        }
        if (StringUtils.hasText(request.getTitle())) {
            entity.setTitle(request.getTitle().trim());
        }
        if (request.getStartTime() != null) {
            entity.setStartTime(request.getStartTime());
        }
        entity.setEndTime(request.getEndTime());
        entity.setUserEndTime(request.getEndTime() != null);

        if (StringUtils.hasText(request.getColor())) {
            entity.setColorCode(request.getColor());
        }
    }

    public GetDayEventResponseDto toGetDayEventResponse(Event event) {
        return GetDayEventResponseDto.builder()
                .eventId(event.getId())
                .type("event")
                .title(event.getTitle())
                .startTime(event.getStartTime().format(TIME_FORMATTER))
                .endTime(event.getEndTime() != null ? event.getEndTime().format(TIME_FORMATTER) : null)
                .color(event.getColorCode())
                .build();
    }

}
