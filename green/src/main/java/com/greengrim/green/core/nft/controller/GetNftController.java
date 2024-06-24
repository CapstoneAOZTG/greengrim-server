package com.greengrim.green.core.nft.controller;

import com.greengrim.green.common.entity.NftSortOption;
import com.greengrim.green.common.oauth.auth.CurrentMember;
import com.greengrim.green.common.entity.dto.PageResponseDto;
import com.greengrim.green.core.member.Member;
import com.greengrim.green.core.nft.entity.NftGrade;
import com.greengrim.green.core.nft.dto.NftResponseDto.HotNftInfo;
import com.greengrim.green.core.nft.dto.NftResponseDto.NftAndMemberInfo;
import com.greengrim.green.core.nft.dto.NftResponseDto.NftCollectionInfo;
import com.greengrim.green.core.nft.dto.NftResponseDto.NftDetailInfo;
import com.greengrim.green.core.nft.dto.NftResponseDto.NftStockAmountInfo;
import com.greengrim.green.core.nft.dto.NftResponseDto.NftStockInfo;
import com.greengrim.green.core.nft.usecase.GetNftUseCase;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/visitor/nfts")
@Tag(name = "NFT")
public class GetNftController {

    private final GetNftUseCase getNftUseCase;

    /**
     * [GET] NFT 상세 조회
     */
    @Operation(summary = "NFT 상세 조회")
    @GetMapping("/{id}")
    public ResponseEntity<NftDetailInfo> getNftInfo(
            @CurrentMember Member member,
            @PathVariable("id") Long id) {
        return new ResponseEntity<>(
            getNftUseCase.getNftDetailInfo(member, id),
                HttpStatus.OK);
    }

    /**
     * [GET] NFT 잔량 조회
     */
    @Operation(summary = "NFT 잔량 조회")
    @GetMapping("/stock/amount")
    public ResponseEntity<NftStockAmountInfo> getNftStockAmountInfo() {
        return new ResponseEntity<>(
            getNftUseCase.getNftStockAmountInfo(),
            HttpStatus.OK);
    }

    /**
     * [GET] NFT Collection 상세 조회
     */
    @Operation(summary = "NFT Collection 상세 조회")
    @GetMapping("/stock/{id}")
    public ResponseEntity<NftStockInfo> getNftStockDetailInfo(
            @PathVariable("id") Long id) {
        return new ResponseEntity<>(
                getNftUseCase.getNftStockDetailInfo(id),
                HttpStatus.OK);
    }

    /**
     * [GET] 교환할 NFT 조회
     */
    @Operation(summary = "교환할 NFT 조회")
    @GetMapping("/stock")
    public ResponseEntity<NftStockInfo> getNftStockInfo(
        @RequestParam(value = "grade") NftGrade grade) {
        return new ResponseEntity<>(
            getNftUseCase.getNftStockInfo(grade),
            HttpStatus.OK);
    }

    /**
     * [GET] 교환할 NFT 조회 새로고침
     */
    @Operation(summary = "교환할 NFT 조회 새로고침")
    @GetMapping("/stock/refresh")
    public ResponseEntity<NftStockInfo> getNftStockInfoRefresh(
        @RequestParam(value = "grade") NftGrade grade,
        @RequestParam(value = "nftList") List<Long> nftList) {
        return new ResponseEntity<>(
            getNftUseCase.getNftStockInfoRefresh(grade, nftList),
            HttpStatus.OK);
    }

    /**
     * [GET] 교환된 NFT List 보기
     */
    @Operation(summary = "교환된 NFT List 보기")
    @GetMapping
    public ResponseEntity<PageResponseDto<List<NftAndMemberInfo>>> getExchangedNfts(
            @CurrentMember Member member,
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size,
            @RequestParam(value = "sort") NftSortOption sort) {
        return ResponseEntity.ok(getNftUseCase.getExchangedNfts(member, page, size, sort));
    }

    /**
     * [GET] NFT Collection 조회
     */
    @Operation(summary = "NFT Collection 조회")
    @GetMapping("/collection")
    public ResponseEntity<PageResponseDto<List<NftCollectionInfo>>> getCollectionNfts(
        @CurrentMember Member member,
        @RequestParam(value = "grade") NftGrade grade,
        @RequestParam(value = "page") int page,
        @RequestParam(value = "size") int size
        ) {
        return ResponseEntity.ok(getNftUseCase.getCollectionNfts(grade, page, size));
    }

    /**
     * [GET] 멤버 별 NFT 조회
     */
    @Operation(summary = "멤버 별 NFT 조회",
        description = "자신의 것을 조회하고 싶다면 memberId는 안 보내셔도 됩니다!")
    @GetMapping("/profile")
    public ResponseEntity<PageResponseDto<List<NftAndMemberInfo>>> getProfileNfts(
            @CurrentMember Member member,
            @RequestParam(value = "memberId", required = false) Long memberId,
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size,
            @RequestParam(value = "sort") NftSortOption sort) {
        return ResponseEntity.ok(getNftUseCase.getMemberNfts(member, memberId, page, size, sort));
    }

    /**
     * [GET] 홈 화면 핫 NFT 조회
     */
    @Operation(summary = "홈 화면 핫 NFT 조회",
            description = "한 달 동안 가장 좋아요를 많이 받은 NFT 순으로 5개 내려갑니다!")
    @GetMapping("/home/hot-nfts")
    public ResponseEntity<PageResponseDto<List<HotNftInfo>>> getHotNfts(
            @CurrentMember Member member) {
        return ResponseEntity.ok(getNftUseCase.getHotNfts(member));
    }

}
