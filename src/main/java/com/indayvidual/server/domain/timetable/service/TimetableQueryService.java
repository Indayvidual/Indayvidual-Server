package com.indayvidual.server.domain.timetable.service;

import com.indayvidual.server.domain.timetable.dto.response.GetTimetableResponseDto;

import java.util.List;

public interface TimetableQueryService {

    List<GetTimetableResponseDto> getTimetables(Long userId);
}
