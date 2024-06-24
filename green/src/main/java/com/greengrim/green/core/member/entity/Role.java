package com.greengrim.green.core.member.entity;

public enum Role {
    ROLE_VISITOR("지갑 미보유 회원"),
    ROLE_MEMBER("지갑 보유 회원"),
    ROLE_MANAGER("관리자");

    private final String name;

    Role(String name) {
        this.name = name;
    }
}
