package com.indayvidual.server.domain.memo.service.query;

import com.indayvidual.server.domain.memo.dto.request.MemoSliceResponseDTO;

public interface MemoQueryService {
	MemoSliceResponseDTO getMemosWithSlice(Long userId, Integer page, Integer size);
}
