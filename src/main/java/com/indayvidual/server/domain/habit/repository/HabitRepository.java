package com.indayvidual.server.domain.habit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.indayvidual.server.domain.habit.entity.Habit;

@Repository
public interface HabitRepository extends JpaRepository<Habit, Long> {
}
