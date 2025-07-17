package com.indayvidual.server.domain.memo.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.indayvidual.server.common.BaseEntity;
import com.indayvidual.server.domain.memo.exception.MemoException;
import com.indayvidual.server.domain.user.entity.User;
import com.indayvidual.server.global.api.code.status.ErrorStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@Table(name = "memo")
public class Memo extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "memo_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	private String title;

	@Lob
	private String content;

	//== 생성 메서드 ==//
	public static Memo createMemo(String title, String content, User user) {
		return Memo.builder()
			.user(user)
			.title(title)
			.content(content)
			.build();
	}

	//== 더티체킹 메서드 ==//

	//== 비즈니스 로직 ==//
	public boolean isOwnerBy(User user) {
		return this.user.equals(user);
	}

	public void ensureOwnership(User user) {
		if (!isOwnerBy(user)) {
			throw new MemoException(ErrorStatus.MEMO_OWNER_MISMATCH);
		}
	}

	public boolean canDeleteMemo(User user) {
		ensureOwnership(user);

		user.getMemos().remove(this);

		return true;
	}
}
