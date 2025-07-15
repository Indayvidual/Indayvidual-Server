package com.indayvidual.server.domain.timetable.entity;

import com.indayvidual.server.common.BaseEntity;
import com.indayvidual.server.domain.timetable.entity.enums.Semester;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Timetable extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "school_id", nullable = false)
    private String schoolId;

    @Column(name = "semester", nullable = false)
    private Semester semester;

    @Column(name = "time_table_image", nullable = false)
    private String timeTableImage;

    @Builder
    public Timetable(Long userId, String schoolId, Semester semester, String timeTableImage) {
        this.userId = userId;
        this.schoolId = schoolId;
        this.semester = semester;
        this.timeTableImage = timeTableImage;
    }
}
