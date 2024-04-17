package com.greengrim.green.core.nftlike.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class LikeResponseDto {

    @Getter
    @RequiredArgsConstructor
    public static class PushLikeInfo {
        private boolean status;

        public PushLikeInfo(boolean status) {
            this.status = status;
        }
    }


}
