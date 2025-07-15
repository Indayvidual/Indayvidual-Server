package com.indayvidual.server.domain.timetable.dto.response;

import com.indayvidual.server.domain.timetable.entity.enums.Semester;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetTimetableResponseDto {

    private Long timetableId;
    private String schoolId;
    private Semester semester;
    private String imageUrl;

    public static GetTimetableResponseDto of(Long timetableId, String schoolId, Semester semester, String imageUrl) {
        return GetTimetableResponseDto.builder()
                .timetableId(timetableId)
                .schoolId(schoolId)
                .semester(semester)
                .imageUrl(imageUrl)
                .build();
    }
}
