package com.indayvidual.server.domain.memo.service.query;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.indayvidual.server.domain.memo.dto.request.MemoSliceResponseDTO;
import com.indayvidual.server.domain.memo.dto.response.MemoSummaryResponseDTO;
import com.indayvidual.server.domain.memo.entity.Memo;
import com.indayvidual.server.domain.memo.repository.MemoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemoQueryServiceImpl implements MemoQueryService {

	private static final int DEFAULT_PAGE_SIZE = 20;
	private static final int MAX_PAGE_SIZE = 100;
	private final MemoRepository memoRepository;

	@Override
	public MemoSliceResponseDTO getMemosWithSlice(Long userId, Integer page, Integer size) {
		// TODO: 사용자 정보 불러오기 로직

		int pageNumber = (page != null && page >= 0) ? page : 0;
		int pageSize = validatePageSize(size);

		PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
		Slice<Memo> memos = memoRepository.findByUserIdOrderByCreatedAtDescIdDesc(userId, pageRequest);

		return MemoSliceResponseDTO.from(memos.map(MemoSummaryResponseDTO::from));
	}

	private int validatePageSize(Integer size) {
		if (size == null)
			return DEFAULT_PAGE_SIZE;
		if (size <= 0)
			return DEFAULT_PAGE_SIZE;
		if (size > MAX_PAGE_SIZE)
			return MAX_PAGE_SIZE;
		return size;
	}
}
