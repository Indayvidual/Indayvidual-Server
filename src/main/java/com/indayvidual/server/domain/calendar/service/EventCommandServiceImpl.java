package com.indayvidual.server.domain.calendar.service;

import com.indayvidual.server.domain.calendar.converter.EventConverter;
import com.indayvidual.server.domain.calendar.dto.request.CreateEventRequestDto;
import com.indayvidual.server.domain.calendar.entity.Event;
import com.indayvidual.server.domain.calendar.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class EventCommandServiceImpl implements EventCommandService {

    private final EventRepository eventRepository;
    private final EventConverter eventConverter;

    @Override
    public Event createEvent(CreateEventRequestDto request, Long userId) {
        if (request.getEndTime() != null && request.getStartTime().isAfter(request.getEndTime())) {
            throw new RuntimeException("시작 시간이 종료 시간보다 늦을 수 없습니다.");
        }

        Event event = eventConverter.toEntity(request, userId);
        return eventRepository.save(event);
    }
}
