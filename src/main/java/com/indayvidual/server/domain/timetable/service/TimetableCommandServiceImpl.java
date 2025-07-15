package com.indayvidual.server.domain.timetable.service;

import com.indayvidual.server.domain.timetable.converter.TimetableConverter;
import com.indayvidual.server.domain.timetable.dto.request.CreateTimetableRequestDto;
import com.indayvidual.server.domain.timetable.entity.Timetable;
import com.indayvidual.server.domain.timetable.repository.TimetableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TimetableCommandServiceImpl implements TimetableCommandService {

    private final TimetableRepository timetableRepository;
    private final TimetableConverter timetableConverter;

    @Override
    public Timetable createTimetable(CreateTimetableRequestDto request, Long userId) {

        // 중복 학기 체크
        if (timetableRepository.existsByUserIdAndSemester(userId, request.getSemester())) {
            throw new IllegalArgumentException("이미 해당 학기의 시간표가 존재합니다.");
        }

        Timetable timetable = timetableConverter.toEntity(request, userId);
        return timetableRepository.save(timetable);
    }
}
