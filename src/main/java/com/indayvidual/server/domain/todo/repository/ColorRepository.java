package com.indayvidual.server.domain.todo.repository;

import com.indayvidual.server.domain.todo.entity.Color;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ColorRepository extends JpaRepository<Color, Long> {

    List<Color> findAllByOrderByIdAsc();
}
