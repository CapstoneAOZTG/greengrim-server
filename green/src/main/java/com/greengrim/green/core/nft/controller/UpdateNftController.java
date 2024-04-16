package com.greengrim.green.core.nft.controller;

import com.greengrim.green.common.oauth.auth.CurrentMember;
import com.greengrim.green.core.member.Member;
import com.greengrim.green.core.nft.dto.NftRequestDto.NftModifyInfo;
import com.greengrim.green.core.nft.dto.NftResponseDto.NftDetailInfo;
import com.greengrim.green.core.nft.usecase.UpdateNftUseCase;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UpdateNftController {

    private final UpdateNftUseCase updateNftUseCase;

    /**
     * [DELETE] NFT 삭제하기
     */
    @Operation(summary = "NFT 삭제하기")
    @DeleteMapping("/visitor/nfts/{id}")
    public ResponseEntity<Integer> deleteNft(@CurrentMember Member member, @PathVariable("id") Long id) {
        updateNftUseCase.delete(member, id);
        return new ResponseEntity<>(200, HttpStatus.OK);
    }

    /**
     * [PATCH] NFT 수정하기
     */
    @Operation(summary = "NFT 수정하기")
    @PatchMapping("/visitor/nfts")
    public ResponseEntity<NftDetailInfo> modifyNft(@CurrentMember Member member, @RequestBody NftModifyInfo modifyInfo) {
        return new ResponseEntity<>(updateNftUseCase.modify(member, modifyInfo), HttpStatus.OK);
    }
}
