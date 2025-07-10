package com.indayvidual.server.domain.memo.service.command;

import org.springframework.stereotype.Service;

import com.indayvidual.server.domain.memo.dto.request.CreateMemoRequestDTO;
import com.indayvidual.server.domain.memo.entity.Memo;
import com.indayvidual.server.domain.memo.repository.MemoRepository;
import com.indayvidual.server.domain.user.entity.User;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemoCommandServiceImpl implements MemoCommandService {

	private final MemoRepository memoRepository;

	@Override
	public Void createMemo(CreateMemoRequestDTO requestDTO) {
		// TODO: 사용자의 primary key, 혹은 email로 조회하는 로직 추가

		User user = null;
		Memo newMemo = Memo.createMemo(requestDTO.getTitle(), requestDTO.getContent(), user);

		memoRepository.save(newMemo);

		return null;

	}
}
