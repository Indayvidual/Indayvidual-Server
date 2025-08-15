package com.indayvidual.server.common;


public final class Patterns {
    private Patterns() {}

    // JS/Java 공통 호환 패턴: 한글/영문/숫자/공백/_/-
    public static final String USERNAME_JS_COMPAT =
            "^[A-Za-z0-9 _\\-\\uAC00-\\uD7A3\\u1100-\\u11FF\\u3130-\\u318F]{2,20}$";
}
