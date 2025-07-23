package com.indayvidual.server.domain.event.repository;

import com.indayvidual.server.domain.event.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    /**
     * 사용자 ID와 이벤트 ID로 이벤트 조회
     */
    Optional<Event> findByIdAndUserId(Long id, Long userId);

    /**
     * 사용자 ID와 이벤트 ID로 이벤트 삭제
     */
    void deleteByIdAndUserId(Long id, Long userId);

    /**
     * 사용자 ID와 이벤트 ID로 이벤트 존재 여부 확인
     */
    boolean existsByIdAndUserId(Long id, Long userId);

    /**
     * 특정 날짜의 사용자 이벤트를 시작 시간 순으로 조회
     */
    List<Event> findByUserIdAndEventDateOrderByStartTimeAsc(Long userId, LocalDate eventDate);

    /**
     * 사용자 ID와 낢짜 범위로 일정(Event) 전체 조회
     */
    List<Event> findByUserIdAndEventDateBetween(Long userId, LocalDate startDate, LocalDate endDate);
}
