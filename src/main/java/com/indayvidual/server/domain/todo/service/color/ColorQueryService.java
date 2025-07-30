package com.indayvidual.server.domain.todo.service.color;

import com.indayvidual.server.domain.todo.converter.ColorConverter;
import com.indayvidual.server.domain.todo.dto.response.ColorResponseDTO;
import com.indayvidual.server.domain.todo.entity.Color;
import com.indayvidual.server.domain.todo.repository.ColorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ColorQueryService {

    private final ColorRepository colorRepository;
    private final ColorConverter colorConverter;

    public List<ColorResponseDTO> findAll() {
        List<Color> colors = colorRepository.findAllByOrderByIdAsc();
        return colorConverter.toResponseList(colors);
    }
}
