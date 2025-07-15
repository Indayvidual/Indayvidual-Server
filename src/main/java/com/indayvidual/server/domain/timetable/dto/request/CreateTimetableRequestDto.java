package com.indayvidual.server.domain.timetable.dto.request;

import com.indayvidual.server.domain.timetable.entity.enums.Semester;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateTimetableRequestDto {

    @NotBlank(message = "학교 ID는 필수입니다.")
    private String schoolId;

    @NotNull(message = "학기는 필수입니다.")
    private Semester semester;

    @NotBlank(message = "시간표 이미지 URL은 필수입니다.")
    @URL(message = "올바른 URL 형식이어야 합니다.")
    private String imageUrl;
}
