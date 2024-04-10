package com.greengrim.green.core.nft.usecase;

import com.greengrim.green.common.entity.SortOption;
import com.greengrim.green.common.entity.dto.PageResponseDto;
import com.greengrim.green.core.member.Member;
import com.greengrim.green.core.nft.Nft;
import com.greengrim.green.core.nft.dto.NftResponseDto.NftAndMemberInfo;
import com.greengrim.green.core.nft.dto.NftResponseDto.NftDetailInfo;
import java.util.List;
import org.springframework.data.domain.Page;

public interface GetNftUseCase {

    NftDetailInfo getNftDetailInfo(Member member, Long id);

    PageResponseDto<List<NftAndMemberInfo>> getMemberNfts(Long memberId, int page, int size, SortOption sortOption);

    PageResponseDto<List<NftAndMemberInfo>> getExchangedNfts(Member member, int page, int size, SortOption sortOption);

    PageResponseDto<List<NftAndMemberInfo>> makeNftsInfoList(Page<Nft> nfts);
}