package com.greengrim.green.core.nft.service;

import static com.greengrim.green.common.util.UtilService.checkIsMine;
import static com.greengrim.green.common.util.UtilService.getPageable;

import com.greengrim.green.common.entity.SortOption;
import com.greengrim.green.common.entity.dto.PageResponseDto;
import com.greengrim.green.common.exception.BaseException;
import com.greengrim.green.common.exception.errorCode.NftErrorCode;
import com.greengrim.green.core.member.Member;
import com.greengrim.green.core.nft.Nft;
import com.greengrim.green.core.nft.dto.NftResponseDto.HomeNftInfo;
import com.greengrim.green.core.nft.dto.NftResponseDto.HomeNfts;
import com.greengrim.green.core.nft.dto.NftResponseDto.NftAndMemberInfo;
import com.greengrim.green.core.nft.dto.NftResponseDto.NftDetailInfo;
import com.greengrim.green.core.nft.repository.NftRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetNftService {

    private final NftRepository nftRepository;

    public NftDetailInfo getNftDetailInfo(Member member, Long id) {
        Nft nft = nftRepository.findByIdAndStatusTrue(id)
                .orElseThrow(() -> new BaseException(NftErrorCode.EMPTY_NFT));

        boolean isMine = false;
        if (member != null) { // 로그인 했다면
            isMine = checkIsMine(member.getId(), nft.getMember().getId());
        }
        return new NftDetailInfo(nft, isMine);
    }

    public HomeNfts getHomeNfts(Member member, int page, int size, SortOption sortOption) {
        Page<Nft> nfts = nftRepository.findHomeNfts(getPageable(page, size, sortOption));

        List<HomeNftInfo> homeNftInfoList = new ArrayList<>();
        nfts.forEach(nft ->
                homeNftInfoList.add(new HomeNftInfo(nft)));

        return new HomeNfts(homeNftInfoList);
    }

    /**
     * 핫 NFTS 더보기
     * TODO: @param member 를 이용해 차단 목록에 있다면 보여주지 않기
     */
    public PageResponseDto<List<HomeNftInfo>> getMoreHotNfts(Member member, int page, int size, SortOption sortOption) {
        Page<Nft> nfts = nftRepository.findHomeNfts(getPageable(page, size, sortOption));
        return makeNftsInfoList(nfts);
    }

    /**
     * 내 NFTS 더보기
     * TODO: @param member 를 이용해 차단 목록에 있다면 보여주지 않기
     */
    public PageResponseDto<List<HomeNftInfo>> getMyHotNfts(Member member, int page, int size, SortOption sortOption) {
        Page<Nft> nfts = nftRepository.findMyNfts(member, getPageable(page, size, sortOption));
        return makeNftsInfoList(nfts);
    }

    /**
     * NFT 판매 전 정보 조회
     */
    public NftAndMemberInfo getNftInfoBeforeSale(Member member, Long id) {
        Nft nft = nftRepository.findByIdAndStatusTrue(id)
                .orElseThrow(() -> new BaseException(NftErrorCode.EMPTY_NFT));
        if (!checkIsMine(member.getId(), nft.getMember().getId())) {
            throw new BaseException(NftErrorCode.NO_AUTHORIZATION);
        }
        return new NftAndMemberInfo(nft);
    }

    private PageResponseDto<List<HomeNftInfo>> makeNftsInfoList(Page<Nft> nfts) {
        List<HomeNftInfo> homeNftInfos = new ArrayList<>();
        nfts.forEach(nft ->
                homeNftInfos.add(new HomeNftInfo(nft)));

        return new PageResponseDto<>(nfts.getNumber(), nfts.hasNext(), homeNftInfos);
    }

}
