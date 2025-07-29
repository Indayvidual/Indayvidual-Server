package com.indayvidual.server.domain.todo.service.color;

import com.indayvidual.server.domain.todo.entity.Color;
import com.indayvidual.server.domain.todo.repository.ColorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ColorQueryService {

    private final ColorRepository colorRepository;

    public List<String> findAll() {
        List<String> colors = colorRepository.findAllColorCodes();
        return colors;
    }
}
