package com.indayvidual.server.domain.event.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "일정 생성 응답 DTO")
public class CreateEventResponseDto {

    @Schema(description = "생성된 일정 ID", example = "1")
    private Long eventId;
}
