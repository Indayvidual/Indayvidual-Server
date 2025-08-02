package com.indayvidual.server.domain.todo.service.color;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ColorService {

    public List<String> findAll() {
        List<String> colors = Arrays.asList(
                "#CD7AFB",
                "#C69ADF",
                "#CDB4DB",
                "#E5D3EF",
                "#EFE5F9",
                "#FB6F92",
                "#FF8FAB",
                "#FFB3C6",
                "#FFC2D1",
                "#FFE5EC",
                "#F08080",
                "#F4978E",
                "#F8AD9D",
                "#FBC4AB",
                "#FFDAB9",
                "#FFD400",
                "#FFE14B",
                "#F7E06E",
                "#FFED93",
                "#FFF3B6",
                "#338A17",
                "#11AF22",
                "#20C933",
                "#93E088",
                "#D1F7C4",
                "#02AAA4",
                "#06A09B",
                "#20D9D2",
                "#72DDC3",
                "#C2F5E9",
                "#005FA7",
                "#0077B6",
                "#00B4D8",
                "#5CCFE6",
                "#CAF0F8",
                "#4E5052",
                "#808284",
                "#999B9D",
                "#D7D9DB",
                "#F2F4F6"
        );
        return colors;
    }
}
