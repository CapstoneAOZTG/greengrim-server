package com.greengrim.green.core.nft.controller;

import com.greengrim.green.common.oauth.auth.CurrentMember;
import com.greengrim.green.core.member.Member;
import com.greengrim.green.core.nft.service.UpdateNftService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UpdateNftController {

    private final UpdateNftService updateNftService;

    @Operation(summary = "NFT 삭제하기")
    @DeleteMapping("/member/nfts/{id}")
    public ResponseEntity<Integer> deleteNft(
        @CurrentMember Member member,
        @PathVariable("id") Long id) {
        updateNftService.delete(member, id);
        return new ResponseEntity<>(200, HttpStatus.OK);
    }
}
