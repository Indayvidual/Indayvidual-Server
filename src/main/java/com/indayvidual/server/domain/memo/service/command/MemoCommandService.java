package com.indayvidual.server.domain.memo.service.command;

import com.indayvidual.server.domain.memo.dto.request.CreateMemoRequestDTO;

public interface MemoCommandService {

	Void createMemo(Long userId, CreateMemoRequestDTO requestDTO);

	Void deleteMemo(Long userId, Long memoId);

}
