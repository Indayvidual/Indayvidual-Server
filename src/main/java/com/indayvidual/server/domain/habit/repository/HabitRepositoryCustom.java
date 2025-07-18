package com.indayvidual.server.domain.habit.repository;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import com.indayvidual.server.domain.habit.entity.Habit;

public interface HabitRepositoryCustom {
	/**
	 * 특정 날짜의 사용자 습관과 체크 로그를 함께 조회
	 * 체크되지 않은 습관도 포함하여 모든 습관 반환
	 */
	List<Habit> findAllHabitsWithLogsOnDate(Long userId, LocalDate date);

	/**
	 * 특정 날짜부터 일주일간의 사용자 습관과 체크 로그를 함께 조회
	 * 체크되지 않은 습관도 포함하여 모든 습관 반환
	 */
	List<Habit> findAllHabitsWithLogsOnDateRange(Long userId, LocalDate startDate, LocalDate endDate);

	/**
	 * 특정 년도 월의 한달간의 사용자 사용자 습관과 체크한 날짜의 로그를 함께 조회
	 */
	List<Habit> findAllHabitsWithLogsOnMonth(Long userId, YearMonth date);

}
