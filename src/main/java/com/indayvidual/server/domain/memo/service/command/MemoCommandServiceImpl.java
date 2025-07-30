package com.indayvidual.server.domain.memo.service.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.indayvidual.server.domain.memo.dto.request.CreateMemoRequestDTO;
import com.indayvidual.server.domain.memo.dto.request.UpdateMemoRequestDTO;
import com.indayvidual.server.domain.memo.dto.response.MemoDetailResponseDTO;
import com.indayvidual.server.domain.memo.entity.Memo;
import com.indayvidual.server.domain.memo.exception.MemoException;
import com.indayvidual.server.domain.memo.repository.MemoRepository;
import com.indayvidual.server.domain.user.entity.User;
import com.indayvidual.server.domain.user.repository.UserRepository;
import com.indayvidual.server.global.api.code.status.ErrorStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemoCommandServiceImpl implements MemoCommandService {

	private final UserRepository userRepository;
	private final MemoRepository memoRepository;

	@Override
	public MemoDetailResponseDTO createMemo(Long userId, CreateMemoRequestDTO requestDTO) {
		User currentUser = getCurrentUser(userId);

		Memo newMemo = Memo.createMemo(requestDTO.getTitle(), requestDTO.getContent(), currentUser);

		memoRepository.save(newMemo);

		return MemoDetailResponseDTO.from(newMemo);

	}

	@Override
	public Void deleteMemo(Long userId, Long memoId) {

		Memo memo = getMemoByMemoIdAndUserId(memoId, userId);

		memoRepository.delete(memo);

		return null;
	}

	@Override
	public MemoDetailResponseDTO updateMemo(Long userId, Long memoId, UpdateMemoRequestDTO request) {
		Memo memo = getMemoByMemoIdAndUserId(memoId, userId);

		memo.updateTitle(request.getTitle());
		memo.updateContent(request.getContent());

		return MemoDetailResponseDTO.from(memo);
	}

	private User getCurrentUser(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new MemoException(ErrorStatus.USER_NOT_FOUND));
	}

	private Memo getMemoByMemoIdAndUserId(Long memoId, Long userId) {
		return memoRepository.findById(memoId)
			.orElseThrow(() -> new MemoException(ErrorStatus.MEMO_NOT_FOUND));
	}
}
