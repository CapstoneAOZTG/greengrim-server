package com.greengrim.green.core.nft.controller;

import com.greengrim.green.common.oauth.auth.CurrentMember;
import com.greengrim.green.core.member.Member;
import com.greengrim.green.core.nft.usecase.RegisterNftUseCase;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RegisterNftController {

    private final RegisterNftUseCase registerNftUseCase;

    /**
     * [POST] NFT 교환하기
     */
    @Operation(summary = "NFT 교환하기")
    @PostMapping("/visitor/nfts/{id}")
    public void exchangeNft(@CurrentMember Member member, @PathVariable("id") Long id) {
        registerNftUseCase.exchangeNft(member, id);
    }
}
