package com.indayvidual.server.domain.memo.service.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.indayvidual.server.domain.memo.dto.request.CreateMemoRequestDTO;
import com.indayvidual.server.domain.memo.entity.Memo;
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
	public Void createMemo(Long userId, CreateMemoRequestDTO requestDTO) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UserException(ErrorStatus.USER_NOT_FOUND));

		Memo newMemo = Memo.createMemo(requestDTO.getTitle(), requestDTO.getContent(), user);

		memoRepository.save(newMemo);

		return null;

	}
}
