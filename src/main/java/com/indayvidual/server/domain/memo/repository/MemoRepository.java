package com.indayvidual.server.domain.memo.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.indayvidual.server.domain.memo.entity.Memo;

@Repository
public interface MemoRepository extends JpaRepository<Memo, Long> {
	Slice<Memo> findByUserIdOrderByCreatedAtDescIdDesc(Long userId, Pageable pageable);
}
