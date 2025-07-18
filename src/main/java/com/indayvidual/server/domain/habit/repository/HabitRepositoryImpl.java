package com.indayvidual.server.domain.habit.repository;

import static com.indayvidual.server.domain.habit.entity.QHabit.*;
import static com.indayvidual.server.domain.habitlog.entity.QHabitLog.*;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.indayvidual.server.domain.habit.entity.Habit;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class HabitRepositoryImpl implements HabitRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<Habit> findAllHabitsWithLogsOnDate(Long userId, LocalDate date) {
		return queryFactory
			.selectFrom(habit)
			.distinct()
			.leftJoin(habit.habitLogs, habitLog).fetchJoin()
			.where(
				habit.user.id.eq(userId),
				habitLog.checkedAt.eq(date))
			.fetch();
	}

	@Override
	public List<Habit> findAllHabitsWithLogsOnDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
		return queryFactory
			.selectFrom(habit)
			.distinct()
			.leftJoin(habit.habitLogs, habitLog).fetchJoin()
			.where(
				habit.user.id.eq(userId),
				habitLog.checkedAt.between(startDate, endDate))
			.orderBy(habitLog.checkedAt.asc())
			.fetch();

	}
}
