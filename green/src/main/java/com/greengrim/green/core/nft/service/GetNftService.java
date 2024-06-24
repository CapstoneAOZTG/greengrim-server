package com.greengrim.green.core.nft.service;

import static com.greengrim.green.common.util.UtilService.checkIsMine;
import static com.greengrim.green.common.util.UtilService.getNftPageable;

import com.greengrim.green.common.entity.NftSortOption;
import com.greengrim.green.common.entity.dto.PageResponseDto;
import com.greengrim.green.common.exception.BaseException;
import com.greengrim.green.common.exception.errorCode.NftErrorCode;
import com.greengrim.green.core.member.entity.Member;
import com.greengrim.green.core.nft.entity.Nft;
import com.greengrim.green.core.nft.entity.NftGrade;
import com.greengrim.green.core.nft.dto.NftResponseDto.HotNftInfo;
import com.greengrim.green.core.nft.dto.NftResponseDto.NftAndMemberInfo;
import com.greengrim.green.core.nft.dto.NftResponseDto.NftCollectionInfo;
import com.greengrim.green.core.nft.dto.NftResponseDto.NftDetailInfo;
import com.greengrim.green.core.nft.dto.NftResponseDto.NftStockAmountInfo;
import com.greengrim.green.core.nft.dto.NftResponseDto.NftStockInfo;
import com.greengrim.green.core.nft.repository.NftRepository;
import com.greengrim.green.core.nft.usecase.GetNftUseCase;
import com.greengrim.green.core.nftlike.service.LikeService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetNftService implements GetNftUseCase {

    private final NftRepository nftRepository;
    private final LikeService likeService;
    private final String[][] traits;

    /**
     * NFT 상세 조회
     */
    public NftDetailInfo getNftDetailInfo(Member member, Long id) {
        Nft nft = nftRepository.findByIdAndStatusTrue(id)
                .orElseThrow(() -> new BaseException(NftErrorCode.EMPTY_NFT));

        boolean isMine = false;
        boolean isLiked = false;
        if (member != null) { // 로그인 했다면
            isMine = checkIsMine(member.getId(), nft.getMember().getId());
            isLiked = checkIsLiked(member.getId(), nft);
        }

        return new NftDetailInfo(nft, isMine, isLiked, traits);
    }

    /**
     * NFT 잔량 조회
     */
    public NftStockAmountInfo getNftStockAmountInfo() {
        List<Object[]> nftCounts = nftRepository.countNftsByGrade();

        long basic = 0;
        long standard = 0;
        long premium = 0;

        for (Object[] result : nftCounts) {
            NftGrade grade = (NftGrade) result[0];
            long count = (Long) result[1];

            switch (grade) {
                case BASIC:
                    basic = count;
                    break;
                case STANDARD:
                    standard = count;
                    break;
                case PREMIUM:
                    premium = count;
                    break;
            }
        }

        return new NftStockAmountInfo((int) basic, (int) standard, (int) premium);
    }

    /**
     * NFT Collection 상세 조회
     */
    public NftStockInfo getNftStockDetailInfo(Long id) {
        Nft nft = nftRepository.findByIdAndStatusTrue(id)
            .orElseThrow(() -> new BaseException(NftErrorCode.EMPTY_NFT));

        return new NftStockInfo(nft, traits);
    }

    /**
     * 교환할 NFT 조회
     */
    public NftStockInfo getNftStockInfo(NftGrade grade) {
        Nft nft = nftRepository.findRandomByGrade(grade)
                .orElseThrow(() -> new BaseException(NftErrorCode.EMPTY_NFT));

        return new NftStockInfo(nft, traits);
    }


    /**
     * 교환할 NFT 조회 새로고침
     */
    public NftStockInfo getNftStockInfoRefresh(NftGrade grade, List<Long> nftList) {

        Nft nft = nftRepository.findRandomByGradeExceptList(grade, nftList)
            .orElseThrow(() -> new BaseException(NftErrorCode.EMPTY_NFT));

        return new NftStockInfo(nft, traits);
    }

    /**
     * 교환된 NFT List 조회
     */
    public PageResponseDto<List<NftAndMemberInfo>> getExchangedNfts(Member member, int page, int size, NftSortOption sortOption) {
        Page<Nft> nfts = nftRepository.findExchangedNfts(member.getId(), getNftPageable(page, size, sortOption));
        return makeNftsInfoList(member.getId(), nfts);
    }

    /**
     * NFT Collection 조회
     */
    public PageResponseDto<List<NftCollectionInfo>> getCollectionNfts(NftGrade grade, int page, int size) {
        Page<Nft> nfts = nftRepository.findCollectionNfts(grade, getNftPageable(page, size, NftSortOption.TOKEN_ID));
        List<NftCollectionInfo> nftCollectionInfos = new ArrayList<>();
        nfts.forEach(nft ->
            nftCollectionInfos.add(
                new NftCollectionInfo(nft)));

        return new PageResponseDto<>(nfts.getNumber(), nfts.hasNext(), nftCollectionInfos);
    }

    /**
     * 멤버 별 NFTS 보기
     */
    public PageResponseDto<List<NftAndMemberInfo>> getMemberNfts(Member member, Long targetId, int page, int size, NftSortOption sortOption) {
        if(targetId == null) {
            targetId = member.getId();
        }
        Page<Nft> nfts = nftRepository.findMemberNfts(member.getId(), targetId, getNftPageable(page, size, sortOption));
        return makeNftsInfoList(member.getId(), nfts);
    }

    /**
     * Hot NFT 조회
     */
    public PageResponseDto<List<HotNftInfo>> getHotNfts(Member member) {
        Page<Nft> nfts = nftRepository.findHotNfts(
                member.getId(),
                LocalDateTime.now().minusMonths(1),
                PageRequest.of(0, 5));
        return makeHotNftInfoList(nfts);
    }

    public PageResponseDto<List<NftAndMemberInfo>> makeNftsInfoList(Long memberId, Page<Nft> nfts) {
        List<NftAndMemberInfo> memberNftInfos = new ArrayList<>();
        nfts.forEach(nft ->
            memberNftInfos.add(
                    new NftAndMemberInfo(
                            nft, checkIsLiked(memberId, nft))));

        return new PageResponseDto<>(nfts.getNumber(), nfts.hasNext(), memberNftInfos);
    }

    public PageResponseDto<List<HotNftInfo>> makeHotNftInfoList(Page<Nft> nfts) {
        List<HotNftInfo> hotNftInfos = new ArrayList<>();
        nfts.forEach(nft ->
                hotNftInfos.add(new HotNftInfo(nft)));

        return new PageResponseDto<>(nfts.getNumber(), nfts.hasNext(), hotNftInfos);
    }

    public boolean checkIsLiked(Long memberId, Nft nft) {
        return likeService.checkIsLiked(memberId, nft);
    }

}
