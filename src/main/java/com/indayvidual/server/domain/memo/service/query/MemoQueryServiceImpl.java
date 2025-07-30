package com.indayvidual.server.domain.memo.service.query;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.indayvidual.server.domain.memo.dto.response.MemoDetailResponseDTO;
import com.indayvidual.server.domain.memo.dto.response.MemoSliceResponseDTO;
import com.indayvidual.server.domain.memo.dto.response.MemoSummaryResponseDTO;
import com.indayvidual.server.domain.memo.entity.Memo;
import com.indayvidual.server.domain.memo.exception.MemoException;
import com.indayvidual.server.domain.memo.repository.MemoRepository;
import com.indayvidual.server.global.api.code.status.ErrorStatus;
import com.indayvidual.server.global.util.Utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemoQueryServiceImpl implements MemoQueryService {

	private final MemoRepository memoRepository;

	@Override
	public MemoSliceResponseDTO getMemosWithSlice(Long userId, Integer page, Integer size) {

		int pageNumber = (page != null && page >= 0) ? page : 0;
		int pageSize = Utils.validatePageSize(size);

		PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
		Slice<Memo> memos = memoRepository.findByUserIdOrderByCreatedAtDescIdDesc(userId, pageRequest);

		return MemoSliceResponseDTO.from(memos.map(MemoSummaryResponseDTO::from));
	}

	@Override
	public MemoDetailResponseDTO getMemoDetail(Long userId, Long memoId) {

		Memo memo = memoRepository.findByIdAndUserId(memoId, userId)
			.orElseThrow(() -> new MemoException(ErrorStatus.MEMO_NOT_FOUND));

		return MemoDetailResponseDTO.from(memo);
	}

}
