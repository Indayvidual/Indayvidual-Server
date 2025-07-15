package com.indayvidual.server.domain.timetable.service;

import com.indayvidual.server.domain.timetable.converter.TimetableConverter;
import com.indayvidual.server.domain.timetable.dto.response.GetTimetableResponseDto;
import com.indayvidual.server.domain.timetable.entity.Timetable;
import com.indayvidual.server.domain.timetable.repository.TimetableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TimetableQueryServiceImpl implements TimetableQueryService {

    private final TimetableRepository timetableRepository;
    private final TimetableConverter timetableConverter;

    @Override
    public List<GetTimetableResponseDto> getTimetables(Long userId) {

        List<Timetable> timetables = timetableRepository.findByUserIdOrderByCreatedAtAsc(userId);

        List<GetTimetableResponseDto> response = timetables.stream()
                .map(timetable -> {
                    String schoolId = timetable.getSchoolId();
                    return timetableConverter.toGetResponse(timetable, schoolId);
                })
                .collect(Collectors.toList());

        return response;
    }
}
