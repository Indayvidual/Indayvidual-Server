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
import com.indayvidual.server.domain.user.exception.UserException;
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

		Memo newMemo = Memo.createMemo(requestDTO.getContent(), currentUser);

		memoRepository.save(newMemo);

		return MemoDetailResponseDTO.from(newMemo);

	}

	@Override
	public Void deleteMemo(Long userId, Long memoId) {
		User currentUser = getCurrentUser(userId);

		Memo memo = getMemo(memoId);

		if (memo.canDeleteMemo(currentUser)) {
			memoRepository.delete(memo);
		}

		return null;
	}

	@Override
	public MemoDetailResponseDTO updateMemo(Long userId, Long memoId, UpdateMemoRequestDTO request) {
		User currentUser = getCurrentUser(userId);

		Memo memo = getMemo(memoId);

		memo.updateContent(request.getContent());

		return MemoDetailResponseDTO.from(memo);
	}

	private Memo getMemo(Long memoId) {
		return memoRepository.findById(memoId)
			.orElseThrow(() -> new MemoException(ErrorStatus.MEMO_NOT_FOUND));
	}

	private User getCurrentUser(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new UserException(ErrorStatus.USER_NOT_FOUND));
	}
}
