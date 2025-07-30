package com.indayvidual.server.domain.timetable.converter;

import com.indayvidual.server.domain.timetable.dto.response.CreateTimetableResponseDto;
import com.indayvidual.server.domain.timetable.dto.response.GetTimetableResponseDto;
import com.indayvidual.server.domain.timetable.entity.Timetable;
import org.springframework.stereotype.Component;

@Component
public class TimetableConverter {

    public CreateTimetableResponseDto toCreateResponse(Timetable timetable) {
        return CreateTimetableResponseDto.of(
                timetable.getId(),
                timetable.getSchoolId(),
                timetable.getSemester(),
                timetable.getTimeTableImage()
        );
    }

    public GetTimetableResponseDto toGetResponse(Timetable timetable, String schoolId) {
        return GetTimetableResponseDto.of(
                timetable.getId(),
                schoolId,
                timetable.getSemester(),
                timetable.getTimeTableImage()
        );
    }
}
