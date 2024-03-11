package com.greengrim.green.core.wallet.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class WalletResponseDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ExistsWalletInfo {
        private boolean existed;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class WalletDetailInfo {
        private String name;
        private String address;
    }
}
