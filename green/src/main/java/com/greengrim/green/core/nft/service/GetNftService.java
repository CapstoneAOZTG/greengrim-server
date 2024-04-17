package com.greengrim.green.core.nft.service;

import static com.greengrim.green.common.util.UtilService.checkIsMine;
import static com.greengrim.green.common.util.UtilService.getPageable;

import com.greengrim.green.common.entity.SortOption;
import com.greengrim.green.common.entity.dto.PageResponseDto;
import com.greengrim.green.common.exception.BaseException;
import com.greengrim.green.common.exception.errorCode.NftErrorCode;
import com.greengrim.green.core.member.Member;
import com.greengrim.green.core.nft.Nft;
import com.greengrim.green.core.nft.NftGrade;
import com.greengrim.green.core.nft.dto.NftResponseDto.NftAndMemberInfo;
import com.greengrim.green.core.nft.dto.NftResponseDto.NftDetailInfo;
import com.greengrim.green.core.nft.dto.NftResponseDto.NftStockAmountInfo;
import com.greengrim.green.core.nft.dto.NftResponseDto.NftStockInfo;
import com.greengrim.green.core.nft.repository.NftRepository;
import com.greengrim.green.core.nft.usecase.GetNftUseCase;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetNftService implements GetNftUseCase {

    private final NftRepository nftRepository;
    private final String[][] traits;

    public NftDetailInfo getNftDetailInfo(Member member, Long id) {
        Nft nft = nftRepository.findByIdAndStatusTrue(id)
                .orElseThrow(() -> new BaseException(NftErrorCode.EMPTY_NFT));

        boolean isMine = false;
        if (member != null) { // 로그인 했다면
            isMine = checkIsMine(member.getId(), nft.getMember().getId());
        }
        return new NftDetailInfo(nft, isMine, traits);
    }

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
     * TODO: 좋아요 순 추가하기
     */
    public PageResponseDto<List<NftAndMemberInfo>> getExchangedNfts(Member member, int page, int size, SortOption sortOption) {
        Page<Nft> nfts = nftRepository.findExchangedNfts(member.getId(), getPageable(page, size, sortOption));
        return makeNftsInfoList(nfts);
    }

    /**
     * 멤버 별 NFTS 보기
     */
    public PageResponseDto<List<NftAndMemberInfo>> getMemberNfts(Member member, Long targetId, int page, int size, SortOption sortOption) {
        if(targetId == null) {
            targetId = member.getId();
        }
        Page<Nft> nfts = nftRepository.findMemberNfts(member.getId(), targetId, getPageable(page, size, sortOption));
        return makeNftsInfoList(nfts);
    }

    public PageResponseDto<List<NftAndMemberInfo>> makeNftsInfoList(Page<Nft> nfts) {
        List<NftAndMemberInfo> memberNftInfos = new ArrayList<>();
        nfts.forEach(nft ->
            memberNftInfos.add(new NftAndMemberInfo(nft)));

        return new PageResponseDto<>(nfts.getNumber(), nfts.hasNext(), memberNftInfos);
    }

}
