package com.indayvidual.server.domain.todo.converter;

import com.indayvidual.server.domain.todo.dto.response.ColorResponseDTO;
import com.indayvidual.server.domain.todo.entity.Color;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ColorConverter {

    public ColorResponseDTO toResponse(Color color) {
        return ColorResponseDTO.builder()
                .colorId(color.getId())
                .code(color.getColor_code())
                .build();
    }

    public List<ColorResponseDTO> toResponseList(List<Color> colors) {
        return colors.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}