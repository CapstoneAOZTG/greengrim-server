package com.greengrim.green.core.nftlike;

import com.greengrim.green.common.oauth.auth.CurrentMember;
import com.greengrim.green.core.member.Member;
import com.greengrim.green.core.nftlike.dto.LikeRequestDto.RegisterLike;
import com.greengrim.green.core.nftlike.dto.LikeResponseDto.PushLikeInfo;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    /**
     * [POST] 좋아요 누르기
     */
    @Operation(summary = "좋아요 누르기")
    @PostMapping("/visitor/nfts/like")
    public ResponseEntity<PushLikeInfo> pushLike(
            @CurrentMember Member member,
            @RequestBody RegisterLike registerLike) {
        return ResponseEntity.ok(likeService.pushLike(member, registerLike));
    }
}
