package com.indayvidual.server.domain.memo.service.query;

import com.indayvidual.server.domain.memo.dto.request.MemoSliceResponseDTO;
import com.indayvidual.server.domain.memo.dto.response.MemoDetailResponseDTO;

public interface MemoQueryService {
	MemoSliceResponseDTO getMemosWithSlice(Long userId, Integer page, Integer size);

	MemoDetailResponseDTO getMemoDetail(Long userId, Long memoId);
}
