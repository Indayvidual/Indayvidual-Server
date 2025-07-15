package com.indayvidual.server.domain.timetable.dto.response;

import com.indayvidual.server.domain.timetable.entity.enums.Semester;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateTimetableResponseDto {

    private Long timetableId;
    private String schoolId;
    private Semester semester;
    private String imageUrl;

    public static CreateTimetableResponseDto of(Long timetableId, String schoolId, Semester semester, String imageUrl) {
        return CreateTimetableResponseDto.builder()
                .timetableId(timetableId)
                .schoolId(schoolId)
                .semester(semester)
                .imageUrl(imageUrl)
                .build();
    }
}
