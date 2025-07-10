package com.indayvidual.server.domain.calendar.service;

import com.indayvidual.server.domain.calendar.entity.Event;

public interface EventQueryService {
    Event findByIdAndUserId(Long eventId, Long userId);
    boolean existsByIdAndUserId(Long eventId, Long userId);
}
