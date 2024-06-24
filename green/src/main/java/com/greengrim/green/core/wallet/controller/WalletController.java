package com.greengrim.green.core.wallet.controller;

import com.greengrim.green.common.oauth.auth.CurrentMember;
import com.greengrim.green.core.member.Member;
import com.greengrim.green.core.wallet.dto.WalletRequestDto.WalletRequest;
import com.greengrim.green.core.wallet.dto.WalletResponseDto.WalletDetailInfo;
import com.greengrim.green.core.wallet.service.WalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "지갑")
public class WalletController {

    private final WalletService walletService;

    /**
     * [POST] 지갑 추가하기
     */
    @Operation(summary = "지갑 추가하기")
    @PostMapping("/visitor/wallets")
    public ResponseEntity<Integer> registerWallet(
            @Valid @RequestBody WalletRequest walletRequest, @CurrentMember Member member) {
        walletService.registerWallet(member, walletRequest);
        return new ResponseEntity<>(200, HttpStatus.OK);
    }

    /**
     * [PATCH] 지갑 수정하기
     */
    @Operation(summary = "지갑 수정하기")
    @PostMapping("/member/wallets")
    public ResponseEntity<Integer> modifyWallet(
            @Valid @RequestBody WalletRequest walletRequest, @CurrentMember Member member) {
        walletService.modifyWallet(member, walletRequest);
        return new ResponseEntity<>(200, HttpStatus.OK);
    }

    /**
     * [GET] 지갑 정보 가져오기
     */
    @Operation(summary = "지갑 정보 가져오기")
    @GetMapping("/visitor/wallets")
    public ResponseEntity<WalletDetailInfo> getWalletDetail(@CurrentMember Member member){
        return new ResponseEntity<>(walletService.getWalletDetail(member), HttpStatus.OK);
    }
}
