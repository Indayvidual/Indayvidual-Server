package com.indayvidual.server.domain.todo.controller;

import com.indayvidual.server.domain.todo.dto.request.CategoryCreateRequestDTO;
import com.indayvidual.server.domain.todo.dto.response.CategoryResponseDTO;
import com.indayvidual.server.domain.todo.service.category.CategoryCommandService;
import com.indayvidual.server.domain.todo.service.category.CategoryQueryService;
import com.indayvidual.server.global.api.code.status.SuccessStatus;
import com.indayvidual.server.global.api.response.ApiResponse;
import com.indayvidual.server.global.util.Utils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Todo 카테고리 API", description = "Todo category 관련 API")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/todo/categories")
public class TodoCategoryController {

    private final CategoryCommandService categoryCommandService;
    private final CategoryQueryService categoryQueryService;

    @Operation(summary = "카테고리 등록", description = "새로운 카테고리를 등록합니다.")
    @PostMapping("")
    public ApiResponse<CategoryResponseDTO> createCategory(@RequestBody @Valid CategoryCreateRequestDTO request) {
        Long userId = Utils.getUserId();
        return ApiResponse.onSuccess(
                categoryCommandService.create(request, userId),
                SuccessStatus.CREATE_CATEGORY_SUCCESS.getCode(),
                SuccessStatus.CREATE_CATEGORY_SUCCESS.getMessage());
    }

    @Operation(summary = "카테고리 목록 조회", description = "카테고리 목록을 조회합니다.")
    @GetMapping("")
    public ApiResponse<List<CategoryResponseDTO>> getCategories() {
        Long userId = Utils.getUserId();
        return ApiResponse.onSuccess(
                categoryQueryService.findAll(userId),
                SuccessStatus.GET_CATEGORIES_SUCCESS.getCode(),
                SuccessStatus.GET_CATEGORIES_SUCCESS.getMessage());
    }

    @Operation(summary = "카테고리 수정", description = "카테고리 제목과 색상을 수정합니다.")
    @PatchMapping("/{categoryId}")
    public ApiResponse<CategoryResponseDTO> updateCategoryTitleAndColor(
            @PathVariable Long categoryId,
            @RequestBody @Valid CategoryCreateRequestDTO request
    ) {
        Long userId = Utils.getUserId();
        return ApiResponse.onSuccess(
                categoryCommandService.updateCategoryTitleAndColor(request, userId, categoryId),
                SuccessStatus.UPDATE_CATEGORY_SUCCESS.getCode(),
                SuccessStatus.UPDATE_CATEGORY_SUCCESS.getMessage());
    }

    @Operation(summary = "카테고리 삭제", description = """
            카테고리를 삭제합니다.\n
            **카테고리 내 할 일도 모두 삭제됩니다.**""")
    @DeleteMapping("/{categoryId}")
    public ApiResponse<Void> deleteCategory(@PathVariable Long categoryId) {
        Long userId = Utils.getUserId();
        categoryCommandService.delete(userId, categoryId);
        return ApiResponse.onSuccess(
                null,
                SuccessStatus.DELETE_CATEGORY_SUCCESS.getCode(),
                SuccessStatus.DELETE_CATEGORY_SUCCESS.getMessage());
    }
}
