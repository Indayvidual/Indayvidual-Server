package com.indayvidual.server.global.util;

import com.indayvidual.server.global.config.security.JwtUserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class Utils {

	private static final int DEFAULT_PAGE_SIZE = 20;
	private static final int MAX_PAGE_SIZE = 100;

	public static int validatePageSize(Integer size) {
		if (size == null)
			return DEFAULT_PAGE_SIZE;
		if (size <= 0)
			return DEFAULT_PAGE_SIZE;
		if (size > MAX_PAGE_SIZE)
			return MAX_PAGE_SIZE;
		return size;
	}

	public static Long getUserId() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || !(auth.getPrincipal() instanceof JwtUserPrincipal jwt)) {
			throw new IllegalStateException("인증되지 않은 사용자입니다.");
		}
		return jwt.userId();
	}

}
