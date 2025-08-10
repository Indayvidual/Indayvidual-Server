package com.indayvidual.server.domain.timetable.service;

import com.indayvidual.server.domain.timetable.entity.Timetable;
import com.indayvidual.server.domain.timetable.entity.enums.Semester;
import org.springframework.web.multipart.MultipartFile;

public interface TimetableCommandService {

    Timetable createTimetableWithImage(String schoolId, String schoolName, Semester semester, MultipartFile image, Long userId);

    void deleteTimetable(Long userId, Long timetableId);
}
