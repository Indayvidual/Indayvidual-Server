package com.indayvidual.server.domain.event.service;

import com.indayvidual.server.domain.event.converter.EventConverter;
import com.indayvidual.server.domain.event.dto.request.CreateEventRequestDto;
import com.indayvidual.server.domain.event.dto.request.UpdateEventRequestDto;
import com.indayvidual.server.domain.event.entity.Event;
import com.indayvidual.server.domain.event.exception.EventException;
import com.indayvidual.server.domain.event.repository.EventRepository;
import com.indayvidual.server.global.api.code.status.ErrorStatus;
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
        if (request.getEndTime() != null &&
                (request.getStartTime().equals(request.getEndTime()) || request.getStartTime().isAfter(request.getEndTime()))) {
            throw new EventException(ErrorStatus.EVENT_INVALID_TIME_ORDER);
        }

        Event event = eventConverter.toEntity(request, userId);
        return eventRepository.save(event);
    }

    @Override
    public Event updateEvent(Long eventId, UpdateEventRequestDto request, Long userId) {
        Event existingEvent = eventQueryService.findByIdAndUserId(eventId, userId);

        validateTimeUpdate(existingEvent, request);
        eventConverter.updateEntityFromRequest(existingEvent, request);
        return eventRepository.save(existingEvent);
    }

    @Override
    public void deleteEvent(Long eventId, Long userId) {
        if (!eventQueryService.existsByIdAndUserId(eventId, userId)) {
            throw new EventException(ErrorStatus.EVENT_NOT_FOUND);
        }
        eventRepository.deleteByIdAndUserId(eventId, userId);
    }

    private void validateTimeUpdate(Event existingEvent, UpdateEventRequestDto request) {
        LocalTime newStartTime = request.getStartTime() != null ?
                request.getStartTime() : existingEvent.getStartTime();
        LocalTime newEndTime = request.getEndTime() != null ?
                request.getEndTime() : existingEvent.getEndTime();

        if (newEndTime != null &&
                (newStartTime.equals(newEndTime) || newStartTime.isAfter(newEndTime))) {
            throw new EventException(ErrorStatus.EVENT_INVALID_TIME_ORDER);
        }
    }

}
