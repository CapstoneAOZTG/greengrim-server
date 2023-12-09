package com.greengrim.green.core.nft.controller;

import com.greengrim.green.common.auth.CurrentMember;
import com.greengrim.green.common.entity.SortOption;
import com.greengrim.green.common.entity.dto.PageResponseDto;
import com.greengrim.green.core.member.Member;
import com.greengrim.green.core.nft.dto.NftResponseDto.HomeNftInfo;
import com.greengrim.green.core.nft.dto.NftResponseDto.HomeNfts;
import com.greengrim.green.core.nft.dto.NftResponseDto.NftDetailInfo;
import com.greengrim.green.core.nft.dto.NftResponseDto.NftInfoBeforeSale;
import com.greengrim.green.core.nft.service.GetNftService;
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

    private final GetNftService getNftService;

    /**
     * [GET] NFT 상세 조회
     */
    @Operation(summary = "NFT 상세 조회")
    @GetMapping("/nfts/{id}")
    public ResponseEntity<NftDetailInfo> getNftInfo(
            @CurrentMember Member member,
            @PathVariable("id") Long id) {
        return new ResponseEntity<>(
                getNftService.getNftDetailInfo(member, id),
                HttpStatus.OK);
    }

    /**
     * [GET] 홈 화면 NFT 5개 조회
     */
    @Operation(summary = "홈 화면 NFT 조회")
    @GetMapping("/home/nfts")
    public ResponseEntity<HomeNfts> getHotNfts(
            @CurrentMember Member member) {
        return ResponseEntity.ok(getNftService.getHomeNfts(member, 0,5, SortOption.DESC));
    }

    /**
     * [GET] NFT 더보기
     */
    @Operation(summary = "NFT 더보기")
    @GetMapping("/hot-nfts")
    public ResponseEntity<PageResponseDto<List<HomeNftInfo>>> getMoreHotNfts(
            @CurrentMember Member member,
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size,
            @RequestParam(value = "sort") SortOption sort) {
        return ResponseEntity.ok(getNftService.getMoreHotNfts(member, page, size, sort));
    }

    /**
     * [GET] 내 NFT 조회
     */
    @Operation(summary = "내 NFT 조회")
    @GetMapping("/member/nfts")
    public ResponseEntity<PageResponseDto<List<HomeNftInfo>>> getMoreHotChallenges(
            @CurrentMember Member member,
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size,
            @RequestParam(value = "sort") SortOption sort) {
        return ResponseEntity.ok(getNftService.getMyHotNfts(member, page, size, sort));
    }

    @Operation(summary = "NFT 판매 전 정보 조회")
    @GetMapping("/member/nfts/{id}/sales")
    public ResponseEntity<NftInfoBeforeSale> getNftInfoBeforeSale(
            @CurrentMember Member member,
            @PathVariable(value = "id") Long id) {
        return ResponseEntity.ok(getNftService.getNftInfoBeforeSale(member, id));
    }
}
