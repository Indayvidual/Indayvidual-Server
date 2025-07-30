package com.indayvidual.server.domain.todo.controller;

import com.indayvidual.server.domain.todo.dto.response.ColorResponseDTO;
import com.indayvidual.server.domain.todo.service.color.ColorQueryService;
import com.indayvidual.server.global.api.code.status.SuccessStatus;
import com.indayvidual.server.global.api.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Color", description = "Color 관련 API")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/colors")
public class ColorController {

    private final ColorQueryService colorQueryService;

    @Operation(summary = "색상 목록 조회", description = "색상 목록을 조회합니다.")
    @GetMapping("")
    public ApiResponse<List<ColorResponseDTO>> getColors() {
        return ApiResponse.onSuccess(
                colorQueryService.findAll(),
                SuccessStatus.GET_COLORS_SUCCESS.getCode(),
                SuccessStatus.GET_COLORS_SUCCESS.getMessage());
    }
}
