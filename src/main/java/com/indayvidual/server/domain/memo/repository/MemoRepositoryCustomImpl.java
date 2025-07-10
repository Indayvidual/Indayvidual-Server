package com.indayvidual.server.domain.memo.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import com.indayvidual.server.domain.memo.entity.Memo;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MemoRepositoryCustomImpl implements MemoRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Slice<Memo> findMemosByUserIdWithSlice(Long userId, Pageable pageable) {
		return null;
	}

}
