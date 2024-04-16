package com.greengrim.green.core.hiding.nft;

import com.greengrim.green.common.oauth.auth.CurrentMember;
import com.greengrim.green.core.member.Member;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NftHidingController {

    private final NftHidingService nftHidingService;

    /**
     * [POST] Nft 숨기기
     */
    @Operation(summary = "Nft 숨기기")
    @PostMapping("/visitor/hiding/nft")
    public ResponseEntity<Integer> hideNft(
            @CurrentMember Member member,
            @RequestParam("id") Long id) {
        nftHidingService.hideNft(member, id);
        return new ResponseEntity<>(200, HttpStatus.OK);
    }
}
