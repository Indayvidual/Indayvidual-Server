package com.indayvidual.server.domain.calendar.service;

import com.indayvidual.server.domain.calendar.converter.EventConverter;
import com.indayvidual.server.domain.calendar.dto.request.CreateEventRequestDto;
import com.indayvidual.server.domain.calendar.dto.request.UpdateEventRequestDto;
import com.indayvidual.server.domain.calendar.entity.Event;
import com.indayvidual.server.domain.calendar.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;

@Service
@RequiredArgsConstructor
@Transactional
public class EventCommandServiceImpl implements EventCommandService {

    private final EventRepository eventRepository;
    private final EventConverter eventConverter;
    private final EventQueryService eventQueryService;

    @Override
    public Event createEvent(CreateEventRequestDto request, Long userId) {
        if (request.getEndTime() != null && request.getStartTime().isAfter(request.getEndTime())) {
            throw new RuntimeException("시작 시간이 종료 시간보다 늦을 수 없습니다.");
        }

        Event event = eventConverter.toEntity(request, userId);
        return eventRepository.save(event);
    }

    @Override
    public Event updateEvent(Long eventId, UpdateEventRequestDto request, Long userId) {
        Event existingEvent = eventQueryService.findByIdAndUserId(eventId, userId);

        LocalTime newStartTime = request.getStartTime() != null ? request.getStartTime() : existingEvent.getStartTime();
        LocalTime newEndTime = request.getEndTime() != null ? request.getEndTime() : existingEvent.getEndTime();

        if (newEndTime != null && newStartTime.isAfter(newEndTime)) {
            throw new RuntimeException("시작 시간이 종료 시간보다 늦을 수 없습니다.");
        }

        eventConverter.updateEntityFromRequest(existingEvent, request);
        return eventRepository.save(existingEvent);
    }
}
