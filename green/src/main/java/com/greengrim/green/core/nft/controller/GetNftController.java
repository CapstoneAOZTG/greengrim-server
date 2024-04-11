package com.greengrim.green.core.nft.controller;

import com.greengrim.green.common.auth.CurrentMember;
import com.greengrim.green.common.entity.SortOption;
import com.greengrim.green.common.entity.dto.PageResponseDto;
import com.greengrim.green.core.member.Member;
import com.greengrim.green.core.nft.NftGrade;
import com.greengrim.green.core.nft.dto.NftResponseDto.NftAndMemberInfo;
import com.greengrim.green.core.nft.dto.NftResponseDto.NftDetailInfo;
import com.greengrim.green.core.nft.dto.NftResponseDto.NftStockInfo;
import com.greengrim.green.core.nft.usecase.GetNftUseCase;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GetNftController {

    private final GetNftUseCase getNftUseCase;

    /**
     * [GET] NFT 상세 조회
     */
    @Operation(summary = "NFT 상세 조회")
    @GetMapping("/nfts/{id}")
    public ResponseEntity<NftDetailInfo> getNftInfo(
            @CurrentMember Member member,
            @PathVariable("id") Long id) {
        return new ResponseEntity<>(
            getNftUseCase.getNftDetailInfo(member, id),
                HttpStatus.OK);
    }

    /**
     * [GET] 교환할 NFT 조회
     */
    @Operation(summary = "교환할 NFT 조회")
    @GetMapping("/visitor/nfts/stock")
    public ResponseEntity<NftStockInfo> getNftStockInfo(
        @RequestParam(value = "grade") NftGrade grade) {
        return new ResponseEntity<>(
            getNftUseCase.getNftStockInfo(grade),
            HttpStatus.OK);
    }

    /**
     * [GET] 교환된 NFT List 보기
     */
    @Operation(summary = "교환된 NFT List 보기")
    @GetMapping("/visitor/nfts")
    public ResponseEntity<PageResponseDto<List<NftAndMemberInfo>>> getExchangedNfts(
            @CurrentMember Member member,
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size,
            @RequestParam(value = "sort") SortOption sort) {
        return ResponseEntity.ok(getNftUseCase.getExchangedNfts(member, page, size, sort));
    }

    /**
     * [GET] 멤버 별 NFT 조회
     */
    @Operation(summary = "멤버 별 NFT 조회",
        description = "자신의 것을 조회하고 싶다면 memberId는 안 보내셔도 됩니다!")
    @GetMapping("/visitor/nfts/profile")
    public ResponseEntity<PageResponseDto<List<NftAndMemberInfo>>> getProfileNfts(
            @CurrentMember Member member,
            @RequestParam(value = "memberId", required = false) Long memberId,
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size,
            @RequestParam(value = "sort") SortOption sort) {
        return ResponseEntity.ok(getNftUseCase.getMemberNfts(member, memberId, page, size, sort));
    }

}
