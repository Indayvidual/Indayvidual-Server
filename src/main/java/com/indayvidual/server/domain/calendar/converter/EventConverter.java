package com.indayvidual.server.domain.calendar.converter;

import com.indayvidual.server.domain.calendar.dto.request.CreateEventRequestDto;
import com.indayvidual.server.domain.calendar.dto.response.CreateEventResponseDto;
import com.indayvidual.server.domain.calendar.entity.Event;
import org.springframework.stereotype.Component;

@Component
public class EventConverter {

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
}
