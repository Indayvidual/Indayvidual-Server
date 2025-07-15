package com.indayvidual.server.domain.calendar.converter;

import com.indayvidual.server.domain.calendar.dto.request.CreateEventRequestDto;
import com.indayvidual.server.domain.calendar.dto.request.UpdateEventRequestDto;
import com.indayvidual.server.domain.calendar.dto.response.CreateEventResponseDto;
import com.indayvidual.server.domain.calendar.dto.response.GetDayEventResponseDto;
import com.indayvidual.server.domain.calendar.dto.response.UpdateEventResponseDto;
import com.indayvidual.server.domain.calendar.entity.Event;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class EventConverter {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public Event toEntity(CreateEventRequestDto request, Long userId) {
        boolean hasEndTime = request.getEndTime() != null;

        return Event.builder()
                .userId(userId)
                .title(request.getTitle())
                .eventDate(request.getDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .colorCode(request.getColor() != null ? request.getColor() : "#CD7AFB")
                .userEndTime(hasEndTime)
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
                .title(entity.getTitle())
                .startTime(entity.getStartTime())
                .endTime(entity.getEndTime())
                .color(entity.getColorCode())
                .build();
    }

    public void updateEntityFromRequest(Event entity, UpdateEventRequestDto request) {
        if (request.getTitle() != null) {
            entity.setTitle(request.getTitle());
        }
        if (request.getStartTime() != null) {
            entity.setStartTime(request.getStartTime());
        }
        if (request.getEndTime() != null) {
            entity.setEndTime(request.getEndTime());
            entity.setUserEndTime(true);
        } else if (request.getEndTime() == null && request.getEndTime() != entity.getEndTime()) {
            entity.setEndTime(null);
            entity.setUserEndTime(false);
        }
        if (request.getColor() != null) {
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
