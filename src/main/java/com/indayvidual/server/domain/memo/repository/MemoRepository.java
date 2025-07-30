package com.indayvidual.server.domain.memo.repository;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.indayvidual.server.domain.memo.entity.Memo;

@Repository
public interface MemoRepository extends JpaRepository<Memo, Long> {
	Optional<Memo> findByIdAndUserId(Long memoId, Long userId);

	Slice<Memo> findByUserIdOrderByCreatedAtDescIdDesc(Long userId, Pageable pageable);
}
