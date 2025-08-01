package com.indayvidual.server.domain.event.converter;

import com.indayvidual.server.domain.event.dto.request.CreateEventRequestDto;
import com.indayvidual.server.domain.event.dto.request.UpdateEventRequestDto;
import com.indayvidual.server.domain.event.dto.response.CreateEventResponseDto;
import com.indayvidual.server.domain.calendar.dto.response.GetDayEventResponseDto;
import com.indayvidual.server.domain.event.dto.response.UpdateEventResponseDto;
import com.indayvidual.server.domain.event.entity.Event;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
public class EventConverter {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final String DEFAULT_COLOR = "#CD7AFB";
    private static final LocalTime ALL_DAY_START_TIME = LocalTime.of(0, 0);
    private static final LocalTime ALL_DAY_END_TIME = LocalTime.of(23, 59);

    public Event toEntity(CreateEventRequestDto request, Long userId) {
        LocalTime startTime;
        LocalTime endTime;

        if (Boolean.TRUE.equals(request.getIsAllDay())) {
            startTime = ALL_DAY_START_TIME;
            endTime = ALL_DAY_END_TIME;
        } else {
            startTime = request.getStartTime() != null ? request.getStartTime() : LocalTime.of(9, 0);
            endTime = request.getEndTime();
        }

        return Event.builder()
                .userId(userId)
                .title(request.getTitle().trim())
                .eventDate(request.getDate())
                .startTime(startTime)
                .endTime(endTime)
                .colorCode(StringUtils.hasText(request.getColor()) ? request.getColor() : DEFAULT_COLOR)
                .userEndTime(endTime != null)
                .isAllDay(Boolean.TRUE.equals(request.getIsAllDay()))
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
                .isAllDay(entity.getIsAllDay())
                .build();
    }

    public void updateEntityFromRequest(Event entity, UpdateEventRequestDto request) {
        if (request.getDate() != null) {
            entity.setEventDate(request.getDate());
        }
        if (StringUtils.hasText(request.getTitle())) {
            entity.setTitle(request.getTitle().trim());
        }

        if (request.getIsAllDay() != null) {
            entity.setIsAllDay(request.getIsAllDay());

            if (Boolean.TRUE.equals(request.getIsAllDay())) {
                entity.setStartTime(ALL_DAY_START_TIME);
                entity.setEndTime(ALL_DAY_END_TIME);
                entity.setUserEndTime(true);
            } else {
                if (request.getStartTime() != null) {
                    entity.setStartTime(request.getStartTime());
                } else if (entity.getStartTime().equals(ALL_DAY_START_TIME)) {
                    entity.setStartTime(LocalTime.of(9, 0));
                }

                entity.setEndTime(request.getEndTime());
                entity.setUserEndTime(request.getEndTime() != null);
            }
        } else {
            if (!Boolean.TRUE.equals(entity.getIsAllDay())) {
                if (request.getStartTime() != null) {
                    entity.setStartTime(request.getStartTime());
                }
                entity.setEndTime(request.getEndTime());
                entity.setUserEndTime(request.getEndTime() != null);
            }
        }

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