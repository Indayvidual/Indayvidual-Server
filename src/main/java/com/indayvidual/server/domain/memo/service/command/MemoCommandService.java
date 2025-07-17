package com.indayvidual.server.domain.memo.service.command;

import com.indayvidual.server.domain.memo.dto.request.CreateMemoRequestDTO;
import com.indayvidual.server.domain.memo.dto.response.MemoDetailResponseDTO;

public interface MemoCommandService {

	MemoDetailResponseDTO createMemo(Long userId, CreateMemoRequestDTO requestDTO);

	Void deleteMemo(Long userId, Long memoId);

}
