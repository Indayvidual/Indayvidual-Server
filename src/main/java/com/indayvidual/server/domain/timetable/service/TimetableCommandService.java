package com.indayvidual.server.domain.timetable.service;

import com.indayvidual.server.domain.timetable.dto.request.CreateTimetableRequestDto;
import com.indayvidual.server.domain.timetable.entity.Timetable;

public interface TimetableCommandService {

    Timetable createTimetable(CreateTimetableRequestDto request, Long userId);
}
