package com.greengrim.green.core.nft.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class NftRequestDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisterNft {
        @NotBlank(message = "그림 ID는 빈 값일 수 없습니다.")
        private Long grimId;
        @NotBlank(message = "제목은 빈 값일 수 없습니다.")
        private String title;
        @NotBlank(message = "설명은 빈 값일 수 없습니다.")
        private String description;
    }


}
