package com.greengrim.green.core.nft.usecase;

import com.greengrim.green.common.entity.NftSortOption;
import com.greengrim.green.common.entity.dto.PageResponseDto;
import com.greengrim.green.core.member.Member;
import com.greengrim.green.core.nft.Nft;
import com.greengrim.green.core.nft.NftGrade;
import com.greengrim.green.core.nft.dto.NftResponseDto.NftAndMemberInfo;
import com.greengrim.green.core.nft.dto.NftResponseDto.NftCollectionInfo;
import com.greengrim.green.core.nft.dto.NftResponseDto.NftDetailInfo;
import com.greengrim.green.core.nft.dto.NftResponseDto.NftStockAmountInfo;
import com.greengrim.green.core.nft.dto.NftResponseDto.NftStockInfo;
import java.util.List;
import org.springframework.data.domain.Page;

public interface GetNftUseCase {

    NftDetailInfo getNftDetailInfo(Member member, Long id);

    NftStockInfo getNftStockInfo(NftGrade grade);

    NftStockAmountInfo getNftStockAmountInfo();

    NftStockInfo getNftStockInfoRefresh(NftGrade grade, List<Long> nftList);

    PageResponseDto<List<NftCollectionInfo>> getCollectionNfts(NftGrade grade, int page, int size);

    PageResponseDto<List<NftAndMemberInfo>> getMemberNfts(Member member, Long memberId, int page, int size, NftSortOption sortOption);

    PageResponseDto<List<NftAndMemberInfo>> getExchangedNfts(Member member, int page, int size, NftSortOption sortOption);

    PageResponseDto<List<NftAndMemberInfo>> makeNftsInfoList(Long memberId, Page<Nft> nfts);

}
