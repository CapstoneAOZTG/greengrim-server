package com.greengrim.green.common.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NftSortOption {

    DESC("최신순"),
    ASC("오래된 순"),
    FAVORITE("좋아요 순");

    private final String description;
}
