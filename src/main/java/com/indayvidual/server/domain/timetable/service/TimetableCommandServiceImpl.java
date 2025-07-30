package com.indayvidual.server.domain.timetable.service;

import com.indayvidual.server.domain.timetable.converter.TimetableConverter;
import com.indayvidual.server.domain.timetable.dto.request.CreateTimetableRequestDto;
import com.indayvidual.server.domain.timetable.entity.Timetable;
import com.indayvidual.server.domain.timetable.exception.TimetableException;
import com.indayvidual.server.domain.timetable.repository.TimetableRepository;
import com.indayvidual.server.global.api.code.status.ErrorStatus;
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

        if (timetableRepository.existsByUserIdAndSemester(userId, request.getSemester())) {
            throw new TimetableException(ErrorStatus.TIMETABLE_DUPLICATE_SEMESTER);
        }

        Timetable timetable = timetableConverter.toEntity(request, userId);
        return timetableRepository.save(timetable);
    }

    @Override
    public void deleteTimetable(Long userId, Long timetableId) {
        Timetable timetable = timetableRepository.findById(timetableId)
                .orElseThrow(() -> new TimetableException(ErrorStatus.TIMETABLE_NOT_FOUND));

        if (!timetable.getUserId().equals(userId)) {
            throw new TimetableException(ErrorStatus.TIMETABLE_FORBIDDEN);
        }

        timetableRepository.deleteById(timetableId);
    }
}
