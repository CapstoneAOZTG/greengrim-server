package com.greengrim.green.core.nftlike.controller;

import com.greengrim.green.common.oauth.auth.CurrentMember;
import com.greengrim.green.core.member.Member;
import com.greengrim.green.core.nftlike.service.LikeService;
import com.greengrim.green.core.nftlike.dto.LikeRequestDto.RegisterLike;
import com.greengrim.green.core.nftlike.dto.LikeResponseDto.PushLikeInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/visitor/nft-likes")
@Tag(name = "NFT")
public class LikeController {

    private final LikeService likeService;

    /**
     * [POST] 좋아요 누르기
     */
    @Operation(summary = "좋아요 누르기")
    @PostMapping
    public ResponseEntity<PushLikeInfo> pushLike(
            @CurrentMember Member member,
            @RequestBody RegisterLike registerLike) {
        return ResponseEntity.ok(likeService.pushLike(member, registerLike));
    }
}
