package com.indayvidual.server.domain.todo.repository;

import com.indayvidual.server.domain.todo.entity.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ColorRepository extends JpaRepository<Color, Long> {

    @Query("SELECT c.color_code FROM Color c ORDER BY c.id ASC")
    List<String> findAllColorCodes();
}
