package com.indayvidual.server.domain.timetable.service;

import com.indayvidual.server.domain.timetable.entity.Timetable;
import com.indayvidual.server.domain.timetable.entity.enums.Semester;
import com.indayvidual.server.domain.timetable.exception.TimetableException;
import com.indayvidual.server.domain.timetable.repository.TimetableRepository;
import com.indayvidual.server.domain.user.service.UserService.S3Uploader;
import com.indayvidual.server.global.api.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class TimetableCommandServiceImpl implements TimetableCommandService {

    private final TimetableRepository timetableRepository;
    private final S3Uploader s3Uploader;

    @Override
    public Timetable createTimetableWithImage(String schoolId, String schoolName, Semester semester, MultipartFile image, Long userId) {
        if (timetableRepository.existsByUserIdAndSemester(userId, semester)) {
            throw new TimetableException(ErrorStatus.TIMETABLE_DUPLICATE_SEMESTER);
        }

        String imageUrl = s3Uploader.uploadProfileImage(userId, image);
        Timetable timetable = Timetable.builder()
                .userId(userId)
                .schoolId(schoolId)
                .schoolName(schoolName)
                .semester(semester)
                .timeTableImage(imageUrl)
                .build();
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
