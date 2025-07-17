package com.indayvidual.server.domain.memo.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.indayvidual.server.domain.memo.entity.Memo;

public interface MemoRepositoryCustom {

	Slice<Memo> findMemosByUserIdWithSlice(Long userId, Pageable pageable);
	
}
