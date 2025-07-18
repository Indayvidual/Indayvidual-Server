package com.indayvidual.server.domain.todo.controller;

import com.indayvidual.server.domain.todo.dto.request.CategoryCreateRequestDTO;
import com.indayvidual.server.domain.todo.dto.response.CategoryResponseDTO;
import com.indayvidual.server.domain.todo.service.category.CategoryCommandService;
import com.indayvidual.server.domain.todo.service.category.CategoryQueryService;
import com.indayvidual.server.global.api.response.ApiResponse;
import com.indayvidual.server.global.util.Utils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Todo 카테고리 API", description = "Todo category 관련 API")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/todo/categories")
public class TodoCategoryController {
    // TODO: success status 추가

    private final CategoryCommandService categoryCommandService;
    private final CategoryQueryService categoryQueryService;

    @Operation(summary = "카테고리 등록", description = "새로운 카테고리를 등록합니다.")
    @PostMapping("")
    public ApiResponse<CategoryResponseDTO> createCategory(@RequestBody @Valid CategoryCreateRequestDTO request) {
        Long userId = Utils.getUserId();
        return ApiResponse.onSuccess(categoryCommandService.create(request, userId));
    }

    @Operation(summary = "카테고리 목록 조회", description = "카테고리 목록을 조회합니다.")
    @GetMapping("")
    public ApiResponse<List<CategoryResponseDTO>> getCategories() {
        Long userId = Utils.getUserId();
        return ApiResponse.onSuccess(categoryQueryService.findAll(userId));
    }
}
