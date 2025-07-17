package com.indayvidual.server.domain.event.service;

import com.indayvidual.server.domain.event.dto.request.CreateEventRequestDto;
import com.indayvidual.server.domain.event.dto.request.UpdateEventRequestDto;
import com.indayvidual.server.domain.event.entity.Event;

public interface EventCommandService {
    Event createEvent(CreateEventRequestDto request, Long userId);
    Event updateEvent(Long eventId, UpdateEventRequestDto request, Long userId);
    void deleteEvent(Long eventId, Long userId);
}
