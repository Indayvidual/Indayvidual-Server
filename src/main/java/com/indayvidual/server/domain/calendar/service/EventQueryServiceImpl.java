package com.indayvidual.server.domain.calendar.service;

import com.indayvidual.server.domain.calendar.entity.Event;
import com.indayvidual.server.domain.calendar.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventQueryServiceImpl implements EventQueryService {

    private final EventRepository eventRepository;

    @Override
    public Event findByIdAndUserId(Long eventId, Long userId) {
        return eventRepository.findByIdAndUserId(eventId, userId)
                .orElseThrow(() -> new RuntimeException("해당 일정을 찾을 수 없습니다."));
    }
}
