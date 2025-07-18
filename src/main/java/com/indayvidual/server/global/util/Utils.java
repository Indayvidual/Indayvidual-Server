package com.indayvidual.server.global.util;

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
		return Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getName());
	}
}
