package com.greengrim.green.core.nftlike.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class LikeRequestDto {

    @Getter
    @RequiredArgsConstructor
    public static class RegisterLike {
        private Long nftId;
    }
}
